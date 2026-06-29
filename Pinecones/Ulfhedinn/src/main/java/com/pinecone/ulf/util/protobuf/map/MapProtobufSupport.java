package com.pinecone.ulf.util.protobuf.map;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.protobuf.ByteString;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.pinecone.ulf.util.protobuf.BeanProtobufDecoder;
import com.pinecone.ulf.util.protobuf.BeanProtobufEncoder;
import com.pinecone.ulf.util.protobuf.Options;

public final class MapProtobufSupport {
    public static final String KeyFieldName   = "key";
    public static final String ValueFieldName = "value";

    private MapProtobufSupport() {
    }

    public static DescriptorProtos.FieldDescriptorProto.Builder transformMapField(
            String szFieldName, int nFieldNumber, MapTypeSpec spec, List<Descriptors.FileDescriptor> dependencies,
            Set<String> exceptedKeys, Options options, String szEntryName, BeanProtobufEncoder encoder
    ) {
        try {
            Descriptors.Descriptor entryDescriptor = MapProtobufSupport.transformMapEntry(
                    spec, dependencies, exceptedKeys, options, szEntryName, encoder
            );
            return DescriptorProtos.FieldDescriptorProto.newBuilder()
                    .setName( szFieldName )
                    .setNumber( nFieldNumber )
                    .setLabel( DescriptorProtos.FieldDescriptorProto.Label.LABEL_REPEATED )
                    .setType( DescriptorProtos.FieldDescriptorProto.Type.TYPE_MESSAGE )
                    .setTypeName( entryDescriptor.getFullName() );
        }
        catch ( Descriptors.DescriptorValidationException e ) {
            throw new MapProtobufException( "Unable to transform protobuf map field: " + szFieldName, e );
        }
    }

    protected static Descriptors.Descriptor transformMapEntry(
            MapTypeSpec spec, List<Descriptors.FileDescriptor> dependencies, Set<String> exceptedKeys,
            Options options, String szEntryName, BeanProtobufEncoder encoder
    ) throws Descriptors.DescriptorValidationException {
        DescriptorProtos.DescriptorProto.Builder entryBuilder = DescriptorProtos.DescriptorProto.newBuilder()
                .setName( options.normalizeDescriptorName( szEntryName + "Entry" ) )
                .setOptions( DescriptorProtos.MessageOptions.newBuilder().setMapEntry( true ) );

        entryBuilder.addField( DescriptorProtos.FieldDescriptorProto.newBuilder()
                .setName( KeyFieldName )
                .setNumber( 1 )
                .setType( spec.getKeyFieldType() ) );

        DescriptorProtos.FieldDescriptorProto.Builder valueBuilder = DescriptorProtos.FieldDescriptorProto.newBuilder()
                .setName( ValueFieldName )
                .setNumber( 2 )
                .setType( spec.getValueFieldType() );

        if ( spec.isValueMessage() ) {
            Descriptors.Descriptor valueDescriptor = encoder.transform( spec.getValueType(), null, null, exceptedKeys );
            if ( valueDescriptor == null ) {
                throw new MapProtobufException( "Unable to transform protobuf map value type: " + spec.getValueType().getName() );
            }
            valueBuilder.setTypeName( valueDescriptor.getFullName() );
            dependencies.add( valueDescriptor.getFile() );
        }

        entryBuilder.addField( valueBuilder );

        Descriptors.FileDescriptor fileDescriptor = Descriptors.FileDescriptor.buildFrom(
                DescriptorProtos.FileDescriptorProto.newBuilder()
                        .setName( entryBuilder.getName() + options.getDescriptorFileExtend() )
                        .addMessageType( entryBuilder.build() )
                        .build(),
                dependencies.toArray( new Descriptors.FileDescriptor[ 0 ] )
        );
        Descriptors.Descriptor entryDescriptor = fileDescriptor.findMessageTypeByName( entryBuilder.getName() );
        dependencies.add( entryDescriptor.getFile() );
        return entryDescriptor;
    }

    public static boolean isMapField( Descriptors.FieldDescriptor fieldDescriptor ) {
        return fieldDescriptor != null && fieldDescriptor.isMapField();
    }

    public static void encodeMapField(
            Descriptors.FieldDescriptor fieldDescriptor, DynamicMessage.Builder messageBuilder, Map<?, ?> map,
            BeanProtobufEncoder encoder, Set<String> exceptedKeys, Options options
    ) {
        if ( fieldDescriptor == null || messageBuilder == null ) {
            return;
        }
        if ( map == null ) {
            messageBuilder.clearField( fieldDescriptor );
            return;
        }

        Descriptors.Descriptor entryDescriptor = fieldDescriptor.getMessageType();
        Descriptors.FieldDescriptor keyField = entryDescriptor.findFieldByName( KeyFieldName );
        Descriptors.FieldDescriptor valueField = entryDescriptor.findFieldByName( ValueFieldName );

        for ( Map.Entry<?, ?> entry : map.entrySet() ) {
            DynamicMessage.Builder entryBuilder = DynamicMessage.newBuilder( entryDescriptor );
            entryBuilder.setField( keyField, MapProtobufSupport.coerceScalar( entry.getKey(), keyField ) );
            Object value = entry.getValue();
            if ( value != null ) {
                if ( valueField.getType() == Descriptors.FieldDescriptor.Type.MESSAGE ) {
                    entryBuilder.setField( valueField, encoder.encode( valueField.getMessageType(), value, exceptedKeys, options ) );
                }
                else {
                    entryBuilder.setField( valueField, MapProtobufSupport.coerceScalar( value, valueField ) );
                }
            }
            messageBuilder.addRepeatedField( fieldDescriptor, entryBuilder.build() );
        }
    }

    public static Map<Object, Object> decodeMapField(
            Descriptors.FieldDescriptor fieldDescriptor, Object value, Class<?> keyType, Class<?> valueType,
            BeanProtobufDecoder decoder, Set<String> exceptedKeys, Options options
    ) {
        Map<Object, Object> ret = new LinkedHashMap<>();
        if ( value == null ) {
            return ret;
        }
        List<?> values = (List<?>) value;
        Descriptors.Descriptor entryDescriptor = fieldDescriptor.getMessageType();
        Descriptors.FieldDescriptor keyField = entryDescriptor.findFieldByName( KeyFieldName );
        Descriptors.FieldDescriptor valueField = entryDescriptor.findFieldByName( ValueFieldName );

        for ( Object item : values ) {
            DynamicMessage entryMessage = (DynamicMessage) item;
            Object key = MapProtobufSupport.decodeScalar( entryMessage.getField( keyField ), keyType, keyField );
            Object entryValue = null;
            if ( entryMessage.hasField( valueField ) ) {
                Object rawValue = entryMessage.getField( valueField );
                if ( valueField.getType() == Descriptors.FieldDescriptor.Type.MESSAGE ) {
                    if ( valueType == null || valueType == Object.class || Map.class.isAssignableFrom( valueType ) ) {
                        entryValue = decoder.decodeMap( LinkedHashMap.class, valueField.getMessageType(), (DynamicMessage) rawValue, exceptedKeys, options );
                    }
                    else {
                        entryValue = decoder.decode( valueType, valueField.getMessageType(), (DynamicMessage) rawValue, exceptedKeys, options );
                    }
                }
                else {
                    entryValue = MapProtobufSupport.decodeScalar( rawValue, valueType, valueField );
                }
            }
            ret.put( key, entryValue );
        }
        return ret;
    }

    public static Map<Object, Object> decodeMapField(
            Descriptors.FieldDescriptor fieldDescriptor, Object value, BeanProtobufDecoder decoder,
            Set<String> exceptedKeys, Options options
    ) {
        Descriptors.Descriptor entryDescriptor = fieldDescriptor.getMessageType();
        Descriptors.FieldDescriptor keyField = entryDescriptor.findFieldByName( KeyFieldName );
        Descriptors.FieldDescriptor valueField = entryDescriptor.findFieldByName( ValueFieldName );
        return MapProtobufSupport.decodeMapField(
                fieldDescriptor, value, MapProtobufSupport.inferJavaType( keyField ), MapProtobufSupport.inferJavaType( valueField ),
                decoder, exceptedKeys, options
        );
    }

    protected static Class<?> inferJavaType( Descriptors.FieldDescriptor fieldDescriptor ) {
        switch ( fieldDescriptor.getType() ) {
            case INT32:
            case SINT32:
            case SFIXED32: {
                return Integer.class;
            }
            case INT64:
            case SINT64:
            case SFIXED64: {
                return Long.class;
            }
            case FLOAT: {
                return Float.class;
            }
            case DOUBLE: {
                return Double.class;
            }
            case STRING: {
                return String.class;
            }
            case BOOL: {
                return Boolean.class;
            }
            case BYTES: {
                return byte[].class;
            }
            case MESSAGE: {
                return LinkedHashMap.class;
            }
            default: {
                return Object.class;
            }
        }
    }

    protected static Object coerceScalar( Object value, Descriptors.FieldDescriptor fieldDescriptor ) {
        if ( value == null ) {
            return null;
        }
        switch ( fieldDescriptor.getType() ) {
            case INT32:
            case SINT32:
            case SFIXED32: {
                return ((Number) value).intValue();
            }
            case INT64:
            case SINT64:
            case SFIXED64: {
                return ((Number) value).longValue();
            }
            case FLOAT: {
                return ((Number) value).floatValue();
            }
            case DOUBLE: {
                return ((Number) value).doubleValue();
            }
            case STRING: {
                return value.toString();
            }
            case BOOL: {
                return (Boolean) value;
            }
            case BYTES: {
                return ByteString.copyFrom( (byte[]) value );
            }
            default: {
                return value;
            }
        }
    }

    protected static Object decodeScalar( Object value, Class<?> targetType, Descriptors.FieldDescriptor fieldDescriptor ) {
        if ( value == null ) {
            return null;
        }
        if ( fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.BYTES && value instanceof ByteString ) {
            return ((ByteString) value).toByteArray();
        }
        if ( targetType == byte.class || targetType == Byte.class ) {
            return ((Number) value).byteValue();
        }
        if ( targetType == short.class || targetType == Short.class ) {
            return ((Number) value).shortValue();
        }
        return value;
    }
}
