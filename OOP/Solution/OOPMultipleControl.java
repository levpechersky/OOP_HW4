package OOP.Solution;

import OOP.Provided.*;
import OOP.Provided.OOPInaccessibleMethod.ForbiddenAccess;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class OOPMultipleControl {

    private Class<?> interfaceClass;
    private File sourceFile;

    public OOPMultipleControl(Class<?> interfaceClass, File sourceFile) {
        this.interfaceClass = interfaceClass;
        this.sourceFile = sourceFile;
    }

    public void validateInheritanceGraph() throws OOPMultipleException {
        checkGraphAnnotations();
        checkGraphInnerCalls();
        checkGraphInherentAmbiguities();
    }

    //TODO: fill in here :
    public Object invoke(String methodName, Object[] args)
            throws OOPMultipleException {
        return null;
    }

    public void removeSourceFile() {
        if (sourceFile.exists()) {
            sourceFile.delete();
        }
    }

    private void checkGraphAnnotations() throws OOPBadClass {
        try {
            // Root of inheritance graph doesn't need to be annotated, so we don't check it.
            doBFS(this.interfaceClass, (aClass, unused) -> {
                    if (aClass != this.interfaceClass)
                        checkAnnotationsOfInterface(aClass);
                }, null);
        } catch (OOPBadClass e) {
            throw e;
        } catch (OOPMultipleException e){
            // We don't expect exceptions other than OOPBadClass
            e.printStackTrace();
            assert false;
        }
    }

    private static void checkAnnotationsOfInterface(Class<?> interfaceToCheck) throws OOPBadClass {
        assert interfaceToCheck.isInterface(); // Sanity check. We don't expect non-interface classes

        // 1. interfaceToCheck has OOPMultipleInterface annotation
        if (!interfaceToCheck.isAnnotationPresent(OOPMultipleInterface.class))
            throw new OOPBadClass(interfaceToCheck);

        // 2. each method of interfaceToCheck has OOPMultipleMethod annotation
        for (Method method : interfaceToCheck.getDeclaredMethods()){
            if (!method.isAnnotationPresent(OOPMultipleMethod.class))
                throw new OOPBadClass(method);
        }
    }

    private void checkGraphInnerCalls() throws OOPInaccessibleMethod {
        Collection<ForbiddenAccess> badInnerMethods = new ArrayList<>();
        try {

            doBFS(this.interfaceClass, (aClass, accumulator) -> {
                    checkInnerCallsOfInterface(aClass, (Collection<ForbiddenAccess>) accumulator);
                }, badInnerMethods);

            if (!badInnerMethods.isEmpty())
                throw new OOPInaccessibleMethod(badInnerMethods);

        } catch (OOPInaccessibleMethod e){
            throw e;
        }catch (OOPMultipleException e){
            // We don't expect exceptions other than OOPInaccessibleMethod
            e.printStackTrace();
            assert false;
        }
    }

    private static void checkInnerCallsOfInterface(Class<?> interfaceToCheck, Collection<ForbiddenAccess> badInnerCalls)
            throws OOPInaccessibleMethod {
        assert interfaceToCheck.isInterface(); // Sanity check. We don't expect non-interface classes

        for (Method callingMethod : interfaceToCheck.getDeclaredMethods()) {
            // Assumption: no annotation => no inner call
            if (!callingMethod.isAnnotationPresent(OOPInnerMethodCall.class))
                continue;

            OOPInnerMethodCall innerCallAnnotation = callingMethod.getAnnotation(OOPInnerMethodCall.class);
            Class<?> caller = innerCallAnnotation.caller();
            Class<?> callee = innerCallAnnotation.callee();
            try {
                Method calledMethod = caller.getMethod(innerCallAnnotation.methodName(), innerCallAnnotation.argTypes());
                OOPMultipleMethod methodAnnotation = calledMethod.getAnnotation(OOPMultipleMethod.class);

                if (methodAnnotation.modifier().equals(OOPMethodModifier.PRIVATE))
                    badInnerCalls.add(new ForbiddenAccess(caller, callee, calledMethod));

            } catch (NoSuchMethodException e) {
                // If this happens - there's probably a mistake in OOPInnerMethodCall annotation
                e.printStackTrace();
                assert false;
            }
        }
    }

    // TODO: refactor this
    private void checkGraphInherentAmbiguities() throws OOPInherentAmbiguity {
        Deque<Class<?>> topoSort = topologicalSort(this.interfaceClass);

        Map<Class<?>, List<InheritedMethod>> inheritedAndDeclaredMethods = topoSort.stream()
                .collect(Collectors.toMap(Function.identity(), unused -> new LinkedList<>()));

        Iterator i = topoSort.descendingIterator();
        while (i.hasNext()){
            Class<?> current = (Class<?>) i.next();
            // 1. get declared and inherited methods from direct parents
            List<InheritedMethod> myMethods = inheritedAndDeclaredMethods.get(current);
            for (Class<?> baseInterface : current.getInterfaces()){
                myMethods.addAll(inheritedAndDeclaredMethods.get(baseInterface));
            }
            // 2. add current class methods, override existing (remove them from my methods)
            // 2.1 remove inherited methods which I override
            myMethods.removeIf(aMethod -> {
                        try {
                            current.getMethod(aMethod.method.getName(), aMethod.method.getParameterTypes());
                            return false;
                        } catch (NoSuchMethodException e) {
                            return true;
                        }
                    }
            );
            // 2.2 add all methods overriden by me
            myMethods.addAll(Arrays.stream(current.getDeclaredMethods())
                    .map(aMethod -> new InheritedMethod(aMethod, current))
                    .collect(Collectors.toList()));
            // 3. check for ambiguities
            for (InheritedMethod m : myMethods) {
                final String mname = m.method.getName();
                final Class<?>[] args = m.method.getParameterTypes();
                long occurences = myMethods.stream()
                        .filter(inMethod -> {
                                    return inMethod.method.getName().equals(mname) &&
                                            Arrays.equals(inMethod.method.getParameterTypes(), args);
                                }
                        ).count();
                if (occurences>1) {
                    throw new OOPInherentAmbiguity(this.interfaceClass, m.definingClass, m.method);
                }
            }
        }
    }

    private class InheritedMethod {
        public Method method;
        public Class<?> definingClass;

        public InheritedMethod(Method method, Class<?> definingClass){
            this.method = method;
            this.definingClass = definingClass;
        }

        @Override
        public int hashCode(){
            return method.getName().hashCode() ^ definingClass.hashCode();
        }
    }

    /**
     *
     * @param start - start node.
     * @param callbackFunction - function to invoke for each node (look BFSCallBack).
     * @param callbackArgument - additional argument to pass to callbackFunction.
     * @throws OOPMultipleException
     */
    private static void doBFS(Class<?> start, BFSCallBack callbackFunction, Object callbackArgument)
            throws OOPMultipleException {
        Queue<Class<?>> queue = new ArrayDeque<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            Class<?> classNode = queue.remove();
            callbackFunction.invoke(classNode, callbackArgument);
            queue.addAll(Arrays.asList(classNode.getInterfaces()));
        }
    }

    @FunctionalInterface
    private interface BFSCallBack {
        void invoke(Class<?> node, Object callbackArgument) throws OOPMultipleException;
    }

    private static Deque<Class<?>> topologicalSort(Class<?> start) {
        Map<Class<?>, Integer> graph = getGraphWithNumberOfImplementors(start);
        Deque<Class<?>> sorted = new ArrayDeque<>();
        Deque<Class<?>> sources = new ArrayDeque<>();

        sources.add(start);

        while (!sources.isEmpty()){
            Class<?> current = sources.removeFirst();
            sorted.addLast(current);
            for (Class<?> baseInterface : current.getInterfaces()){
                Integer count = graph.get(baseInterface);
                graph.put(baseInterface, count - 1);
                if (graph.get(baseInterface) == 0)
                    sources.add(baseInterface);
            }
        }
        return sorted;
    }

    /**
     *
     * @return Map (Interface)->(number of interfaces which extend it)
     */
    private static Map<Class<?>, Integer> getGraphWithNumberOfImplementors(Class<?> start){
        Map<Class<?>, Integer> result = new HashMap<>();
        try {
            doBFS(start, (aClass, accumulator)->{
                HashMap<Class<?>, Integer> map = (HashMap<Class<?>, Integer>) accumulator;
                Integer count = map.getOrDefault(aClass, 0);
                map.put(aClass, count + 1);
            }, result);
        } catch (OOPMultipleException e){
            e.printStackTrace();
            assert false;
        }
        return result;
    }

}

