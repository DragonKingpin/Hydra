package com.pinecone.ulf.util.protobuf;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.pinecone.framework.util.ReflectionUtils;

public final class ProtobufUtils {

    public static Object evalValue(DynamicMessage dynamicMessage, Descriptors.FieldDescriptor fieldDescriptor ) {
        if ( !fieldDescriptor.isRepeated() ) {
            boolean bHasField = dynamicMessage.hasField( fieldDescriptor );
            if ( !bHasField ) {
                return null;
            }
        }
        return dynamicMessage.getField( fieldDescriptor );
    }

    static Class<?> loadSingleGenericType( Class<?> parent, String componentGenericLabel ) {
        try {
            String[] genericTypeNames = ReflectionUtils.extractGenericClassNames( componentGenericLabel );
            if( genericTypeNames != null && genericTypeNames.length > 0 ) {
                String genericTypeName = genericTypeNames[ 0 ];

                if( !genericTypeName.equals( "?" ) && !genericTypeName.equals( Object.class.getSimpleName() ) ) {
                    return parent.getClassLoader().loadClass( genericTypeName );
                }
            }
        }
        catch ( ClassNotFoundException e ) {
            return null;
        }

        return null;
    }

    public static String evalSetterGenericLabel( Method setter ) {
        Type[] gType   = setter.getGenericParameterTypes();
        String szGType ;
        if ( gType.length > 0 ) {
            szGType = gType[ 0 ].getTypeName();
        }
        else {
            szGType = null;
        }
        return szGType;
    }

}
