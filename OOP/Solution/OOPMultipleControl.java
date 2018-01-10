package OOP.Solution;

import OOP.Provided.*;
import OOP.Provided.OOPInaccessibleMethod.ForbiddenAccess;
import javafx.util.Pair;
import java.io.File;
import java.lang.Class;
import java.lang.reflect.InvocationTargetException;
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

    public Object invoke(String methodName, Object[] args)
            throws OOPMultipleException {
        Method best_match_proto = this.checkCoincidentalAmbiguity(methodName, args);
        //We have the method from its interface, we need to get the actual method
        //from the class InterfaceImpl

        Method best_match;
        Class<?> actual = this.getInterfaceActualClass(best_match_proto.getDeclaringClass().getName());
        try {
            Class<?>[] params = (best_match_proto.getParameterTypes().length == 0) ? null : best_match_proto.getParameterTypes();
            //If proto is default, then we already have impl for it.
            best_match = (best_match_proto.isDefault()) ?
                    best_match_proto
                    : actual.getDeclaredMethod(best_match_proto.getName(), params);
        } catch (NoSuchMethodException e) {
            return null;
        }
        Object output;

        try {
            output = best_match.invoke(actual.newInstance(), args);
            return output;
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            throw new OOPBadClass(best_match);
        } catch (InstantiationException e) {
            return null;
        }
    }

    private Class<?> getInterfaceActualClass(String interClass_name) {
        String method_inter = interClass_name;
        String str_start = method_inter.substring(0, method_inter.lastIndexOf('.')+1);
        String str_end = method_inter.substring(method_inter.lastIndexOf('.')+2);
        String  actual_class_str = str_start + "C" + str_end;
        Class actual;
        try {
            actual = Class.forName(actual_class_str);
            return actual;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public void removeSourceFile() {
        if (sourceFile.exists()) {
            sourceFile.delete();
        }
    }

    private void checkGraphAnnotations() throws OOPBadClass {
        try {
            doBFS(this.interfaceClass, (aClass, unused) -> checkAnnotationsOfInterface(aClass), null);
        } catch (OOPBadClass e) {
            throw e;
        } catch (OOPMultipleException e) {
            // We don't expect exceptions other than OOPBadClass
            e.printStackTrace();
            assert false;
        }
    }

    private void checkAnnotationsOfInterface(Class<?> interfaceToCheck) throws OOPBadClass {
        assert interfaceToCheck.isInterface(); // Sanity check. We don't expect non-interface classes

        // No special requirements from 'root' class
        if (interfaceToCheck == this.interfaceClass)
            return;

        // 1. interfaceToCheck has OOPMultipleInterface annotation
        // Root of inheritance graph doesn't need to be annotated, so we don't check it.
        if (!interfaceToCheck.isAnnotationPresent(OOPMultipleInterface.class))
            throw new OOPBadClass(interfaceToCheck);

        // 2. each method of interfaceToCheck has OOPMultipleMethod annotation
        for (Method method : interfaceToCheck.getDeclaredMethods()) {
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

        } catch (OOPInaccessibleMethod e) {
            throw e;
        } catch (OOPMultipleException e) {
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
                Method calledMethod = callee.getMethod(innerCallAnnotation.methodName(), innerCallAnnotation.argTypes());
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
     * General idea: we want to simulate the way methods are inherited and overridden in C++.
     * We traverse inheritance graph from most basic interfaces to most derived (reverse topological order), and
     * for each class we keep track of methods it knows.
     *
     * @throws OOPInherentAmbiguity
     */
    private void checkGraphInherentAmbiguities() throws OOPInherentAmbiguity {
        /* If there is no common base class with declared methods - only coincidental ambiguities can exist.
        *  In this case we can skip the following check. */
        if (!existsCommonBaseWithDeclaredMethods())
            return;

        Deque<Class<?>> topoSort = topologicalSort(this.interfaceClass);

        /* For each interface in inheritance graph we keep list of methods it knows (either inherited or declared).
        *  Note: values in this map are valid only for classes we already processed
        *  (i.e. from current to the end of topological sort)
        */
        Map<Class<?>, List<Method>> inheritedAndDeclaredMethods = topoSort.stream()
                .collect(Collectors.toMap(Function.identity(), unused -> new LinkedList<>()));

        Iterator i = topoSort.descendingIterator();
        while (i.hasNext()) {
            Class<?> currentInterface = (Class<?>) i.next();
            List<Method> myMethods = getMethodsFromBaseInterfaces(currentInterface, inheritedAndDeclaredMethods);
            applyMethodsOverride(currentInterface, myMethods);
            checkForAmbiguities(myMethods);
        }
    }

    private boolean existsCommonBaseWithDeclaredMethods() {
        Set<Class<?>> commonBases = new HashSet<>();
        countBaseUsages(this.interfaceClass).forEach((baseInterface, usagesCount) -> {
            if(usagesCount > 1)
                commonBases.add(baseInterface);
        });
        return commonBases.stream().anyMatch(baseInterface -> baseInterface.getDeclaredMethods().length > 0);
    }

    private static List<Method> getMethodsFromBaseInterfaces(Class<?> current, Map<Class<?>, List<Method>> inheritedAndDeclaredMethods) {
        List<Method> myMethods = inheritedAndDeclaredMethods.get(current);
        for (Class<?> baseInterface : current.getInterfaces()) {
            myMethods.addAll(inheritedAndDeclaredMethods.get(baseInterface));
        }
        return myMethods;
    }

    private void applyMethodsOverride(Class<?> currentInterface, List<Method> inheritedMethods) {
        // 1. remove inherited methods which currentInterface overrides
        inheritedMethods.removeIf(aMethod -> {
            try {
                currentInterface.getDeclaredMethod(aMethod.getName(), aMethod.getParameterTypes());
                return true;
            } catch (NoSuchMethodException e) {
                return false;
            }
        });
        // 2. add all methods overriden by me
        inheritedMethods.addAll(Arrays.stream(currentInterface.getDeclaredMethods())
                .collect(Collectors.toList()));
    }

    /**
     * We simply look for duplicates in list of all methods an interface knows.
     * Note: java's Method.equals() can't do this, since methods declared in different classes aren't equal.
     * Instead we compare only signatures.
     *
     * @param inheritedAndDeclaredMethods - all methods which an interface knows, after applying override.
     * @throws OOPInherentAmbiguity
     */
    private void checkForAmbiguities(List<Method> inheritedAndDeclaredMethods) throws OOPInherentAmbiguity {
        for (Method m : inheritedAndDeclaredMethods) {
            final String name = m.getName();
            final Class<?>[] args = m.getParameterTypes();
            long occurrences = inheritedAndDeclaredMethods.stream().filter(inMethod ->
                    inMethod.getName().equals(name) && Arrays.equals(inMethod.getParameterTypes(), args))
                    .count();
            if (occurrences > 1) {
                throw new OOPInherentAmbiguity(this.interfaceClass, m.getDeclaringClass(), m);
            }
        }
    }

    /**
     * BFS traversal of inheritance graph.
     *
     * @param start            - start node.
     * @param callbackFunction - function to invoke for each node (look BFSCallBack).
     * @param callbackArgument - additional argument to pass to callbackFunction.
     *                         Type is Object for the sake of genericity.
     *                         It's callbackFunction responsibility to cast callbackArgument to an appropriate type.
     * @throws OOPMultipleException
     */
    private static void doBFS(Class<?> start, BFSCallBack callbackFunction, Object callbackArgument)
            throws OOPMultipleException {
        Queue<Class<?>> queue = new ArrayDeque<>();
        Set<Class<?>> visited = new HashSet<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Class<?> classNode = queue.remove();
            callbackFunction.invoke(classNode, callbackArgument);
            for (Class<?> baseInterface : classNode.getInterfaces()) {
                if (!visited.contains(baseInterface)) {
                    visited.add(baseInterface);
                    queue.add(baseInterface);
                }
            }
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
     * @return Map (Interface)->(number of interfaces which extend it)
     */
    private static Map<Class<?>, Integer> countInterfaceImplementors(Class<?> start) {
        Map<Class<?>, Integer> result = new HashMap<>();
        result.put(start, 0);
        try {
            doBFS(start, (aClass, accumulator) -> {
                HashMap<Class<?>, Integer> implementorsCount = (HashMap<Class<?>, Integer>) accumulator;
                for (Class<?> baseInterface : aClass.getInterfaces()) {
                    Integer count = implementorsCount.getOrDefault(baseInterface, 0);
                    implementorsCount.put(baseInterface, count + 1);
                }
            }, result);
        } catch (OOPMultipleException e) {
            e.printStackTrace();
            assert false;
        }
        return result;
    }
    /**
     * @return Map (Interface)->(number of interfaces which extend it)
     */
    private static Map<Class<?>, Integer> countBaseUsages(Class<?> start) {
        Map<Class<?>, Integer> result = new HashMap<>();
        result.put(start, 1);
        try {
            doBFS(start, (aClass, accumulator) -> {
                HashMap<Class<?>, Integer> implementorsCount = (HashMap<Class<?>, Integer>) accumulator;
                for (Class<?> baseInterface : aClass.getInterfaces()) {
                    Integer count = implementorsCount.getOrDefault(baseInterface, 0);
                    implementorsCount.put(baseInterface, count + implementorsCount.get(aClass));
                }
            }, result);
        } catch (OOPMultipleException e) {
            e.printStackTrace();
            assert false;
        }
        return result;
    }

    /* ------------- Methods for Part 3 ------------ */
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    private Method checkCoincidentalAmbiguity(String invokedName, Object[] invokedArgs)
            throws OOPInaccessibleMethod, OOPCoincidentalAmbiguity {
        Set<Method> possibleMatches
                = possibleMethodMatches(this.interfaceClass, invokedName, invokedArgs);
        Integer minimalDist = getMinimalParamDist(possibleMatches, invokedArgs);
        Map<Method, Integer> matchsWithDist = getMethodDistMap(invokedArgs, possibleMatches);
        Map<Method, Integer> minimalDistMethods = matchsWithDist.entrySet().stream()
                .filter(aPair -> (aPair.getValue() <= minimalDist))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        if(minimalDistMethods.size() == 1)
            return minimalDistMethods.keySet().iterator().next();
        else if(minimalDistMethods.size() == 0)
            return null;
        // Else, we need to check if there is ambiguity.
        for(Map.Entry<Method, Integer> m1 : minimalDistMethods.entrySet()) {
            for(Map.Entry<Method, Integer> m2 : minimalDistMethods.entrySet()) {
                if(m1.getKey().equals(m2.getKey())) //Method always equal to itself.
                    continue;
                if(getMethodModifier(m1.getKey()) == OOPMethodModifier.PRIVATE ||
                        getMethodModifier(m2.getKey()) == OOPMethodModifier.PRIVATE)
                    continue;
                if(checkSameParameterTypes(m1.getKey().getParameterTypes(),(m2.getKey().getParameterTypes()))) {
                    List<Pair<Class<?>, Method>> ambiguities = minimalDistMethods.entrySet().stream()
                            .collect(Collectors.toMap(aPair -> aPair.getKey().getDeclaringClass(), aPair -> aPair.getKey()))
                            .entrySet().stream()
                            .map(anEntry -> new Pair<Class<?>, Method>(anEntry.getKey(), anEntry.getValue()))
                            .collect(Collectors.toList());
                    throw new OOPCoincidentalAmbiguity(ambiguities);
                }
            }
        }

        //If no ambiguity, we find the best method (closest in bfs) and invoke it.
        Method best_match = null;
        Integer minimal_bfsVal = Integer.MAX_VALUE; //Set an upper bound.
        for(Map.Entry<Method, Integer> m : minimalDistMethods.entrySet()) {
            if(getMethodModifier(m.getKey()) == OOPMethodModifier.PRIVATE)
                continue;
            if(m.getValue() < minimal_bfsVal) {
                minimal_bfsVal = m.getValue();
                best_match = m.getKey();
            }
        }
        if(best_match == null && minimalDistMethods.size() > 0) { //If there are methods, but private
            List<ForbiddenAccess> inaccessible = minimalDistMethods.entrySet().stream()
                    .map(aPair -> new ForbiddenAccess(this.getInterfaceActualClass(this.interfaceClass.getName()), aPair.getKey().getDeclaringClass(), aPair.getKey()))
                    .collect(Collectors.toList());
            throw new OOPInaccessibleMethod(inaccessible);
        }
        return best_match;
    }

    private Integer getMinimalParamDist(Set<Method> possibleMatches, Object[] argArray) {
        Integer minimalDist = Integer.MAX_VALUE;
        Class<?>[] match_types;
        Integer currentDist;
        for(Method match : possibleMatches) {
            match_types = match.getParameterTypes();
            currentDist = getParametersDistance(argArray, match_types);
            if(currentDist == null)
                continue; //Should never get here.
            if(currentDist <= minimalDist && getMethodModifier(match) != OOPMethodModifier.PRIVATE)
                minimalDist = currentDist;
        }
        return minimalDist;
    }

    private Map<Method, Integer> getMethodDistMap(Object[] argArray, Set<Method> possibleMatches) {
        Map<Method, Integer> resultsMap = new HashMap<>();
        for(Method m : possibleMatches) {
            Class<?>[] method_types = m.getParameterTypes();
            resultsMap.put(m, getParametersDistance(argArray, method_types));
        }
        return resultsMap;
    }

    /**
     * Returns a list of all possible matches to the method with name=methodName
     * and possible-matching argument types.
     * @param interfaceClass - The interface to start searching from.
     * @param methodName     - The name of the method to search for.
     * @param methodArgs     - The arguments given to the invoke method originally.
     */
    private static Set<Method> possibleMethodMatches(Class<?> interfaceClass, String methodName, Object[] methodArgs) {
        Queue<Class<?>> qu_BFS = new ArrayDeque<>();
        Map<Method, Integer> levelMap = new HashMap<>();
        assert (interfaceClass.getGenericInterfaces().length > 0);
        qu_BFS.add(interfaceClass);
        Integer bfs_level = 0;
        Integer num_params = (methodArgs == null) ? 0 : methodArgs.length;
        while(!qu_BFS.isEmpty()) {
            bfs_level += 1;
            Class<?> currVertex = qu_BFS.poll();
            Class<?>[] singleLevelInterfaces = currVertex.getInterfaces();
            if(singleLevelInterfaces == null)
                continue;
            for(Class<?> currInterface : singleLevelInterfaces) {
                Method found = matchingMethodExists(currInterface, methodName, num_params);
                if(found != null) {
                    if ((found.getModifiers() == Modifier.PRIVATE)
                            || (getMethodModifier(found) == OOPMethodModifier.PRIVATE)) {
                        addMethodIfNeeded(levelMap, found, bfs_level);
                        continue; //If private, means it hides methods above it,
                        //even if their parameters are different.
                    }
                    if (verifyArgumentTypes(methodArgs, found.getParameterTypes())) {
                        addMethodIfNeeded(levelMap, found, bfs_level);
                        continue; //If exists, means hiding parental methods.
                        //Even if parameters different, still want to hide.
                    }
                }
                qu_BFS.add(currInterface); //Adding again to the BFS.
            }
        }
        return levelMap.keySet();
    }

    /**
     * Checks if there exists a method with matching name and args count in
     * the speicified interfaceClass.
     * @param interfaceClass - The interface to search for the methodName inside.
     * @param methodName     - The name of the method to search for.
     * @param numArgs        - The number of arguments the method should have.
     */
    private static Method matchingMethodExists(Class<?> interfaceClass, String methodName, Integer numArgs) {
        assert(interfaceClass != null);
        Method[] interfaceMethods = interfaceClass.getDeclaredMethods();
        for(Method m : interfaceMethods) {
            if(!m.isAnnotationPresent(OOPMultipleMethod.class))
                continue;
            if(m.getName().equals(methodName)
                    && (m.getParameterTypes().length == numArgs)) {
                return m;
            }
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
        if(inputVars == null && expectedTypes == null)
            return true;
        if(inputVars == null && expectedTypes != null && expectedTypes.length == 0)
            return true;
        if((inputVars != null && expectedTypes == null)
                || (inputVars == null && expectedTypes != null))
            return false;
        //At this point, both are not null.
        if(inputVars.length != expectedTypes.length)
            return false;
        for(int i = 0; i < inputVars.length; i++) {
            if(!expectedTypes[i].isInstance(inputVars[i]))
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
            Class<?> newer_declareClass = possibleAdd.getDeclaringClass();
            if(newer_declareClass.isAssignableFrom(exist_declareClass)) {
                matchingFound.remove(entry.getKey());
                matchingFound.put(possibleAdd, level);
                need_add = false;
                break;
            }
        }
        if(need_add) {
            matchingFound.put(possibleAdd, level);
        }
    }

    /**
     * Returns the MethodModifier of the method specified
     * @param oopMethod - The method to get the modifier from.
     * @return Returns the OOPMethodModifier of the method.
     */
    private static OOPMethodModifier getMethodModifier(Method oopMethod) {
        OOPMultipleMethod modifierAnnotation
                = oopMethod.getAnnotation(OOPMultipleMethod.class);
        return modifierAnnotation.modifier();
    }

    /**
     * Gets the distance in parameters between the array of arguments,
     * and the specified array of types.
     * @param argArray - The array of arguments we hope to give a
     *                   method to invoke.
     * @param possibleTypes - The types to compare with the types of
     *                        the objects in argArray.
     * @return Returns the number of mismatched objects, or null if
     *         the sizes of the arrays are not equals.
     */
    private static Integer getParametersDistance(Object[] argArray, Class<?>[] possibleTypes) {
        if(!verifyArgumentTypes(argArray, possibleTypes)) {
            return null;
        }
        Integer num_params = (argArray == null) ? 0 : argArray.length;
        Integer mismatchCount = 0;

        for(int i = 0; i < num_params; i++) {
            if(argArray[i].getClass().equals(possibleTypes[i]))
                continue;
            mismatchCount += getClassesDist(argArray[i].getClass(), possibleTypes[i]);
        }
        return mismatchCount;
    }

    private static boolean checkSameParameterTypes(Class<?>[] arr1, Class<?>[] arr2) {
        Set<Class<?>> set1 = Arrays.stream(arr1).collect(Collectors.toSet());
        Set<Class<?>> set2 = Arrays.stream(arr2).collect(Collectors.toSet());
        return set1.equals(set2);
    }

    private static int getClassesDist(Class<?> c1, Class<?> c2) {
        if(c1.isAssignableFrom(c2) && c2.isAssignableFrom(c1))
            return 0;
        if(c2.isAssignableFrom(c1))
            return 1 + getClassesDist(c1.getSuperclass(), c2);
        if(c1.isAssignableFrom(c2))
            return 1 + getClassesDist(c1, c2.getSuperclass());
        else
            return -1;

    }
}
