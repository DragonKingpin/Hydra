package com.pinecone.slime.source;

import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.unit.LinkedTreeMap;
import com.pinecone.slime.source.ResultConverter;

import java.beans.IntrospectionException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.Map;
import java.text.ParseException;

public class GenericResultConverter<V > implements ResultConverter<V > {
    private Class<V >       mValueType;
    private Set<String >    mValueMetaKeys;

    public GenericResultConverter( Class<V > valueType, Set<String > valueMetaKeys ) {
        this.mValueType     = valueType;
        this.mValueMetaKeys = valueMetaKeys;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V convert( Object val ) {
        if ( val instanceof Map ) {
            Map<String, Object> map = (Map<String, Object>) val;

            // Handling single value scenario for primitive or String
            if ( this.mValueMetaKeys.size() == 1 ) {
                Object singleValue = map.get( this.mValueMetaKeys.iterator().next() );
                if ( ResultConverter.isPrimitiveOrSpecialType( this.mValueType ) ) {
                    return (V) this.convertToType( singleValue, this.mValueType );
                }
            }

            // Handling Map scenarios
            if ( Map.class.isAssignableFrom( this.mValueType ) ) {
                if ( val instanceof LinkedTreeMap && this.mValueType.isAssignableFrom( LinkedTreeMap.class ) ) {
                    return (V) map;
                }
                else {
                    try {
                        Map<String, Object> targetMap = ( Map<String, Object> ) this.mValueType.getDeclaredConstructor().newInstance();
                        targetMap.putAll( map );
                        return (V) targetMap;
                    }
                    catch ( NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e ) {
                        if( this.mValueType.isAssignableFrom( val.getClass() ) ) {
                            return (V) map;
                        }
                        throw new ProxyProvokeHandleException( "Error converting to target Map type.", e );
                    }
                }
            }

            // Handling Bean scenarios
            try {
                Constructor<V >  constructor = this.mValueType.getDeclaredConstructor();
                constructor.setAccessible( true );  // [NOTICE] Set the constructor accessible
                V bean = constructor.newInstance();

                for ( Map.Entry<String, Object > entry : map.entrySet() ) {
                    try{
                        String property = entry.getKey();
                        Object value = entry.getValue();
                        this.setBeanProperty( bean, property, value );
                    }
                    catch ( IntrospectionException | InvocationTargetException | IllegalAccessException e ) {
                        e.printStackTrace();
                        // continue
                    }
                }
                return bean;
            }
            catch ( NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e ) {
                throw new ProxyProvokeHandleException("Error converting to target Bean type", e);
            }
        }
        else if( val != null && ResultConverter.isPrimitiveOrSpecialType( val.getClass() )  ){
            if ( ResultConverter.isPrimitiveOrSpecialType( this.mValueType ) ) {
                return (V) this.convertToType( val, this.mValueType );
            }
            if( this.mValueType.equals( Object.class ) ){
                return (V) val;
            }
        }
        else if( this.mValueType.equals( Object.class ) ){
            return (V) val;
        }

        throw new IllegalArgumentException( "Unsupported conversion from value: " + val );
    }

    private Object convertToType( Object value, Class<?> type ) {
        if ( value == null ) {
            return null;
        }
        if ( type.isInstance( value ) ) {
            return type.cast( value );
        }

        if ( type == String.class ) {
            return value.toString();
        }
        else if ( type == int.class || type == Integer.class ) {
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            else {
                return Integer.parseInt(value.toString());
            }
        }
        else if ( type == long.class || type == Long.class ) {
            if (value instanceof Number) {
                return ((Number) value).longValue();
            }
            else {
                return Long.parseLong(value.toString());
            }
        }
        else if ( type == double.class || type == Double.class ) {
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            } else {
                return Double.parseDouble(value.toString());
            }
        }
        else if ( type == boolean.class || type == Boolean.class ) {
            if (value instanceof Boolean) {
                return value;
            } else {
                return Boolean.parseBoolean(value.toString());
            }
        }
        else if ( type == byte.class || type == Byte.class ) {
            if (value instanceof Number) {
                return ((Number) value).byteValue();
            } else {
                return Byte.parseByte(value.toString());
            }
        }
        else if ( type == short.class || type == Short.class ) {
            if (value instanceof Number) {
                return ((Number) value).shortValue();
            } else {
                return Short.parseShort(value.toString());
            }
        }
        else if ( type == float.class || type == Float.class ) {
            if (value instanceof Number) {
                return ((Number) value).floatValue();
            } else {
                return Float.parseFloat(value.toString());
            }
        }
        else if ( type == char.class || type == Character.class ) {
            return value.toString().charAt(0);
        }
        else if ( type == Date.class ) {
            if ( value instanceof Date ) {
                return value;
            }
            else {
                try {
                    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value.toString());
                }
                catch (ParseException e) {
                    throw new IllegalArgumentException("Cannot parse date: " + value, e);
                }
            }
        }
        else if ( type.isEnum() ) {
            return Enum.valueOf((Class<Enum>) type, value.toString());
        }
        else if ( type == byte[].class ) {
            if ( value instanceof byte[] ) {
                return value;
            }
            else {
                return value.toString().getBytes();
            }
        }
        else {
            throw new IllegalArgumentException( "Cannot convert value to type: " + type );
        }
    }

    private void setBeanProperty( Object bean, String property, Object value ) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        java.beans.PropertyDescriptor propertyDescriptor = new java.beans.PropertyDescriptor( property, bean.getClass() );
        Method writeMethod = propertyDescriptor.getWriteMethod();
        if ( writeMethod != null ) {
            writeMethod.setAccessible( true );
            writeMethod.invoke( bean, value );
        }
    }
}