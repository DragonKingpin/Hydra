package com.pinecone.framework.system.prototype;


import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashSet;

public abstract class Prototype {
    public static String prototypeName( Object that ){
        try {
            return that.getClass().getSimpleName();
        }
        catch ( Exception E ){
            return "[object Object]";
        }
    }

    public static TypeIndex typeid( Object that ) {
        return new TypeIndex( that );
    }

    public static String namespace( Class that ){
        //return that.getName().split( "." + that.getSimpleName() )[0];
        return that.getPackage().getName();
    }

    public static String namespace( Object that ){
        return Prototype.namespace( that.getClass() );
    }

    public static String namespaceNode ( Class that ) {
        String szNamespace = Prototype.namespace( that );
        String[] debris = szNamespace.split("\\.");
        return debris.length <= 1 ? szNamespace : debris [ debris.length - 1 ];
    }

    public static String namespaceNode ( Object that ) {
        return Prototype.namespaceNode( that.getClass() );
    }

    public static boolean isAbstract ( Class that ) {
        return Modifier.isAbstract( that.getModifiers() );
    }

    private static String[] getPropertyNames ( Object that, boolean bAllOwned ) {
        if ( that == null ) {
            return null;
        }
        else {
            Class klass = that.getClass();
            Field[] fields = klass.getDeclaredFields();
            int length = fields.length;
            if ( length == 0 ) {
                return null;
            }
            else {
                String[] names = new String[length];

                int j = 0;
                for( int i = 0; i < length; ++i ) {
                    Field field = fields[i];
                    if ( (!Modifier.isPublic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible() ) {
                        if( !bAllOwned ){
                            continue;
                        }
                    }
                    names[j++] = fields[i].getName();
                }

                if( !bAllOwned ){
                    return Arrays.copyOf( names, j );
                }

                return names;
            }
        }
    }

    public static String[] getOwnPropertyNames ( Object that ){
        return Prototype.getPropertyNames( that, true );
    }

    public static String[] keys ( Object that ){
        return Prototype.getPropertyNames( that, false );
    }





    public static HashSet<String > getDeclaredMethodsNameSet( Object that ){
        HashSet<String > hashSet = new HashSet<>();
        Prototype.getDeclaredMethodsNameSet( hashSet, that );
        return hashSet;
    }

    public static void getDeclaredMethodsNameSet( HashSet<String > hSet, Object that ){
        Prototype.getDeclaredMethodsNameSet( hSet, that.getClass() );
    }

    public static HashSet<String > getDeclaredMethodsNameSet( Class<?> that ){
        HashSet<String > hashSet = new HashSet<>();
        Prototype.getDeclaredMethodsNameSet( hashSet, that );
        return hashSet;
    }

    public static void getDeclaredMethodsNameSet( HashSet<String > set,  Class<?> hThatClass ){
        Method[] methods = hThatClass.getDeclaredMethods();
        for ( Method row : methods ) {
            set.add( row.getName() );
        }
    }

    public static Object invokeNoParameterMethod ( Object that , String szFunctionName ) throws NoSuchMethodException, InvocationTargetException ,IllegalAccessException {
        Method method = that.getClass().getMethod( szFunctionName );
        return method.invoke( that );
    }

    public static boolean isMethodDeclared       ( Object that, String szFnName, Class<?>... parameterTypes ) {
        try{
            return that.getClass().getDeclaredMethod( szFnName, parameterTypes ) != null;
        }
        catch ( NoSuchMethodException e ){
            return false;
        }
    }

    public static Class primitivify( Class c ){
        if( c == Byte.class ){
            return byte.class;
        }
        else if( c == Short.class ){
            return short.class;
        }
        else if( c == Integer.class ){
            return int.class;
        }
        else if( c == Long.class ){
            return long.class;
        }
        else if( c == Float.class ){
            return float.class;
        }
        else if( c == Double.class ){
            return double.class;
        }
        else if( c == Character.class ){
            return char.class;
        }
        else if( c == Void.class ){
            return void.class;
        }

        return c;
    }







    /** Element **/
    public static boolean isNumber( Class<?> stereotype ) {
        if( Number.class.isAssignableFrom( stereotype ) ){
            return true;
        }
        else if( stereotype.isPrimitive() ){
            return  stereotype == byte.class  || stereotype == short.class ||
                    stereotype == int.class   || stereotype == long.class  ||
                    stereotype == float.class || stereotype == double.class;
        }
        return false;
    }

}
