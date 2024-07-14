package com.pinecone.framework.system.stereotype;

import java.lang.reflect.Method;

public final class JavaBeans {
    public static final String MethodKeyGetClass            = "getClass";
    public static final String MethodKeyGetDeclaringClass   = "getDeclaringClass";
    public static final String MethodMajorKeyGet            = "get";
    public static final int    MethodMajorKeyGetLength      = JavaBeans.MethodMajorKeyGet.length();
    public static final String MethodMajorKeyIs             = "is";
    public static final int    MethodMajorKeyIsLength       = JavaBeans.MethodMajorKeyIs.length();
    public static final String MethodMajorKeySet            = "set";
    public static final int    MethodMajorKeySetLength      = JavaBeans.MethodMajorKeySet.length();


    public static String getGetterMethodKeyName( String szMethodName ) {
        String key = null;
        if ( szMethodName.startsWith( JavaBeans.MethodMajorKeyGet ) ) {
            if ( !JavaBeans.MethodKeyGetClass.equals(szMethodName) && !JavaBeans.MethodKeyGetDeclaringClass.equals(szMethodName) ) {
                key = szMethodName.substring( JavaBeans.MethodMajorKeyGetLength ); // "get"
            }
        }
        else if ( szMethodName.startsWith( JavaBeans.MethodMajorKeyIs ) ) {
            key = szMethodName.substring( JavaBeans.MethodMajorKeyIsLength ); // "is"
        }

        return key;
    }

    public static String getGetterMethodKeyName( Method method ) {
        return JavaBeans.getGetterMethodKeyName( method.getName() );
    }

    // First character lower case.
    public static String methodKeyNameLowerCaseNormalize( String key ) {
        if ( key.length() == 1 ) {
            key = key.toLowerCase();
        }
        else if ( !Character.isUpperCase( key.charAt( 1 ) ) ) {
            key = key.substring(0, 1).toLowerCase() + key.substring(1);
        }

        return key;
    }

    // First character lower case.
    public static String getKeyGetterMethodNameLowerCaseNormalized( String szMethodName ) {
        return JavaBeans.methodKeyNameLowerCaseNormalize( JavaBeans.getGetterMethodKeyName( szMethodName ) );
    }

    // First character lower case.
    public static String getKeyGetterMethodNameLowerCaseNormalized( Method method ) {
        return JavaBeans.methodKeyNameLowerCaseNormalize( JavaBeans.getGetterMethodKeyName( method ) );
    }




    public static String getSetterMethodKeyName( String szMethodName ) {
        String key = null;
        if ( szMethodName.startsWith( JavaBeans.MethodMajorKeySet ) ) {
            key = szMethodName.substring( JavaBeans.MethodMajorKeySetLength ); // "set"
        }

        return key;
    }

    public static String getSetterMethodKeyName( Method method ) {
        return JavaBeans.getSetterMethodKeyName( method.getName() );
    }

    // First character upper case.
    public static String methodKeyNameUpperCaseNormalize( String key ) {
        if ( key.length() == 1 ) {
            key = key.toUpperCase();
        }
        else if ( Character.isLowerCase( key.charAt( 0 ) ) ) {
            key = key.substring(0, 1).toUpperCase() + key.substring(1);
        }

        return key;
    }


    // First character upper case.
    public static String getKeySetterMethodNameLowerCaseNormalized( String szMethodName ) {
        return JavaBeans.methodKeyNameLowerCaseNormalize( JavaBeans.getSetterMethodKeyName( szMethodName ) );
    }

    // First character upper case.
    public static String getKeySetterMethodNameLowerCaseNormalized( Method method ) {
        return JavaBeans.methodKeyNameLowerCaseNormalize( JavaBeans.getSetterMethodKeyName( method ) );
    }

}
