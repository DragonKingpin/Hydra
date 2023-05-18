package Pinecone.Framework.System.util;


import Pinecone.Framework.Unit.MultiValueMap;

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

public abstract class CollectionUtils {
    public CollectionUtils() {
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static List arrayToList(Object source) {
        return Arrays.asList(ObjectUtils.toObjectArray(source));
    }

    public static <E> void mergeArrayIntoCollection(Object array, Collection<E> collection) {
        if (collection == null) {
            throw new IllegalArgumentException("Collection must not be null");
        } else {
            Object[] arr = ObjectUtils.toObjectArray(array);
            Object[] var3 = arr;
            int var4 = arr.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Object elem = var3[var5];
                collection.add((E) elem);
            }

        }
    }

    public static <K, V> void mergePropertiesIntoMap(Properties props, Map<String, Object> map) {
        if (map == null) {
            throw new IllegalArgumentException("Map must not be null");
        } else {
            String key;
            Object value;
            if (props != null) {
                for(Enumeration en = props.propertyNames(); en.hasMoreElements(); map.put(key, value)) {
                    key = (String)en.nextElement();
                    value = props.getProperty(key);
                    if (value == null) {
                        value = props.get(key);
                    }
                }
            }

        }
    }

    public static boolean contains(Iterator<?> iterator, Object element) {
        if (iterator != null) {
            while(iterator.hasNext()) {
                Object candidate = iterator.next();
                if (ObjectUtils.nullSafeEquals(candidate, element)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean contains(Enumeration<?> enumeration, Object element) {
        if (enumeration != null) {
            while(enumeration.hasMoreElements()) {
                Object candidate = enumeration.nextElement();
                if (ObjectUtils.nullSafeEquals(candidate, element)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean containsInstance(Collection<?> collection, Object element) {
        if (collection != null) {
            Iterator var2 = collection.iterator();

            while(var2.hasNext()) {
                Object candidate = var2.next();
                if (candidate == element) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean containsAny(Collection<?> source, Collection<?> candidates) {
        if (!isEmpty(source) && !isEmpty(candidates)) {
            Iterator var2 = candidates.iterator();

            Object candidate;
            do {
                if (!var2.hasNext()) {
                    return false;
                }

                candidate = var2.next();
            } while(!source.contains(candidate));

            return true;
        } else {
            return false;
        }
    }

    public static <E> E findFirstMatch(Collection<?> source, Collection<E> candidates) {
        if (!isEmpty(source) && !isEmpty(candidates)) {
            Iterator var2 = candidates.iterator();

            Object candidate;
            do {
                if (!var2.hasNext()) {
                    return null;
                }

                candidate = var2.next();
            } while(!source.contains(candidate));

            return (E) candidate;
        } else {
            return null;
        }
    }

    public static <T> T findValueOfType(Collection<?> collection, Class<T> type) {
        if (isEmpty(collection)) {
            return null;
        } else {
            T value = null;
            Iterator var3 = collection.iterator();

            while(true) {
                Object element;
                do {
                    if (!var3.hasNext()) {
                        return value;
                    }

                    element = var3.next();
                } while(type != null && !type.isInstance(element));

                if (value != null) {
                    return null;
                }

                value = (T) element;
            }
        }
    }

    public static Object findValueOfType(Collection<?> collection, Class<?>[] types) {
        if (!isEmpty(collection) && !ObjectUtils.isEmpty(types)) {
            Class[] var2 = types;
            int var3 = types.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Class<?> type = var2[var4];
                Object value = findValueOfType(collection, type);
                if (value != null) {
                    return value;
                }
            }

            return null;
        } else {
            return null;
        }
    }

    public static boolean hasUniqueObject(Collection<?> collection) {
        if (isEmpty(collection)) {
            return false;
        } else {
            boolean hasCandidate = false;
            Object candidate = null;
            Iterator var3 = collection.iterator();

            while(var3.hasNext()) {
                Object elem = var3.next();
                if (!hasCandidate) {
                    hasCandidate = true;
                    candidate = elem;
                } else if (candidate != elem) {
                    return false;
                }
            }

            return true;
        }
    }

    public static Class<?> findCommonElementType(Collection<?> collection) {
        if (isEmpty(collection)) {
            return null;
        } else {
            Class<?> candidate = null;
            Iterator var2 = collection.iterator();

            while(var2.hasNext()) {
                Object val = var2.next();
                if (val != null) {
                    if (candidate == null) {
                        candidate = val.getClass();
                    } else if (candidate != val.getClass()) {
                        return null;
                    }
                }
            }

            return candidate;
        }
    }

    public static <A, E extends A> A[] toArray(Enumeration<E> enumeration, A[] array) {
        ArrayList elements = new ArrayList();

        while(enumeration.hasMoreElements()) {
            elements.add(enumeration.nextElement());
        }

        return (A[]) elements.toArray(array);
    }

    public static <E> Iterator<E> toIterator(Enumeration<E> enumeration) {
        return new CollectionUtils.EnumerationIterator(enumeration);
    }

    public static <K, V> MultiValueMap<K, V> toMultiValueMap(Map<K, List<V>> map) {
        return new CollectionUtils.MultiValueMapAdapter(map);
    }

    public static <K, V> MultiValueMap<K, V> unmodifiableMultiValueMap(MultiValueMap<? extends K, ? extends V> map) {
        Assert.notNull(map, "'map' must not be null");
        Map<K, List<V>> result = new LinkedHashMap(map.size());
        Iterator var2 = map.entrySet().iterator();

        while(var2.hasNext()) {
            Entry<? extends K, ? extends List<? extends V>> entry = (Entry)var2.next();
            List<V> values = Collections.unmodifiableList((List)entry.getValue());
            result.put(entry.getKey(), values);
        }

        Map<K, List<V>> unmodifiableMap = Collections.unmodifiableMap(result);
        return toMultiValueMap(unmodifiableMap);
    }

    private static class MultiValueMapAdapter<K, V> implements MultiValueMap<K, V>, Serializable {
        private final Map<K, List<V>> map;

        public MultiValueMapAdapter(Map<K, List<V>> map) {
            Assert.notNull(map, "'map' must not be null");
            this.map = map;
        }

        public void add(K key, V value) {
            List<V> values = (List)this.map.get(key);
            if (values == null) {
                values = new LinkedList();
                this.map.put(key, values);
            }

            ((List)values).add(value);
        }

        public V getFirst(K key) {
            List<V> values = (List)this.map.get(key);
            return values != null ? values.get(0) : null;
        }

        public void set(K key, V value) {
            List<V> values = new LinkedList();
            values.add(value);
            this.map.put(key, values);
        }

        public void setAll(Map<K, V> values) {
            Iterator var2 = values.entrySet().iterator();

            while(var2.hasNext()) {
                Entry<K, V> entry = (Entry)var2.next();
                this.set(entry.getKey(), entry.getValue());
            }

        }

        public Map<K, V> toSingleValueMap() {
            LinkedHashMap<K, V> singleValueMap = new LinkedHashMap(this.map.size());
            Iterator var2 = this.map.entrySet().iterator();

            while(var2.hasNext()) {
                Entry<K, List<V>> entry = (Entry)var2.next();
                singleValueMap.put(entry.getKey(), (V) ((List)entry.getValue()).get(0));
            }

            return singleValueMap;
        }

        public int size() {
            return this.map.size();
        }

        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        public boolean containsKey(Object key) {
            return this.map.containsKey(key);
        }

        public boolean containsValue(Object value) {
            return this.map.containsValue(value);
        }

        public List<V> get(Object key) {
            return (List)this.map.get(key);
        }

        public List<V> put(K key, List<V> value) {
            return (List)this.map.put(key, value);
        }

        public List<V> remove(Object key) {
            return (List)this.map.remove(key);
        }

        public void putAll(Map<? extends K, ? extends List<V>> m) {
            this.map.putAll(m);
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

        public boolean equals(Object other) {
            return this == other ? true : this.map.equals(other);
        }

        public int hashCode() {
            return this.map.hashCode();
        }

        public String toString() {
            return this.map.toString();
        }
    }

    private static class EnumerationIterator<E> implements Iterator<E> {
        private Enumeration<E> enumeration;

        public EnumerationIterator(Enumeration<E> enumeration) {
            this.enumeration = enumeration;
        }

        public boolean hasNext() {
            return this.enumeration.hasMoreElements();
        }

        public E next() {
            return this.enumeration.nextElement();
        }

        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Not supported");
        }
    }
}
