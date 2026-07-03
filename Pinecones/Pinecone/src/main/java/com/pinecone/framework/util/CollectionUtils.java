package com.pinecone.framework.util;

import com.pinecone.framework.system.Unsafe;
import com.pinecone.framework.unit.AbstractMultiValueMap;
import com.pinecone.framework.unit.MultiValueMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

public final class CollectionUtils {
    private CollectionUtils() {
    }

    public static boolean isEmpty( Collection<?> collection ) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNoneEmpty( Collection<?> collection ) {
        return !CollectionUtils.isEmpty( collection );
    }

    public static boolean isEmpty( Map<?, ?> map ) {
        return map == null || map.isEmpty();
    }

    public static boolean isNoneEmpty( Map<?, ?> map ) {
        return !CollectionUtils.isEmpty( map );
    }

    @SuppressWarnings( "rawtypes" )
    public static List arrayToList( Object source ) {
        return Arrays.asList( ObjectUtils.toObjectArray( source ) );
    }

    @SuppressWarnings( "unchecked" )
    public static <E> void mergeArrayIntoCollection( Object array, Collection<E> collection ) {
        if ( collection == null ) {
            throw new IllegalArgumentException( "Collection must not be null" );
        }

        Object[] arr = ObjectUtils.toObjectArray( array );
        for ( Object elem : arr ) {
            collection.add( (E) elem );
        }
    }

    public static <K, V> void mergePropertiesIntoMap( Properties props, Map<String, Object> map ) {
        if ( map == null ) {
            throw new IllegalArgumentException( "Map must not be null" );
        }

        if ( props != null ) {
            for ( Enumeration<?> en = props.propertyNames(); en.hasMoreElements(); ) {
                String key = (String) en.nextElement();
                Object value = props.getProperty( key );
                if ( value == null ) {
                    value = props.get( key );
                }
                map.put( key, value );
            }
        }
    }

    public static boolean contains( Iterator<?> iterator, Object element ) {
        if ( iterator != null ) {
            while ( iterator.hasNext() ) {
                Object candidate = iterator.next();
                if ( ObjectUtils.nullSafeEquals( candidate, element ) ) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean contains( Enumeration<?> enumeration, Object element ) {
        if ( enumeration != null ) {
            while ( enumeration.hasMoreElements() ) {
                Object candidate = enumeration.nextElement();
                if ( ObjectUtils.nullSafeEquals( candidate, element ) ) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean containsInstance( Collection<?> collection, Object element ) {
        if ( collection != null ) {
            for ( Object candidate : collection ) {
                if ( candidate == element ) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean containsAny( Collection<?> source, Collection<?> candidates ) {
        if ( isEmpty( source ) || isEmpty( candidates ) ) {
            return false;
        }

        for ( Object candidate : candidates ) {
            if ( source.contains( candidate ) ) {
                return true;
            }
        }

        return false;
    }

    public static <E> E findFirstMatch( Collection<?> source, Collection<E> candidates ) {
        if ( isEmpty( source ) || isEmpty( candidates ) ) {
            return null;
        }

        for ( E candidate : candidates ) {
            if ( source.contains( candidate ) ) {
                return candidate;
            }
        }

        return null;
    }

    @SuppressWarnings( "unchecked" )
    public static <T> T findValueOfType( Collection<?> collection, Class<T> type ) {
        if ( isEmpty( collection ) ) {
            return null;
        }

        T value = null;
        for ( Object element : collection ) {
            if ( type != null && !type.isInstance( element ) ) {
                continue;
            }

            if ( value != null ) {
                return null;
            }

            value = (T) element;
        }

        return value;
    }

    public static Object findValueOfType( Collection<?> collection, Class<?>[] types ) {
        if ( isEmpty( collection ) || ObjectUtils.isEmpty( types ) ) {
            return null;
        }

        for ( Class<?> type : types ) {
            Object value = findValueOfType( collection, type );
            if ( value != null ) {
                return value;
            }
        }

        return null;
    }

    public static boolean hasUniqueObject( Collection<?> collection ) {
        if ( isEmpty( collection ) ) {
            return false;
        }

        boolean hasCandidate = false;
        Object candidate = null;
        for ( Object elem : collection ) {
            if ( !hasCandidate ) {
                hasCandidate = true;
                candidate = elem;
            }
            else if ( candidate != elem ) {
                return false;
            }
        }

        return true;
    }

    public static Class<?> findCommonElementType( Collection<?> collection ) {
        if ( isEmpty( collection ) ) {
            return null;
        }

        Class<?> candidate = null;
        for ( Object val : collection ) {
            if ( val != null ) {
                if ( candidate == null ) {
                    candidate = val.getClass();
                }
                else if ( candidate != val.getClass() ) {
                    return null;
                }
            }
        }

        return candidate;
    }

    public static <A, E extends A> A[] toArray( Enumeration<E> enumeration, A[] array ) {
        ArrayList<E> elements = new ArrayList<>();

        while ( enumeration.hasMoreElements() ) {
            elements.add( enumeration.nextElement() );
        }

        return elements.toArray( array );
    }

    public static <E> Iterator<E> toIterator( Enumeration<E> enumeration ) {
        return new CollectionUtils.EnumerationIterator<>( enumeration );
    }

    public static <K, V> MultiValueMap<K, V> toMultiValueMap( Map<K, List<V>> map ) {
        return new CollectionUtils.MultiValueMapAdapter<>( map );
    }

    @SuppressWarnings( "unchecked" )
    public static <K, V> MultiValueMap<K, V> unmodifiableMultiValueMap( MultiValueMap<? extends K, ? extends V> map ) {
        Assert.notNull( map, "'map' must not be null" );
        Map<K, List<V>> result = new LinkedHashMap<>( map.size() );

        for ( Entry<? extends K, ? extends List<? extends V>> entry : map.entrySet() ) {
            List<V> values = Collections.unmodifiableList( (List<V>) entry.getValue() );
            result.put( entry.getKey(), values );
        }

        Map<K, List<V>> unmodifiableMap = Collections.unmodifiableMap( result );
        return toMultiValueMap( unmodifiableMap );
    }

    private static class MultiValueMapAdapter<K, V> extends AbstractMultiValueMap<K, V> implements MultiValueMap<K, V>, Serializable {
        private final Map<K, List<V>> map;

        public MultiValueMapAdapter( Map<K, List<V>> map ) {
            Assert.notNull( map, "'map' must not be null" );
            this.map = map;
        }

        public V add( K key, V value ) {
            List<V> values = this.map.get( key );
            if ( values == null ) {
                values = new LinkedList<>();
                this.map.put( key, values );
            }

            values.add( value );
            return value;
        }

        public V getFirst( K key ) {
            List<V> values = this.map.get( key );
            return values != null ? values.get( 0 ) : null;
        }

        public V set( K key, V value ) {
            List<V> values = new LinkedList<>();
            values.add( value );
            this.map.put( key, values );
            return value;
        }

        public void setAll( Map<K, V> values ) {
            for ( Entry<K, V> entry : values.entrySet() ) {
                this.set( entry.getKey(), entry.getValue() );
            }
        }

        public Map<K, V> toSingleValueMap() {
            LinkedHashMap<K, V> singleValueMap = new LinkedHashMap<>( this.map.size() );
            for ( Entry<K, List<V>> entry : this.map.entrySet() ) {
                singleValueMap.put( entry.getKey(), entry.getValue().get( 0 ) );
            }

            return singleValueMap;
        }

        public int size() {
            return this.map.size();
        }

        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        public boolean containsKey( Object key ) {
            return this.map.containsKey( key );
        }

        public boolean containsValue( Object value ) {
            return this.map.containsValue( value );
        }

        public List<V> get( Object key ) {
            return this.map.get( key );
        }

        public List<V> put( K key, List<V> value ) {
            return this.map.put( key, value );
        }

        public List<V> remove( Object key ) {
            return this.map.remove( key );
        }

        public void putAll( Map<? extends K, ? extends List<V>> m ) {
            this.map.putAll( m );
        }

        public void clear() {
            this.map.clear();
        }

        public Set<K> keySet() {
            return this.map.keySet();
        }

        public Collection<List<V>> values() {
            return this.map.values();
        }

        public Set<Entry<K, List<V>>> entrySet() {
            return this.map.entrySet();
        }

        public boolean equals( Object other ) {
            return this == other ? true : this.map.equals( other );
        }

        public int hashCode() {
            return this.map.hashCode();
        }

        public String toString() {
            return this.map.toString();
        }
    }

    private static class EnumerationIterator<E> implements Iterator<E> {
        private final Enumeration<E> enumeration;

        public EnumerationIterator( Enumeration<E> enumeration ) {
            this.enumeration = enumeration;
        }

        public boolean hasNext() {
            return this.enumeration.hasMoreElements();
        }

        public E next() {
            return this.enumeration.nextElement();
        }

        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException( "Not supported" );
        }
    }

    @Unsafe
    @SuppressWarnings( "unchecked" )
    public static <T> List<T> genericConvert( List list ) {
        return (List<T>) list;
    }

    @Unsafe
    @SuppressWarnings( "unchecked" )
    public static <T> Collection<T> genericConvert( Collection collection ) {
        return (Collection<T>) collection;
    }

    @Unsafe
    @SuppressWarnings( "unchecked" )
    public static <K, V> Map<K, V> genericConvert( Map map ) {
        return (Map<K, V>) map;
    }

}
