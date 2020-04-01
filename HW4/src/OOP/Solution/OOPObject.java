package OOP.Solution;

import OOP.Provided.OOP4AmbiguousMethodException;
import OOP.Provided.OOP4MethodInvocationFailedException;
import OOP.Provided.OOP4NoSuchMethodException;
import OOP.Provided.OOP4ObjectInstantiationFailedException;
import com.sun.tools.javac.util.Pair;


import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class OOPObject {
    private ArrayList<Object> directParents;
    private Map<String, Object> virtualAncestors;
    private static Map<String, Object> allVirtualAncestors = new HashMap<>();
    private static int counter = 0;

    private Object createInstance(Class<?> cls, Boolean isVirtual) throws OOP4ObjectInstantiationFailedException {
        try {
            Constructor<?> c = cls.getDeclaredConstructor(null);
            if (isVirtual) {
                String parent_name = (cls.getName());
                if (!allVirtualAncestors.containsKey(parent_name))
                    allVirtualAncestors.put(parent_name, c.newInstance(null));
                return allVirtualAncestors.get(parent_name);
            } else {
                return c.newInstance(null);
            }
        } catch (Exception e) {
            throw new OOP4ObjectInstantiationFailedException();
        }
    }

    private static void addToInitArray(OOPParent p, ArrayList<Pair<Class<?>, Boolean>> initArray) {
        Pair<Class<?>, Boolean> pair = new Pair<Class<?>, Boolean>(p.parent(), p.isVirtual());
        if (p.isVirtual()) { // if virtual than add if wasn't there before
            if (!initArray.contains(pair)) {
                initArray.add(pair);
            }
        } else { // not virtual ? than add anyway.
            initArray.add(pair);
        }
    }

    public static void getInitArray(Class<?> thisClass, ArrayList<Pair<Class<?>, Boolean>> initArray) {
        Annotation[] annotations = thisClass.getDeclaredAnnotations();
        if (annotations.length == 1) {
            if (annotations[0].annotationType() == OOPParent.class) {
                OOPParent p = (OOPParent) annotations[0];
                if (OOPObject.class.isAssignableFrom(p.parent())) {
                    getInitArray(p.parent(), initArray);
                    addToInitArray(p, initArray);
                } else { // not an OOPObject class ? do the same check
                    addToInitArray(p, initArray);
                }
            } else if (annotations[0].annotationType() == OOPParents.class) {
                OOPParent[] parents = ((OOPParents) annotations[0]).value();
                for (OOPParent p : parents) {
                    if (OOPObject.class.isAssignableFrom(p.parent())) {
                        getInitArray(p.parent(), initArray);
                        addToInitArray(p, initArray);
                    } else { // not an OOPObject class ? do the same check
                        addToInitArray(p, initArray);
                    }
                }
            }
        }
    }


    private static boolean checkIfInInitArray(ArrayList<Pair<Object, Boolean>> alreadyInit, Class<?> cls) {
        return alreadyInit.stream().map(obj -> obj.fst.getClass().getName()).anyMatch(name -> name.equals(cls.getName()));
    }

    private static Object getObjFromArray(ArrayList<Pair<Object, Boolean>> alreadyInit, Pair<Class<?>, Boolean> pair) {
        for (Pair<Object, Boolean> pairParent : alreadyInit) {
            if (pairParent.fst.getClass().getName().equals(pair.fst.getName()) && pairParent.snd.equals(pair.snd)) {
                return pairParent.fst;
            }
        }
        return null;
    }

    public OOPObject() throws OOP4ObjectInstantiationFailedException {
        counter ++;
        ArrayList<Pair<Object, Boolean>> alreadyInit = new ArrayList<>();
        ArrayList<Pair<Class<?>, Boolean>> initArray = new ArrayList<>();

        getInitArray(this.getClass(), initArray);

        virtualAncestors = new HashMap<>();
        for (Pair<Class<?>, Boolean> parentPair : initArray) { // create instance of all virtual parents
            if (checkIfInInitArray(alreadyInit, parentPair.fst)) {
                continue;
            }
            if (parentPair.snd) { // means parent is virtual
                Object virtual_obj = createInstance(parentPair.fst, parentPair.snd);
                virtualAncestors.put(virtual_obj.getClass().getName(), virtual_obj);
            }
        }

        // fill all direct parents
        this.directParents = new ArrayList<>();
        Annotation[] annotations = this.getClass().getDeclaredAnnotations();
        if (annotations.length == 1) {
            if (annotations[0].annotationType() == OOPParent.class) {
                OOPParent p = (OOPParent) annotations[0];
                directParents.add(createInstance(p.parent(), p.isVirtual()));
            } else if (annotations[0].annotationType() == OOPParents.class) {
                OOPParent[] parents = ((OOPParents) annotations[0]).value();
                for (OOPParent p : parents) {
                    directParents.add(createInstance(p.parent(), p.isVirtual()));
                }
            }
        }

        counter--; // good 'ol mavo to madmah trick.
        if (counter == 0) {
            allVirtualAncestors = new HashMap<>();
        }

    }

    public boolean multInheritsFrom(Class<?> cls) {
        if (cls.equals(this.getClass())) return true;

        if (directParents.isEmpty()) {
            if (cls.isAssignableFrom(this.getClass())) return true;
            else return false;
        } else {
            if (cls.isAssignableFrom(this.getClass())) return false;
            if (directParents.stream().anyMatch(p -> cls.equals(p.getClass()))) return true;
        }

        for (Object obj : directParents) {
            if (OOPObject.class.isAssignableFrom(obj.getClass())) {
                OOPObject oop_obj = (OOPObject) obj;
                if (oop_obj.multInheritsFrom(cls)) return true;
            } else {
                if (cls.isAssignableFrom(obj.getClass())) return true;
            }
        }

        return false;
    }

    // checks if we understand the method locally (and with normal heritage).
    public static Object getDefiningMethod(Object o, String methodName, Class<?>... argTypes) {
        if (o.getClass() == Object.class) return null;

        for (Method m : o.getClass().getMethods()) {
            if (m.getName().equals(methodName)) {
                if (Modifier.isPublic(m.getModifiers())) {
                    if (Arrays.equals(argTypes, m.getParameterTypes())) {
                        return o;
                    }
                }
            }
        }

        return null;
    }

    private int getObjectsThatDefineMethod(ArrayList<Object> arr, String methodName, Class<?>... argTypes) {

        // first check if defined at me
        if (getDefiningMethod(this, methodName, argTypes) != null) {
            arr.add(this);
            return 1;
        }

        // check if more than one of the parents knows func
        int counter = 0;
        for (Object obj : directParents) {
            if (OOPObject.class.isAssignableFrom(obj.getClass())) {
                OOPObject oop_obj = (OOPObject) obj;
                counter += oop_obj.getObjectsThatDefineMethod(arr, methodName, argTypes);
            } else {
                Object o = getDefiningMethod(obj, methodName, argTypes);
                if (o != null) {
                    arr.add(o);
                    counter++;
                }
            }
        }

        return counter;
    }

    // TODO: think of tree as a graph and search for all roads, if road passed through virtual than it's ok.

    public Object findMethod(String methodName, Class<?>... argTypes) {
        Object o = getDefiningMethod(this, methodName, argTypes);
        if (o != null) return o;

        for (Object obj : directParents) {
            if (OOPObject.class.isAssignableFrom(obj.getClass())) {
                OOPObject oop_obj = (OOPObject) obj;
                o = oop_obj.findMethod(methodName, argTypes);
            } else {
                o = getDefiningMethod(obj, methodName, argTypes);
            }

            if (o != null) return o;
        }

        return null;
    }

    private static int removeAndCountOccurences(ArrayList<Object> arr, Object o) {
        int counter = 0;
        for (Object obj : arr) {
            if (obj == o) counter++;
        }
        for (int i = 0; i < counter; i++) {
            arr.remove(o);
        }
        return counter;
    }


    public void findAllPaths(List<Stack<Object>> paths, Stack<Object> path, Object target) {
        if (this.getClass().equals(target.getClass())) {
            Stack temp = new Stack();
            temp.add(this);
            paths.add(temp);
        }

        for (Object next_obj : directParents) {
            if (next_obj.equals(target)) {
                Stack temp = new Stack();
                temp.addAll(path);
                temp.push(next_obj);
                paths.add(temp);
            } else if (!path.contains(next_obj)) {
                if (OOPObject.class.isAssignableFrom(next_obj.getClass())) {
                    OOPObject oop_obj = (OOPObject) next_obj;
                    path.push(next_obj);
                    oop_obj.findAllPaths(paths, path, target);
                    path.pop();
                } // else is not relevant because there is no need to go up the normal
                // hierarchy tree, method has to be at the bottom
            }
        }
    }

    private int countVirtualPaths(List<Stack<Object>> l) {
        int counter = 0;

        for (Stack<Object> s : l) {
            for (Object obj : s) {
                if (virtualAncestors.containsKey(obj.getClass().getName())) {
                    counter++;
                    break;
                }
            }
        }
        return counter;
    }


    public Object definingObject(String methodName, Class<?>... argTypes) throws OOP4AmbiguousMethodException, OOP4NoSuchMethodException {

        ArrayList<Object> ambiguousObj = new ArrayList<>();
        int counter = this.getObjectsThatDefineMethod(ambiguousObj, methodName, argTypes);
        if (counter == 0) throw new OOP4NoSuchMethodException();

        // filtering duplicated objects
        List<Object> filteredObjects = ambiguousObj.stream().distinct().collect(Collectors.toList());
        if (filteredObjects.size() > 1) throw new OOP4AmbiguousMethodException(); // two different obj define method.

        // finding all paths to the object that defined the method, only 1 object.
        Stack path = new Stack();
        path.push(this);
        List<Stack<Object>> paths = new ArrayList<>();
        this.findAllPaths(paths, path, filteredObjects.get(0));
        int num_of_paths = paths.size();
        int num_of_virtual_paths = countVirtualPaths(paths);

        if ((num_of_paths - num_of_virtual_paths > 0) && (num_of_paths != 1)) throw new OOP4AmbiguousMethodException();

        // either 1 path or many path but all virtual.
        try {
            Object obj = this.findMethod(methodName, argTypes);
            if (virtualAncestors.containsKey(obj.getClass().getName())) {
                return virtualAncestors.get(obj.getClass().getName());
            }
            return obj; // not a virtual ancestor.
        } catch (Exception e) {
            assert false;
            return null;
        }

    }

    public Object invoke(String methodName, Object... callArgs) throws OOP4AmbiguousMethodException, OOP4NoSuchMethodException, OOP4MethodInvocationFailedException {
        Class<?>[] parameters_types = Arrays.stream(callArgs).map(Object::getClass).toArray(Class[]::new);
        Object obj = this.definingObject(methodName, parameters_types);
        try {
            Method method = obj.getClass().getMethod(methodName, parameters_types);
            return method.invoke(obj, callArgs);
        } catch (Exception e) {
            throw new OOP4MethodInvocationFailedException();
        }
    }

}


