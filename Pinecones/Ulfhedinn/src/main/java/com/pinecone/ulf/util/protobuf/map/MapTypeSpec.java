package com.pinecone.ulf.util.protobuf.map;

import java.util.Map;

import com.google.protobuf.DescriptorProtos;
import com.pinecone.framework.system.prototype.Pinenut;

public class MapTypeSpec implements Pinenut {
    protected final Class<?> mMapType;
    protected final Class<?> mKeyType;
    protected final Class<?> mValueType;
    protected final String   mszGenericLabel;

    public MapTypeSpec( Class<?> mapType, Class<?> keyType, Class<?> valueType, String szGenericLabel ) {
        this.mMapType        = mapType;
        this.mKeyType        = keyType;
        this.mValueType      = valueType;
        this.mszGenericLabel = szGenericLabel;
        this.validate();
    }

    public Class<?> getMapType() {
        return this.mMapType;
    }

    public Class<?> getKeyType() {
        return this.mKeyType;
    }

    public Class<?> getValueType() {
        return this.mValueType;
    }

    public String getGenericLabel() {
        return this.mszGenericLabel;
    }

    public DescriptorProtos.FieldDescriptorProto.Type getKeyFieldType() {
        return MapTypeSpec.toKeyFieldType( this.mKeyType );
    }

    public boolean isValueMessage() {
        return MapTypeSpec.toValueFieldType( this.mValueType ) == DescriptorProtos.FieldDescriptorProto.Type.TYPE_MESSAGE;
    }

    public DescriptorProtos.FieldDescriptorProto.Type getValueFieldType() {
        return MapTypeSpec.toValueFieldType( this.mValueType );
    }

    protected void validate() {
        if ( this.mMapType == null || !Map.class.isAssignableFrom( this.mMapType ) ) {
            throw new MapProtobufException( "Map protobuf type requires java.util.Map but found: " + this.mMapType );
        }
        if ( this.mKeyType == null || this.mValueType == null ) {
            throw new MapProtobufException( "Map protobuf type requires concrete key/value generic types: " + this.mszGenericLabel );
        }
        MapTypeSpec.toKeyFieldType( this.mKeyType );
        MapTypeSpec.toValueFieldType( this.mValueType );
    }

    public static DescriptorProtos.FieldDescriptorProto.Type toKeyFieldType( Class<?> type ) {
        if ( type == String.class ) {
            return DescriptorProtos.FieldDescriptorProto.Type.TYPE_STRING;
        }
        else if ( type == int.class || type == Integer.class ) {
            return DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT32;
        }
        else if ( type == long.class || type == Long.class ) {
            return DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT64;
        }
        else if ( type == boolean.class || type == Boolean.class ) {
            return DescriptorProtos.FieldDescriptorProto.Type.TYPE_BOOL;
        }
        throw new MapProtobufException( "Unsupported protobuf map key type: " + type.getName() );
    }

    public static DescriptorProtos.FieldDescriptorProto.Type toValueFieldType( Class<?> type ) {
        if ( type == String.class ) {
            return DescriptorProtos.FieldDescriptorProto.Type.TYPE_STRING;
        }
        else if ( type == int.class || type == Integer.class || type == short.class || type == Short.class || type == byte.class || type == Byte.class ) {
            return DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT32;
        }
        else if ( type == long.class || type == Long.class ) {
            return DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT64;
        }
        else if ( type == float.class || type == Float.class ) {
            return DescriptorProtos.FieldDescriptorProto.Type.TYPE_FLOAT;
        }
        else if ( type == double.class || type == Double.class ) {
            return DescriptorProtos.FieldDescriptorProto.Type.TYPE_DOUBLE;
        }
        else if ( type == boolean.class || type == Boolean.class ) {
            return DescriptorProtos.FieldDescriptorProto.Type.TYPE_BOOL;
        }
        else if ( type == byte[].class ) {
            return DescriptorProtos.FieldDescriptorProto.Type.TYPE_BYTES;
        }
        else if ( Map.class.isAssignableFrom( type ) ) {
            throw new MapProtobufException( "Nested protobuf map value is not supported: " + type.getName() );
        }
        return DescriptorProtos.FieldDescriptorProto.Type.TYPE_MESSAGE;
    }
}
