package OOP.Solution;

import OOP.Provided.*;
import OOP.Provided.OOPInaccessibleMethod.ForbiddenAccess;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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

    /**
     * General idea: we want to simulate the way methods are inherited and overriden in C++.
     * We traverse inheritance graph from most basic interfaces to most derived (reverse topological order), and
     * for each class we keep track of methods it knows and which interface first declared that method (look
     * InheritedMethod class).
     * @throws OOPInherentAmbiguity
     */
    private void checkGraphInherentAmbiguities() throws OOPInherentAmbiguity {

        Deque<Class<?>> topoSort = topologicalSort(this.interfaceClass);

        /* For each interface in inheritance graph we keep list of methods it knows (either inherited or declared).
        *
        *  Note: values in this map are valid only for classes we already processed
        *  (i.e. from current to the end of topological sort)
        */
        Map<Class<?>, List<InheritedMethod>> inheritedAndDeclaredMethods = topoSort.stream()
                .collect(Collectors.toMap(Function.identity(), unused -> new LinkedList<>()));

        Iterator i = topoSort.descendingIterator();
        while (i.hasNext()){
            Class<?> currentInterface = (Class<?>) i.next();
            List<InheritedMethod> myMethods = getMethodsFromBaseInterfaces(currentInterface, inheritedAndDeclaredMethods);
            applyMethodsOverride(currentInterface, myMethods);
            checkForAmbiguities(myMethods);
        }
    }

    private static List<InheritedMethod> getMethodsFromBaseInterfaces(Class<?> current, Map<Class<?>, List<InheritedMethod>> inheritedAndDeclaredMethods) {
        List<InheritedMethod> myMethods = inheritedAndDeclaredMethods.get(current);
        for (Class<?> baseInterface : current.getInterfaces()) {
            myMethods.addAll(inheritedAndDeclaredMethods.get(baseInterface));
        }
        return myMethods;
    }

    private void applyMethodsOverride(Class<?> currentInterface, List<InheritedMethod> inheritedMethods) {
        // 1. remove inherited methods which currentInterface overrides
        inheritedMethods.removeIf(aMethod -> {
                    try {
                        currentInterface.getMethod(aMethod.method.getName(), aMethod.method.getParameterTypes());
                        return false;
                    } catch (NoSuchMethodException e) {
                        return true;
                    }
                });
        // 2. add all methods overriden by me
        inheritedMethods.addAll(Arrays.stream(currentInterface.getDeclaredMethods())
                .map(aMethod -> new InheritedMethod(aMethod, currentInterface))
                .collect(Collectors.toList()));
    }

    /**
     * We simply look for duplicates in list of all methods an interface knows.
     * Note: java's Method.equals() can't do this, since methods declared in different classes aren't equal.
     *  Instead we compare only declarations.
     * @param inheritedAndDeclaredMethods - all methods which an interface knows, after applying override.
     * @throws OOPInherentAmbiguity
     */
    private void checkForAmbiguities(List<InheritedMethod> inheritedAndDeclaredMethods) throws OOPInherentAmbiguity {
        for (InheritedMethod m : inheritedAndDeclaredMethods) {
            final String mname = m.method.getName();
            final Class<?>[] args = m.method.getParameterTypes();
            long occurrences = inheritedAndDeclaredMethods.stream().filter(inMethod ->
                    inMethod.method.getName().equals(mname) && Arrays.equals(inMethod.method.getParameterTypes(), args))
                    .count();
            if (occurrences > 1) {
                throw new OOPInherentAmbiguity(this.interfaceClass, m.declaringClass, m.method);
            }
        }
    }

    /**
     * Helper class for inherent ambiguities check.
     * That's how we keep track which interface declares each method.
     */
    private class InheritedMethod {
        public Method method;
        public Class<?> declaringClass;

        public InheritedMethod(Method method, Class<?> declaringClass){
            this.method = method;
            this.declaringClass = declaringClass;
        }
    }

    /**
     * BFS traversal of inheritance graph.
     * @param start - start node.
     * @param callbackFunction - function to invoke for each node (look BFSCallBack).
     * @param callbackArgument - additional argument to pass to callbackFunction.
     *                         Type is Object for the sake of genericity.
     *                         It's callbackFunction responsibility to cast callbackArgument to an appropriate type.
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
        Map<Class<?>, Integer> graph = countInterfaceImplementors(start);
        Deque<Class<?>> sorted = new ArrayDeque<>();
        Deque<Class<?>> sources = new ArrayDeque<>();

        sources.add(start);

        while (!sources.isEmpty()) {
            Class<?> current = sources.removeFirst();
            sorted.addLast(current);
            for (Class<?> baseInterface : current.getInterfaces()) {
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
    private static Map<Class<?>, Integer> countInterfaceImplementors(Class<?> start) {
        Map<Class<?>, Integer> result = new HashMap<>();
        try {
            doBFS(start, (aClass, accumulator) -> {
                HashMap<Class<?>, Integer> map = (HashMap<Class<?>, Integer>) accumulator;
                Integer count = map.getOrDefault(aClass, 0);
                map.put(aClass, count + 1);
            }, result);
        } catch (OOPMultipleException e) {
            e.printStackTrace();
            assert false;
        }
        return result;
    }

    /* ------------- Methods for Part 3 ------------ */
    /**
     * Returns a list of all possible matches to the method with name=methodName
     * and possible-matching argument types.
     * @param interfaceClass - The interface to start searching from.
     * @param methodName     - The name of the method to search for.
     * @param methodArgs     - The arguments given to the invoke method originally.
     */
    private static Set<Method> possibleMethodMatches(Class<?> interfaceClass, String methodName, Object[] methodArgs) {
        Queue<Class<?>> qu_BFS = new ArrayDeque<Class<?>>();
        Map<Method, Integer> levelMap = new HashMap<>();
        assert (interfaceClass.getGenericInterfaces().length())
        qu_BFS.add(interfaceClass.getGenericInterfaces()[0]);
        Integer bfs_level = 0;
        while(!qu_BFS.isEmpty()) {
            bfs_level += 1;
            Class<?> currVertex = qu_BFS.removeFirst();
            Class<?>[] singleLevelInterfaces = currVertex.getInterfaces();
            for(Class<?> currInterface : singleLevelInterfaces) {
                Method found = matchingMethodExists(currInterface, methodName, args);
                if(found == null)
                    continue;
                // If method is private, it means that it hides any
                // inherited method with the same name. In that case,
                // we won't add any inherited method unless they came
                // in from a different path.
                if((found.getModifiers() == Modifier.PRIVATE)
                || (getMethodModifier(found) == OOPMethodModifier.PRIVATE))
                    continue;
                if(verifyArgumentTypes(methodArgs, found.getParameterTypes()))
                    addMethodIfNeeded(levelMap, found, bfs_level);
                qu_BFS.add(currInterface); //Adding again to the BFS.
            }
        }
        return levelMap.keySet();
    }

    /** 
     * Checks if every object in input matches its expected type.
     * @param interfaceClass - The interface to search for the methodName inside. 
     * @param methodName     - The name of the method to search for.
     * @param numArgs        - The number of arguments the method should have.
     */
    private static Method matchingMethodExists(Class<?> interfaceClass, String methodName, Integer numArgs) {
        Method[] interfaceMethods = interfaceClass.getDeclaredMethods();
        for(Method m : interfaceMethods) {
            if(m.getName().equals(methodName) 
                && (m.getParameterTypes().length() == numArgs))
                return m;
        }
        return null;
    }

    /** 
     * Checks if every object in input matches its expected type.
     * @param inputVars - The input parameters to verify.
     * @param expectedTypes - The types to compare the parameters against.
     *                        Types that inherit the given one are valid.
     */
    private static boolean verifyArgumentTypes(Object[] inputVars, Class<?>[] expectedTypes) {
        if(inputVars.length() != expectedTypes.length())
            return false;
        for(int i = 0; i < inputVars.length(); i++) {
            if(!(inputVars[i] instanceof expectedTypes[i]))
                return false;
        }

        return true;
    }

    /** 
     * Checks if we need to add the method to the map of possible matchs,
     * or replace a current possible match.
     * @param matchingFound - The map of current possible matchs found.
     * @param possibleAdd   - The method that might be a match.
     * @param level         - The level of the method in the BFS traversal.
     */
    private static void addMethodIfNeeded(Map<Method, Integer> matchingFound, Method possibleAdd, Integer level) {
        /*  
            We want to start doing a bit of replacing possible existing matches
            in case a better one comes along. Later we will check even more for
            any ambiguities. Here we don't remove any ambiguities because if we
            replaced a method, it would have been hidden by the implementation
            of the replacing method. 
        */
        boolean need_add = true;
        for(Map.Entry<Method, Integer> entry : matchingFound.entrySet()) {
            Class<?> exist_declareClass = entry.getKey().getDeclaringClass();
            Class<?> newer_declareClass = possibleMatch.getDeclaringClass();
            if(newer_declareClass.isAssignableFrom(exist_declareClass)) {
                matchingFound.remove(entry.getKey());
                matchingFound.put(possibleMatch, level);
                need_add = false;
                break;
            }
        }
        if(need_add == true) {
            matchingFound.put(possibleMatch, level);
        }
    }

    private static OOPMethodModifier getMethodModifier(Method oopMethod) {
        OOPMethodModifier modifierAnnotation 
            = oopMethod.getAnnotation(OOPMultipleMethod.class);
        return modifierAnnotation;
    }
}
