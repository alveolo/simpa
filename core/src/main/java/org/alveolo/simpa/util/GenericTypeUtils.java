// TODO: Own implementation for our needs

/*
 * Copyright 2011 James Talmage
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.alveolo.simpa.util;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Utility class for introspecting Java Generics.</p>
 *
 * <p>Inspired in large part from Ian Robertsons article on
 * <a href="http://www.artima.com/weblogs/viewpost.jsp?thread=208860">reflecting generics</a>.</p>
 *
 * I've added a number features:
 * <ul>
 *   <li>baseClass can be an interface (his only walked up the superclass path).</li>
 *   <li>take into account owner type information (A&LT;B&GT;.C&LT;D&GT;).</li>
 *   <li>query only a single parameter (minor efficiency enhancement).</li>
 *   <li>ability to inspect nesting variables (A&LT;List&LT;B&GT;&GT;).</li>
 * </ul>
 *
 * <p>The end result looks significantly different, but it is based on Ian's algorithm.
 * In a <a href="http://www.artima.com/forums/flat.jsp?forum=106&thread=208860&start=15&msRange=15">blog post</a>
 * on Aug 21,2008 he says to "Consider it open sourced" (<i>it</i> being his orginal implementation).</p>
 *
 */
public class GenericTypeUtils {
    /**
     * Get the underlying class for a type, or null if the type is a variable type.
     * @param type the type
     * @return the underlying class
     */
    public static Class<?> getClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }

        if (type instanceof ParameterizedType) {
            return getClass(((ParameterizedType) type).getRawType());
        }

        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();

            Class<?> componentClass = getClass(componentType);
            if (componentClass != null) {
                return Array.newInstance(componentClass, 0).getClass();
            }

            return null;
        }

        return null;
    }

    /**
     * Gets a list of resolved type arguments for baseClass, based on values passed up the inheritance tree
     * from childClass.  Arguments are resolved to concrete classes.
     * Arguments that can't be resolved to concrete classes (i.e. TypeVariables) are returned as null.
     * If you are interested only in a single argument, it may be best to use getTypeArgument method.
     * @param baseClass the superclass of interface who's type arguments we're interested in querying.
     * @param childClass is the subclass or implementing class who's we're introspecting.
     * @return list of resolved type arguments as classes.
     */
    public static  Class<?>[] getTypeArguments(Class<?> baseClass, Type childClass) {
        final Type[] genericTypeArguments = getGenericTypeArguments(baseClass, childClass);

        Class<?>[] typesAsClasses = new Class<?>[genericTypeArguments.length];
        for(int i = 0; i < typesAsClasses.length;i++ ){
            typesAsClasses[i] = (getClass(genericTypeArguments[i]));
        }

        return typesAsClasses;
    }

    /**
     * Similar to getTypeArguments(baseClass, childClass), but returns only one argument based on index parameter.
     * Can be more efficient if you're only interested in one and only one argument out of a long list.
     * If you are interested in more than one argument, it's probably best to use getTypeArguments, and then query the list.
     * @param baseClass the superclass of interface who's type arguments we're interested in querying.
     * @param childClass is the subclass or implementing class who's we're introspecting.
     * @param index of type argument we want returned.
     * @return a single resolved type argument as a class.
     */
    public static Class<?> getTypeArgument(Class<?> baseClass, Type childClass, int index) {
        return getClass(getGenericTypeArgument(baseClass, childClass, index));
    }

    /**
     * Get the actual type arguments a child class has used to extend a generic base class.
     *
     * @param baseClass the base class
     * @param childClass the child class
     * @return a list of the raw classes for the actual type arguments.
     */
    public static Type[] getGenericTypeArguments(final Class<?> baseClass, final Type childClass) {
        Map<Type, Type> resolvedTypes = new HashMap<Type, Type>();

        Type type = climbToBasePopulatingMap(baseClass, childClass, resolvedTypes);

        // finally, for each actual type argument provided to baseClass, determine (if possible)
        // the raw class for that type argument.
        Type[] actualTypeArguments = getTypeParams(type);
        Type[] resolvedTypeArguments = new Type[actualTypeArguments.length];

        // resolve types by chasing down type variables.
        for (int i = 0; i < actualTypeArguments.length; i++) {
            resolvedTypeArguments[i] = resolveType(actualTypeArguments[i], resolvedTypes);
        }

        return resolvedTypeArguments;
    }

    /**
     * Gets a list of resolved type arguments for the baseClass.  Arguments are resolved based on childClass.
     * If you are interested only in a single argument, it may be best to use getTypeArgument method.
     * @param baseClass the superclass of interface who's type arguments we're interested in querying.
     * @param childClass is the subclass or implementing class who's we're introspecting.
     * @return list of resolved type arguments as classes.
     */
    public static Type getGenericTypeArgument(Class<?> baseClass, Type childClass, int index) {
        Map<Type,Type> resolvedTypes = new HashMap<Type, Type>();
        Type type = climbToBasePopulatingMap(baseClass, childClass, resolvedTypes);
        return resolveType(getTypeParams(type)[index], resolvedTypes);
    }

    static Type climbToBasePopulatingMap(final Class<?> baseClass, final Type childClass, Map<Type, Type> resolvedTypes) {
        Type type = childClass;

        // start walking up the inheritance hierarchy until we hit baseClass
        Class<?> rawType = getClass(type);
        Type owner = getOwner(type);

        // pull in info from owner.
        while (owner != null){
            populateMap(owner, getClass(owner), resolvedTypes);

            Type ownerSuper = getClass(owner).getGenericSuperclass();
            // TODO: do we need info from interfaces?
            while (ownerSuper != null) {
                final Class<?> ownerSuperRaw = getClass(ownerSuper);
                populateMap(ownerSuper, ownerSuperRaw,resolvedTypes);
                ownerSuper = ownerSuperRaw.getGenericSuperclass();
            }

            owner = getOwner(owner);
        }

        while (!baseClass.equals(rawType)) {
            populateMap(type,rawType,resolvedTypes);
            type = stepTowardsBaseClass(baseClass, rawType);
            rawType = getClass(type);
        }

        return type;
    }

    // Populates the resolvedTypes map with information collected at the current step up the inheritance chain.
    static void populateMap(Type type, Class<?> rawType, Map<Type,Type> resolvedTypes) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            TypeVariable<?>[] typeParameters = rawType.getTypeParameters();

            for (int i = 0; i < actualTypeArguments.length; i++) {
                resolvedTypes.put(typeParameters[i], actualTypeArguments[i]);
            }
        }
    }

    // Gets the type parameters from a Class or Parameterized type
    static Type[] getTypeParams(Type type) {
        if (type instanceof Class) {
            return ((Class<?>) type).getTypeParameters();
        }

        return ((ParameterizedType) type).getActualTypeArguments();
    }

    // Hunts through the resolvedTypes map and finds the actual type argument.
    static Type resolveType(Type type, Map<Type, Type> resolvedTypes) {
        while (resolvedTypes.containsKey(type)){
            type = resolvedTypes.get(type);
        }

        return type;
    }

    // Returns the next step up the hierarchy in the climb towards baseClass.
    // Returns Generic types, not concrete classes (getGenericSuperclass instead of getSuperclass).
    // Prefers to climb to the superclass to over an interface
    static Type stepTowardsBaseClass(Class<?> baseClass, Class<?> childClass) {
        if ((!childClass.isInterface()) && baseClass.isAssignableFrom(childClass.getSuperclass())) {
            return childClass.getGenericSuperclass();
        }

        final Class<?>[] interfaces = childClass.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            if (baseClass.isAssignableFrom(interfaces[i])) {
                return childClass.getGenericInterfaces()[i];
            }
        }

        throw new IllegalArgumentException("Can't find path from " + childClass + " to " + baseClass);
    }

    // Returns the ownerType of a Class or ParameterizedType.
    // getOwnerType(A<B>.C<D>) would be A<B>.
    static Type getOwner(Type type) {
        if (type instanceof Class) {
            return ((Class<?>) type).getEnclosingClass();
        }

        if (type instanceof ParameterizedType){
            return ((ParameterizedType) type).getOwnerType();
        }

        return null;
    }

    public static boolean isAssignable(Type superType, Class<?> subclass) {
        Class<?> c = getClass(superType);
        if (c != null) {
            return c.isAssignableFrom(subclass);
        }

        if (superType instanceof TypeVariable){
            for (Type type : ((TypeVariable<?>) superType).getBounds()) {
                if (!isAssignable(type,subclass)) {
                	return false;
                }
            }

            return true;
        }

        throw new RuntimeException("Can't tell? - shouldn't happen - please report this error");
    }
}
