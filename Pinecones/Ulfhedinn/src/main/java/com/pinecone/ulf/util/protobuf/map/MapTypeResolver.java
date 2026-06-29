package com.pinecone.ulf.util.protobuf.map;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

import com.pinecone.framework.util.ReflectionUtils;

public final class MapTypeResolver {
    private MapTypeResolver() {
    }

    public static boolean isMapType( Class<?> type ) {
        return type != null && Map.class.isAssignableFrom( type );
    }

    public static MapTypeSpec resolve( Class<?> mapType, String szGenericLabel, ClassLoader classLoader ) {
        if ( !MapTypeResolver.isMapType( mapType ) ) {
            return null;
        }
        String[] genericTypeNames = ReflectionUtils.extractGenericClassNames( szGenericLabel );
        if ( genericTypeNames == null || genericTypeNames.length != 2 ) {
            throw new MapProtobufException( "Map protobuf type requires exactly two generic types: " + szGenericLabel );
        }
        Class<?> keyType   = MapTypeResolver.loadGenericClass( genericTypeNames[ 0 ], classLoader );
        Class<?> valueType = MapTypeResolver.loadGenericClass( genericTypeNames[ 1 ], classLoader );
        return new MapTypeSpec( mapType, keyType, valueType, szGenericLabel );
    }

    public static MapTypeSpec resolveGetter( Method method, ClassLoader classLoader ) {
        if ( method == null || !MapTypeResolver.isMapType( method.getReturnType() ) ) {
            return null;
        }
        Type genericType = method.getGenericReturnType();
        return MapTypeResolver.resolve( method.getReturnType(), genericType == null ? null : genericType.getTypeName(), classLoader );
    }

    public static MapTypeSpec resolveSetter( Method method, ClassLoader classLoader ) {
        if ( method == null || method.getParameterCount() != 1 || !MapTypeResolver.isMapType( method.getParameterTypes()[ 0 ] ) ) {
            return null;
        }
        Type[] genericTypes = method.getGenericParameterTypes();
        return MapTypeResolver.resolve( method.getParameterTypes()[ 0 ], genericTypes.length == 0 ? null : genericTypes[ 0 ].getTypeName(), classLoader );
    }

    protected static Class<?> loadGenericClass( String szName, ClassLoader classLoader ) {
        if ( szName == null ) {
            throw new MapProtobufException( "Map protobuf generic type is null." );
        }
        String szType = szName.trim();
        if ( szType.equals( "?" ) || szType.equals( Object.class.getSimpleName() ) || szType.equals( Object.class.getName() ) ) {
            throw new MapProtobufException( "Unsupported raw or wildcard map generic type: " + szName );
        }
        Class<?> primitive = MapTypeResolver.tryPrimitive( szType );
        if ( primitive != null ) {
            return primitive;
        }
        try {
            return classLoader.loadClass( szType );
        }
        catch ( ClassNotFoundException e ) {
            throw new MapProtobufException( "Unable to load map generic type: " + szType, e );
        }
    }

    protected static Class<?> tryPrimitive( String szType ) {
        switch ( szType ) {
            case "int": {
                return int.class;
            }
            case "long": {
                return long.class;
            }
            case "float": {
                return float.class;
            }
            case "double": {
                return double.class;
            }
            case "boolean": {
                return boolean.class;
            }
            case "byte": {
                return byte.class;
            }
            case "short": {
                return short.class;
            }
            default: {
                return null;
            }
        }
    }
}
