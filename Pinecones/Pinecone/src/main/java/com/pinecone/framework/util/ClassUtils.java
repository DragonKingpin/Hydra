package com.pinecone.framework.util;

import java.beans.Introspector;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public final class ClassUtils {
    public static final String ARRAY_SUFFIX = "[]";
    private static final String INTERNAL_ARRAY_PREFIX = "[";
    private static final String NON_PRIMITIVE_ARRAY_PREFIX = "[L";
    private static final char PACKAGE_SEPARATOR = '.';
    private static final char PATH_SEPARATOR = '/';
    private static final char INNER_CLASS_SEPARATOR = '$';
    public static final String CGLIB_CLASS_SEPARATOR = "$$";
    public static final String CLASS_FILE_SUFFIX = ".class";
    private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new HashMap<>(8);
    private static final Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new HashMap<>(8);
    private static final Map<String, Class<?>> primitiveTypeNameMap = new HashMap<>(32);
    private static final Map<String, Class<?>> commonClassCache = new HashMap<>(32);

    private ClassUtils() {
    }

    private static void registerCommonClasses( Class<?>... commonClasses ) {
        for ( Class<?> clazz : commonClasses ) {
            commonClassCache.put( clazz.getName(), clazz );
        }
    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;

        try {
            cl = Thread.currentThread().getContextClassLoader();
        }
        catch ( Throwable ignored ) {
        }

        if ( cl == null ) {
            cl = ClassUtils.class.getClassLoader();
            if ( cl == null ) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                }
                catch ( Throwable ignored ) {
                }
            }
        }

        return cl;
    }

    public static ClassLoader overrideThreadContextClassLoader( ClassLoader classLoaderToUse ) {
        Thread currentThread = Thread.currentThread();
        ClassLoader threadContextClassLoader = currentThread.getContextClassLoader();
        if ( classLoaderToUse != null && !classLoaderToUse.equals( threadContextClassLoader ) ) {
            currentThread.setContextClassLoader( classLoaderToUse );
            return threadContextClassLoader;
        }
        return null;
    }

    public static Class<?> forName( String name, ClassLoader classLoader ) throws ClassNotFoundException, LinkageError {
        Assert.notNull( name, "Name must not be null" );
        Class<?> clazz = resolvePrimitiveClassName( name );
        if ( clazz == null ) {
            clazz = commonClassCache.get( name );
        }

        if ( clazz != null ) {
            return clazz;
        }
        else {
            Class<?> elementClass;
            String elementName;
            if ( name.endsWith( ARRAY_SUFFIX ) ) {
                elementName = name.substring( 0, name.length() - ARRAY_SUFFIX.length() );
                elementClass = forName( elementName, classLoader );
                return Array.newInstance( elementClass, 0 ).getClass();
            }
            else if ( name.startsWith( NON_PRIMITIVE_ARRAY_PREFIX ) && name.endsWith( ";" ) ) {
                elementName = name.substring( NON_PRIMITIVE_ARRAY_PREFIX.length(), name.length() - 1 );
                elementClass = forName( elementName, classLoader );
                return Array.newInstance( elementClass, 0 ).getClass();
            }
            else if ( name.startsWith( INTERNAL_ARRAY_PREFIX ) ) {
                elementName = name.substring( INTERNAL_ARRAY_PREFIX.length() );
                elementClass = forName( elementName, classLoader );
                return Array.newInstance( elementClass, 0 ).getClass();
            }
            else {
                ClassLoader clToUse = classLoader;
                if ( classLoader == null ) {
                    clToUse = getDefaultClassLoader();
                }

                try {
                    return clToUse != null ? clToUse.loadClass( name ) : Class.forName( name );
                }
                catch ( ClassNotFoundException ex ) {
                    int lastDotIndex = name.lastIndexOf( PACKAGE_SEPARATOR );
                    if ( lastDotIndex != -1 ) {
                        String innerClassName = name.substring( 0, lastDotIndex ) + INNER_CLASS_SEPARATOR + name.substring( lastDotIndex + 1 );

                        try {
                            return clToUse != null ? clToUse.loadClass( innerClassName ) : Class.forName( innerClassName );
                        }
                        catch ( ClassNotFoundException ignored ) {
                        }
                    }

                    throw ex;
                }
            }
        }
    }

    public static Class<?> resolveClassName( String className, ClassLoader classLoader ) throws IllegalArgumentException {
        try {
            return forName( className, classLoader );
        }
        catch ( ClassNotFoundException e ) {
            throw new IllegalArgumentException( "Cannot find class [" + className + "]", e );
        }
        catch ( LinkageError e ) {
            throw new IllegalArgumentException( "Error loading class [" + className + "]: problem with class file or dependent class.", e );
        }
    }

    public static Class<?> resolvePrimitiveClassName( String name ) {
        Class<?> result = null;
        if ( name != null && name.length() <= 8 ) {
            result = primitiveTypeNameMap.get( name );
        }

        return result;
    }

    public static boolean isPresent( String className, ClassLoader classLoader ) {
        try {
            forName( className, classLoader );
            return true;
        }
        catch ( Throwable ignored ) {
            return false;
        }
    }

    public static Class<?> getUserClass( Object instance ) {
        Assert.notNull( instance, "Instance must not be null" );
        return getUserClass( instance.getClass() );
    }

    public static Class<?> getUserClass( Class<?> clazz ) {
        if ( clazz != null && clazz.getName().contains( CGLIB_CLASS_SEPARATOR ) ) {
            Class<?> superClass = clazz.getSuperclass();
            if ( superClass != null && !Object.class.equals( superClass ) ) {
                return superClass;
            }
        }

        return clazz;
    }

    public static boolean isCacheSafe( Class<?> clazz, ClassLoader classLoader ) {
        Assert.notNull( clazz, "Class must not be null" );

        try {
            ClassLoader target = clazz.getClassLoader();
            if ( target == null ) {
                return true;
            }
            else {
                ClassLoader cur = classLoader;
                if ( classLoader == target ) {
                    return true;
                }
                else {
                    do {
                        if ( cur == null ) {
                            return false;
                        }

                        cur = cur.getParent();
                    }
                    while ( cur != target );

                    return true;
                }
            }
        }
        catch ( SecurityException ignored ) {
            return true;
        }
    }

    public static String getShortName( String className ) {
        Assert.hasLength( className, "Class name must not be empty" );
        int lastDotIndex = className.lastIndexOf( PACKAGE_SEPARATOR );
        int nameEndIndex = className.indexOf( CGLIB_CLASS_SEPARATOR );
        if ( nameEndIndex == -1 ) {
            nameEndIndex = className.length();
        }

        String shortName = className.substring( lastDotIndex + 1, nameEndIndex );
        shortName = shortName.replace( INNER_CLASS_SEPARATOR, PACKAGE_SEPARATOR );
        return shortName;
    }

    public static String getShortName( Class<?> clazz ) {
        return getShortName( getQualifiedName( clazz ) );
    }

    public static String getShortNameAsProperty( Class<?> clazz ) {
        String shortName = getShortName( clazz );
        int dotIndex = shortName.lastIndexOf( PACKAGE_SEPARATOR );
        shortName = dotIndex != -1 ? shortName.substring( dotIndex + 1 ) : shortName;
        return Introspector.decapitalize( shortName );
    }

    public static String getClassFileName( Class<?> clazz ) {
        Assert.notNull( clazz, "Class must not be null" );
        String className = clazz.getName();
        int lastDotIndex = className.lastIndexOf( PACKAGE_SEPARATOR );
        return className.substring( lastDotIndex + 1 ) + CLASS_FILE_SUFFIX;
    }

    public static String getPackageName( Class<?> clazz ) {
        Assert.notNull( clazz, "Class must not be null" );
        return getPackageName( clazz.getName() );
    }

    public static String getPackageName( String fqClassName ) {
        Assert.notNull( fqClassName, "Class name must not be null" );
        int lastDotIndex = fqClassName.lastIndexOf( PACKAGE_SEPARATOR );
        return lastDotIndex != -1 ? fqClassName.substring( 0, lastDotIndex ) : "";
    }

    public static String getQualifiedName( Class<?> clazz ) {
        Assert.notNull( clazz, "Class must not be null" );
        return clazz.isArray() ? getQualifiedNameForArray( clazz ) : clazz.getName();
    }

    private static String getQualifiedNameForArray( Class<?> clazz ) {
        StringBuilder result = new StringBuilder();

        while ( clazz.isArray() ) {
            clazz = clazz.getComponentType();
            result.append( ARRAY_SUFFIX );
        }

        result.insert( 0, clazz.getName() );
        return result.toString();
    }

    public static String getQualifiedMethodName( Method method ) {
        Assert.notNull( method, "Method must not be null" );
        return method.getDeclaringClass().getName() + "." + method.getName();
    }

    public static String getDescriptiveType( Object value ) {
        if ( value == null ) {
            return null;
        }
        else {
            Class<?> clazz = value.getClass();
            if ( Proxy.isProxyClass( clazz ) ) {
                StringBuilder result = new StringBuilder( clazz.getName() );
                result.append( " implementing " );
                Class<?>[] ifcs = clazz.getInterfaces();

                for ( int i = 0; i < ifcs.length; ++i ) {
                    result.append( ifcs[i].getName() );
                    if ( i < ifcs.length - 1 ) {
                        result.append( ',' );
                    }
                }

                return result.toString();
            }
            return clazz.isArray() ? getQualifiedNameForArray( clazz ) : clazz.getName();
        }
    }

    public static boolean matchesTypeName( Class<?> clazz, String typeName ) {
        return typeName != null &&
                (
                        typeName.equals( clazz.getName() ) ||
                        typeName.equals( clazz.getSimpleName() ) ||
                        clazz.isArray() && typeName.equals( getQualifiedNameForArray( clazz ) )
                );
    }

    public static boolean hasConstructor( Class<?> clazz, Class<?>... paramTypes ) {
        return getConstructorIfAvailable( clazz, paramTypes ) != null;
    }

    public static <T> Constructor<T> getConstructorIfAvailable( Class<T> clazz, Class<?>... paramTypes ) {
        Assert.notNull( clazz, "Class must not be null" );

        try {
            return clazz.getConstructor( paramTypes );
        }
        catch ( NoSuchMethodException ignored ) {
            return null;
        }
    }

    public static boolean hasMethod( Class<?> clazz, String methodName, Class<?>... paramTypes ) {
        return getMethodIfAvailable( clazz, methodName, paramTypes ) != null;
    }

    public static Method getMethod( Class<?> clazz, String methodName, Class<?>... paramTypes ) {
        Assert.notNull( clazz, "Class must not be null" );
        Assert.notNull( methodName, "Method name must not be null" );
        if ( paramTypes != null ) {
            try {
                return clazz.getMethod( methodName, paramTypes );
            }
            catch ( NoSuchMethodException e ) {
                throw new IllegalStateException( "Expected method not found: " + e );
            }
        }
        else {
            Set<Method> candidates = new HashSet<>(1);
            Method[] methods = clazz.getMethods();
            int len = methods.length;

            for ( int i = 0; i < len; ++i ) {
                Method method = methods[i];
                if ( methodName.equals( method.getName() ) ) {
                    candidates.add( method );
                }
            }

            if ( candidates.size() == 1 ) {
                return candidates.iterator().next();
            }
            else if ( candidates.isEmpty() ) {
                throw new IllegalStateException( "Expected method not found: " + clazz + "." + methodName );
            }
            else {
                throw new IllegalStateException( "No unique method found: " + clazz + "." + methodName );
            }
        }
    }

    public static Method getFirstMethodByName( Class<?> clazz, String methodName ) {
        Method[] methods = clazz.getMethods();
        for ( Method method : methods ) {
            if ( method.getName().equals( methodName ) ) {
                return method;
            }
        }
        return null;
    }

    public static Method getMethodIfAvailable( Class<?> clazz, String methodName, Class<?>... paramTypes ) {
        Assert.notNull( clazz, "Class must not be null" );
        Assert.notNull( methodName, "Method name must not be null" );
        if ( paramTypes != null ) {
            try {
                return clazz.getMethod( methodName, paramTypes );
            }
            catch ( NoSuchMethodException ignored ) {
                return null;
            }
        }
        else {
            Set<Method> candidates = new HashSet<>(1);
            Method[] methods = clazz.getMethods();

            for ( Method method : methods ) {
                if ( methodName.equals( method.getName() ) ) {
                    candidates.add( method );
                }
            }

            if ( candidates.size() == 1 ) {
                return candidates.iterator().next();
            }
            return null;
        }
    }

    public static int getMethodCountForName( Class<?> clazz, String methodName ) {
        Assert.notNull( clazz, "Class must not be null" );
        Assert.notNull( methodName, "Method name must not be null" );
        int count = 0;
        Method[] declaredMethods = clazz.getDeclaredMethods();

        for ( Method method : declaredMethods ) {
            if ( methodName.equals( method.getName() ) ) {
                ++count;
            }
        }

        Class<?>[] ifcs = clazz.getInterfaces();
        for ( Class<?> ifc : ifcs ) {
            count += getMethodCountForName( ifc, methodName );
        }

        if ( clazz.getSuperclass() != null ) {
            count += getMethodCountForName( clazz.getSuperclass(), methodName );
        }

        return count;
    }

    public static boolean hasAtLeastOneMethodWithName( Class<?> clazz, String methodName ) {
        Assert.notNull( clazz, "Class must not be null" );
        Assert.notNull( methodName, "Method name must not be null" );
        Method[] declaredMethods = clazz.getDeclaredMethods();

        for ( Method method : declaredMethods ) {
            if ( method.getName().equals( methodName ) ) {
                return true;
            }
        }

        Class<?>[] ifcs = clazz.getInterfaces();
        for ( Class<?> ifc : ifcs ) {
            if ( hasAtLeastOneMethodWithName( ifc, methodName ) ) {
                return true;
            }
        }

        return clazz.getSuperclass() != null && hasAtLeastOneMethodWithName( clazz.getSuperclass(), methodName );
    }

    public static Method getMostSpecificMethod( Method method, Class<?> targetClass ) {
        if ( method != null && isOverridable( method, targetClass ) && targetClass != null && !targetClass.equals( method.getDeclaringClass() ) ) {
            try {
                if ( Modifier.isPublic( method.getModifiers() ) ) {
                    try {
                        return targetClass.getMethod( method.getName(), method.getParameterTypes() );
                    }
                    catch ( NoSuchMethodException ignored ) {
                        return method;
                    }
                }

                Method specificMethod = ReflectionUtils.findMethod( targetClass, method.getName(), method.getParameterTypes() );
                return specificMethod != null ? specificMethod : method;
            }
            catch ( SecurityException ignored ) {
            }
        }

        return method;
    }

    public static boolean isUserLevelMethod( Method method ) {
        Assert.notNull( method, "Method must not be null" );
        return method.isBridge() || !method.isSynthetic() && !isGroovyObjectMethod( method );
    }

    private static boolean isGroovyObjectMethod( Method method ) {
        return method.getDeclaringClass().getName().equals( "groovy.lang.GroovyObject" );
    }

    private static boolean isOverridable( Method method, Class<?> targetClass ) {
        if ( Modifier.isPrivate( method.getModifiers() ) ) {
            return false;
        }
        return !Modifier.isPublic( method.getModifiers() ) && !Modifier.isProtected( method.getModifiers() ) ? getPackageName( method.getDeclaringClass() ).equals( getPackageName( targetClass ) ) : true;
    }

    public static Method getStaticMethod( Class<?> clazz, String methodName, Class<?>... args ) {
        Assert.notNull( clazz, "Class must not be null" );
        Assert.notNull( methodName, "Method name must not be null" );

        try {
            Method method = clazz.getMethod( methodName, args );
            return Modifier.isStatic( method.getModifiers() ) ? method : null;
        }
        catch ( NoSuchMethodException ignored ) {
            return null;
        }
    }

    public static boolean isPrimitiveWrapper( Class<?> clazz ) {
        Assert.notNull( clazz, "Class must not be null" );
        return primitiveWrapperTypeMap.containsKey( clazz );
    }

    public static boolean isPrimitiveOrWrapper( Class<?> clazz ) {
        Assert.notNull( clazz, "Class must not be null" );
        return clazz.isPrimitive() || isPrimitiveWrapper( clazz );
    }

    public static boolean isPrimitiveArray( Class<?> clazz ) {
        Assert.notNull( clazz, "Class must not be null" );
        return clazz.isArray() && clazz.getComponentType().isPrimitive();
    }

    public static boolean isPrimitiveWrapperArray( Class<?> clazz ) {
        Assert.notNull( clazz, "Class must not be null" );
        return clazz.isArray() && isPrimitiveWrapper( clazz.getComponentType() );
    }

    public static Class<?> resolvePrimitiveIfNecessary( Class<?> clazz ) {
        Assert.notNull( clazz, "Class must not be null" );
        return clazz.isPrimitive() && clazz != Void.TYPE ? primitiveTypeToWrapperMap.get( clazz ) : clazz;
    }

    public static boolean isAssignable( Class<?> lhsType, Class<?> rhsType ) {
        Assert.notNull( lhsType, "Left-hand side type must not be null" );
        Assert.notNull( rhsType, "Right-hand side type must not be null" );
        if ( lhsType.isAssignableFrom( rhsType ) ) {
            return true;
        }
        else {
            Class<?> resolvedPrimitive;
            if ( lhsType.isPrimitive() ) {
                resolvedPrimitive = primitiveWrapperTypeMap.get( rhsType );
                if ( lhsType.equals( resolvedPrimitive ) ) {
                    return true;
                }
            }
            else {
                resolvedPrimitive = primitiveTypeToWrapperMap.get( rhsType );
                if ( resolvedPrimitive != null && lhsType.isAssignableFrom( resolvedPrimitive ) ) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean isAssignableValue( Class<?> type, Object value ) {
        Assert.notNull( type, "Type must not be null" );
        return value != null ? isAssignable( type, value.getClass() ) : !type.isPrimitive();
    }

    public static String convertResourcePathToClassName( String resourcePath ) {
        Assert.notNull( resourcePath, "Resource path must not be null" );
        return resourcePath.replace( PATH_SEPARATOR, PACKAGE_SEPARATOR );
    }

    public static String convertClassNameToResourcePath( String className ) {
        Assert.notNull( className, "Class name must not be null" );
        return className.replace( PACKAGE_SEPARATOR, PATH_SEPARATOR );
    }

    public static String addResourcePathToPackagePath( Class<?> clazz, String resourceName ) {
        Assert.notNull( resourceName, "Resource name must not be null" );
        return !resourceName.startsWith( "/" ) ? classPackageAsResourcePath( clazz ) + "/" + resourceName : classPackageAsResourcePath( clazz ) + resourceName;
    }

    public static String classPackageAsResourcePath( Class<?> clazz ) {
        if ( clazz == null ) {
            return "";
        }
        else {
            String className = clazz.getName();
            int packageEndIndex = className.lastIndexOf( PACKAGE_SEPARATOR );
            if ( packageEndIndex == -1 ) {
                return "";
            }
            String packageName = className.substring( 0, packageEndIndex );
            return packageName.replace( PACKAGE_SEPARATOR, PATH_SEPARATOR );
        }
    }

    public static String classNamesToString( Class<?>... classes ) {
        return classNamesToString( Arrays.asList( classes ) );
    }

    public static String classNamesToString( Collection<Class<?>> classes ) {
        if ( CollectionUtils.isEmpty( classes ) ) {
            return "[]";
        }
        else {
            StringBuilder sb = new StringBuilder( "[" );
            Iterator<Class<?>> it = classes.iterator();

            while ( it.hasNext() ) {
                Class<?> clazz = it.next();
                sb.append( clazz.getName() );
                if ( it.hasNext() ) {
                    sb.append( ", " );
                }
            }

            sb.append( "]" );
            return sb.toString();
        }
    }

    public static Class<?>[] toClassArray( Collection<Class<?>> collection ) {
        return collection == null ? null : collection.toArray( new Class<?>[collection.size()] );
    }

    public static Class<?>[] getAllInterfaces( Object instance ) {
        Assert.notNull( instance, "Instance must not be null" );
        return getAllInterfacesForClass( instance.getClass() );
    }

    public static Class<?>[] getAllInterfacesForClass( Class<?> clazz ) {
        return getAllInterfacesForClass( clazz, (ClassLoader)null );
    }

    public static Class<?>[] getAllInterfacesForClass( Class<?> clazz, ClassLoader classLoader ) {
        Set<Class<?>> ifcs = getAllInterfacesForClassAsSet( clazz, classLoader );
        return ifcs.toArray( new Class<?>[ifcs.size()] );
    }

    public static Set<Class<?>> getAllInterfacesAsSet( Object instance ) {
        Assert.notNull( instance, "Instance must not be null" );
        return getAllInterfacesForClassAsSet( instance.getClass() );
    }

    public static Set<Class<?>> getAllInterfacesForClassAsSet( Class<?> clazz ) {
        return getAllInterfacesForClassAsSet( clazz, (ClassLoader)null );
    }

    public static Set<Class<?>> getAllInterfacesForClassAsSet( Class<?> clazz, ClassLoader classLoader ) {
        Assert.notNull( clazz, "Class must not be null" );
        if ( clazz.isInterface() && isVisible( clazz, classLoader ) ) {
            return Collections.singleton( clazz );
        }
        else {
            LinkedHashSet<Class<?>> interfaces;
            for ( interfaces = new LinkedHashSet<>(); clazz != null; clazz = clazz.getSuperclass() ) {
                Class<?>[] ifcs = clazz.getInterfaces();
                for ( Class<?> ifc : ifcs ) {
                    interfaces.addAll( getAllInterfacesForClassAsSet( ifc, classLoader ) );
                }
            }

            return interfaces;
        }
    }

    public static Class<?> createCompositeInterface( Class<?>[] interfaces, ClassLoader classLoader ) {
        Assert.notEmpty( interfaces, "Interfaces must not be empty" );
        Assert.notNull( classLoader, "ClassLoader must not be null" );
        Object proxy = Proxy.newProxyInstance(
                classLoader,
                interfaces,
                ( object, method, args ) -> {
                    throw new UnsupportedOperationException( "Composite interface proxy should not be invoked." );
                }
        );
        return proxy.getClass();
    }

    public static Class<?> determineCommonAncestor( Class<?> clazz1, Class<?> clazz2 ) {
        if ( clazz1 == null ) {
            return clazz2;
        }
        else if ( clazz2 == null ) {
            return clazz1;
        }
        else if ( clazz1.isAssignableFrom( clazz2 ) ) {
            return clazz1;
        }
        else if ( clazz2.isAssignableFrom( clazz1 ) ) {
            return clazz2;
        }
        else {
            Class<?> ancestor = clazz1;

            do {
                ancestor = ancestor.getSuperclass();
                if ( ancestor == null || Object.class.equals( ancestor ) ) {
                    return null;
                }
            }
            while ( !ancestor.isAssignableFrom( clazz2 ) );

            return ancestor;
        }
    }

    public static boolean isVisible( Class<?> clazz, ClassLoader classLoader ) {
        if ( classLoader == null ) {
            return true;
        }
        else {
            try {
                Class<?> actualClass = classLoader.loadClass( clazz.getName() );
                return clazz == actualClass;
            }
            catch ( ClassNotFoundException ignored ) {
                return false;
            }
        }
    }

    public static boolean isCglibProxy( Object object ) {
        return isCglibProxyClass( object.getClass() );
    }

    public static boolean isCglibProxyClass( Class<?> clazz ) {
        return clazz != null && isCglibProxyClassName( clazz.getName() );
    }

    public static boolean isCglibProxyClassName( String className ) {
        return className != null && className.contains( CGLIB_CLASS_SEPARATOR );
    }

    static {
        primitiveWrapperTypeMap.put( Boolean.class, Boolean.TYPE );
        primitiveWrapperTypeMap.put( Byte.class, Byte.TYPE );
        primitiveWrapperTypeMap.put( Character.class, Character.TYPE );
        primitiveWrapperTypeMap.put( Double.class, Double.TYPE );
        primitiveWrapperTypeMap.put( Float.class, Float.TYPE );
        primitiveWrapperTypeMap.put( Integer.class, Integer.TYPE );
        primitiveWrapperTypeMap.put( Long.class, Long.TYPE );
        primitiveWrapperTypeMap.put( Short.class, Short.TYPE );
        Iterator<Entry<Class<?>, Class<?>>> primitiveWrapperTypes = primitiveWrapperTypeMap.entrySet().iterator();

        while ( primitiveWrapperTypes.hasNext() ) {
            Entry<Class<?>, Class<?>> entry = primitiveWrapperTypes.next();
            primitiveTypeToWrapperMap.put( entry.getValue(), entry.getKey() );
            registerCommonClasses( entry.getKey() );
        }

        Set<Class<?>> primitiveTypes = new HashSet<>(32);
        primitiveTypes.addAll( primitiveWrapperTypeMap.values() );
        primitiveTypes.addAll( Arrays.asList( boolean[].class, byte[].class, char[].class, double[].class, float[].class, int[].class, long[].class, short[].class ) );
        primitiveTypes.add( Void.TYPE );
        Iterator<Class<?>> primitiveTypeIterator = primitiveTypes.iterator();

        while ( primitiveTypeIterator.hasNext() ) {
            Class<?> primitiveType = primitiveTypeIterator.next();
            primitiveTypeNameMap.put( primitiveType.getName(), primitiveType );
        }

        registerCommonClasses( Boolean[].class, Byte[].class, Character[].class, Double[].class, Float[].class, Integer[].class, Long[].class, Short[].class );
        registerCommonClasses( Number.class, Number[].class, String.class, String[].class, Object.class, Object[].class, Class.class, Class[].class );
        registerCommonClasses( Throwable.class, Exception.class, RuntimeException.class, Error.class, StackTraceElement.class, StackTraceElement[].class );
    }
}
