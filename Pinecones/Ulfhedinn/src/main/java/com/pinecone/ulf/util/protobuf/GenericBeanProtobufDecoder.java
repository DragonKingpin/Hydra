package com.pinecone.ulf.util.protobuf;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.pinecone.framework.system.stereotype.JavaBeans;
import com.pinecone.framework.unit.Units;

public class GenericBeanProtobufDecoder implements BeanProtobufDecoder {
    @Override
    public <T> T decode( Class<T> clazz, String genericLabel, Descriptors.Descriptor descriptor, DynamicMessage dynamicMessage, Set<String> exceptedKeys, Options options ) {
        if( PrimitiveWrapper.isSupportedPrimitive( clazz ) ) {
            return clazz.cast( dynamicMessage.getField( descriptor.findFieldByName( PrimitiveWrapper.FieldName ) ) );
        }
        else if( RepeatedWrapper.isSupportedRepeated( clazz ) ) {
            Descriptors.FieldDescriptor fieldDescriptor = descriptor.findFieldByName( RepeatedWrapper.FieldName );
            Object val = dynamicMessage.getField( fieldDescriptor );
            Object ret = this.decodeRepeated( val, fieldDescriptor, options, clazz, genericLabel );
            return clazz.cast( ret );
        }
        else if( Map.class.isAssignableFrom( clazz ) ) {
            if( clazz.isInterface() && Map.class.isAssignableFrom( clazz ) ) {
                clazz = options.getDefaultMapType();
            }
            return clazz.cast( this.decodeMap( clazz, descriptor, dynamicMessage, exceptedKeys, options ) );
        }

        return clazz.cast( this.decodeBean( clazz, descriptor, dynamicMessage, exceptedKeys, options ) );
    }

    @Override
    public Map<String, Object> decodeMap( Class<?> clazz, Descriptors.Descriptor descriptor, DynamicMessage dynamicMessage, Set<String> exceptedKeys, Options options ) {
        if ( descriptor == null || dynamicMessage == null ) {
            return null;
        }

        Map<String, Object> result;
        if( clazz.isInterface() && Map.class.isAssignableFrom( clazz ) ) {
            result = Units.newInstance( options.getDefaultMapType() );
        }
        else {
            result = Units.newInstance( clazz );
        }

        for ( Descriptors.FieldDescriptor fieldDescriptor : descriptor.getFields() ) {
            try {
                String fieldName = fieldDescriptor.getName();

                // Skip excluded keys
                if ( exceptedKeys != null && exceptedKeys.contains( fieldName ) ) {
                    continue;
                }

                Object value = ProtobufUtils.evalValue( dynamicMessage, fieldDescriptor );

                if ( value != null ) {
                    if ( fieldDescriptor.isRepeated() ) {
                        List<?> values = (List<?>) value;
                        List<Object> decodedValues = new ArrayList<>();
                        for ( Object item : values ) {
                            decodedValues.add( this.decodeFieldValue( fieldDescriptor, item, item.getClass(), options ) );
                        }
                        result.put( fieldName, decodedValues );
                    }
                    else if ( fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.MESSAGE ) {
                        Descriptors.Descriptor nestedDescriptor = fieldDescriptor.getMessageType();
                        result.put( fieldName, this.decodeMap( clazz, nestedDescriptor, (DynamicMessage) value, exceptedKeys, options ) );
                    }
                    else {
                        result.put( fieldName, this.decodeFieldValue( fieldDescriptor, value, value.getClass(), options ) );
                    }
                }
            }
            catch ( Exception e ) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public <T > T decodeBean( Class<T> targetClass, Descriptors.Descriptor descriptor, DynamicMessage dynamicMessage, Set<String> exceptedKeys, Options options ) {
        if ( descriptor == null || dynamicMessage == null ) {
            return null;
        }
        else if ( BeanProtobufDecoder.isNullMessage( dynamicMessage, descriptor ) ) {
            return null;
        }

        try {
            if ( targetClass == null ) {
                return null;
            }

            Object bean;
            if( targetClass.isInterface() && Map.class.isAssignableFrom( targetClass ) ) {
                bean = Units.newInstance( options.getDefaultMapType() );
            }
            else {
                bean = targetClass.getDeclaredConstructor().newInstance();
            }

            for ( Descriptors.FieldDescriptor fieldDescriptor : descriptor.getFields() ) {
                String fieldName = fieldDescriptor.getName();

                if ( exceptedKeys != null && exceptedKeys.contains( fieldName ) ) {
                    continue;
                }

                Object value = ProtobufUtils.evalValue( dynamicMessage, fieldDescriptor );

                if ( value != null ) {
                    try {
                        String setterMethod = JavaBeans.MethodMajorKeySet + JavaBeans.methodKeyNameUpperCaseNormalize( fieldName );

                        Method setter = null;
                        try{
                            setter = targetClass.getMethod( setterMethod, this.decodeType( fieldDescriptor ) );
                        }
                        catch ( NoSuchMethodException | SecurityException e ) {
                            Method[] methods = targetClass.getMethods();
                            for( Method method : methods ) {
                                if( method.getParameterCount() == 1 && method.getName().equals( setterMethod ) ) {
                                    setter = method;
                                    break;
                                }
                            }

                            if( setter == null ){
                                continue;
                            }
                        }



                        if ( fieldDescriptor.isRepeated() ) {
                            Class<?>[] pars = setter.getParameterTypes();
                            if( pars.length > 0 ) {
                                Class<?> nestedType = pars[ 0 ];
                                String szGType = ProtobufUtils.evalSetterGenericLabel( setter );
                                setter.invoke( bean, this.decodeRepeated( value, fieldDescriptor, options, nestedType, szGType ) );
                            }
                        }
                        else if ( fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.MESSAGE ) {
                            Descriptors.Descriptor nestedDescriptor = fieldDescriptor.getMessageType();
                            Class<?>[] pars = setter.getParameterTypes();
                            if( pars.length > 0 ) {
                                Object nestedBean;
                                Class<?> nestedType = pars[ 0 ];
                                String szGType = ProtobufUtils.evalSetterGenericLabel( setter );
                                if( nestedType.equals( Map.class ) ) {
                                    nestedBean = this.decodeMap( nestedType, nestedDescriptor, (DynamicMessage) value, exceptedKeys, options );
                                }
                                else {
                                    if ( descriptor.equals( nestedDescriptor ) ) {
                                        DynamicMessage dyVal =(DynamicMessage) value;
                                        if ( BeanProtobufDecoder.isNullMessage( dyVal, nestedDescriptor ) ) {
                                            nestedBean = null;
                                        }
                                        else {
                                            nestedBean = this.decode( nestedType, szGType, nestedDescriptor, (DynamicMessage) value, exceptedKeys, options );
                                        }
                                    }
                                    else {
                                        nestedBean = this.decode( nestedType, szGType, nestedDescriptor, (DynamicMessage) value, exceptedKeys, options );
                                    }
                                }

                                setter.invoke( bean, nestedBean );
                            }
                        }
                        else {
                            Class<?>[] pars = setter.getParameterTypes();
                            if( pars.length > 0 ) {
                                Class<?> nestedType = pars[ 0 ];
                                String szGType = ProtobufUtils.evalSetterGenericLabel( setter );
                                setter.invoke( bean, this.decodeFieldValue( fieldDescriptor, value, nestedType, szGType, options ) );
                            }
                        }
                    }
                    catch ( IllegalAccessException | InvocationTargetException | IllegalArgumentException ignore ) {
                        //ignore.printStackTrace();
                    }
                }
            }

            return targetClass.cast( bean );
        }
        catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }
    }

    protected Object decodeFieldValue( Descriptors.FieldDescriptor fieldDescriptor, Object value, Class<?> valueType, String genericLabel, Options options ) {
        switch ( fieldDescriptor.getType() ) {
            case BOOL: {
                return value;
            }
            case INT32:
            case SINT32:
            case SFIXED32: {
                return value;
            }
            case INT64:
            case SINT64:
            case SFIXED64: {
                return value;
            }
            case FLOAT: {
                return value;
            }
            case DOUBLE: {
                return value;
            }
            case STRING: {
                return value.toString();
            }
            case BYTES: {
                return value instanceof com.google.protobuf.ByteString
                        ? ((com.google.protobuf.ByteString) value).toByteArray()
                        : value;
            }
            case MESSAGE: {
                Descriptors.Descriptor nestedDescriptor = fieldDescriptor.getMessageType();
                return this.decode( valueType, genericLabel, nestedDescriptor, (DynamicMessage) value, null, options );
            }
            default: {
                return value;
            }
        }
    }

    protected Object decodeFieldValue( Descriptors.FieldDescriptor fieldDescriptor, Object value, Class<?> valueType, Options options ) {
        return this.decodeFieldValue( fieldDescriptor, value, valueType, null, options );
    }

    protected Class<?> decodeType( Descriptors.FieldDescriptor fieldDescriptor ) {
        switch ( fieldDescriptor.getType() ) {
            case BOOL: {
                return Boolean.class;
            }
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
            case BYTES: {
                return byte[].class;
            }
            case MESSAGE: {
                return null;
            }
            default: {
                throw new IllegalArgumentException( "Unsupported field type: " + fieldDescriptor.getType() );
            }
        }
    }

    protected void setCollectionRepeated( Collection<?> values, Collection<Object> decodedValues, String genericTypeLabel, Descriptors.FieldDescriptor fieldDescriptor, Options options ) {
        if ( genericTypeLabel == null ) {
            throw new IllegalArgumentException( "Unable to decode `genericTypeLabel` with null." );
        }
        Class<?> componentType = ProtobufUtils.loadSingleGenericType( this.getClass(), genericTypeLabel );
        if ( componentType == null ) {
            throw new IllegalArgumentException( "Unable to decode `genericTypeLabel` " + genericTypeLabel + "." );
        }

        for ( Object item : values ) {
            decodedValues.add( this.decodeFieldValue( fieldDescriptor, item, componentType, options ) );
        }
    }

    protected Object decodeRepeated( Object value, Descriptors.FieldDescriptor fieldDescriptor, Options options, Class<?> type, String genericTypeLabel ) {
        if ( type.isArray() ) {
            List<?> values = (List<?>) value;
            Class<?> componentType = type.getComponentType();
            Object[] ret = (Object[]) Array.newInstance( type.getComponentType(), values.size() );
            int i = 0;
            for ( Object item : values ) {
                ret[ i ] = this.decodeFieldValue( fieldDescriptor, item, componentType, options );
                ++i;
            }
            return ret;
        }
        else if ( Collection.class.isAssignableFrom( type ) ) {
            List<?> values = (List<?>) value;
            List<Object> decodedValues = new ArrayList<>();

            this.setCollectionRepeated( values, decodedValues, genericTypeLabel, fieldDescriptor, options );

            return decodedValues;
        }
        else if ( Set.class.isAssignableFrom( type ) ) {
            List<?> values = (List<?>) value;
            Set<Object> decodedValues = new HashSet<>();

            this.setCollectionRepeated( values, decodedValues, genericTypeLabel, fieldDescriptor, options );

            return decodedValues;
        }

        return null;
    }
}
