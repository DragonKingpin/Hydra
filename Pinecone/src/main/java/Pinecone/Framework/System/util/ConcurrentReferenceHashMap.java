package Pinecone.Framework.System.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentReferenceHashMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
    private static final ConcurrentReferenceHashMap.ReferenceType DEFAULT_REFERENCE_TYPE;
    private static final int MAXIMUM_CONCURRENCY_LEVEL = 65536;
    private static final int MAXIMUM_SEGMENT_SIZE = 1073741824;
    private final ConcurrentReferenceHashMap<K, V>.Segment[] segments;
    private final float loadFactor;
    private final ConcurrentReferenceHashMap.ReferenceType referenceType;
    private final int shift;
    private Set<java.util.Map.Entry<K, V>> entrySet;

    public ConcurrentReferenceHashMap() {
        this(16, 0.75F, 16, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity) {
        this(initialCapacity, 0.75F, 16, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor) {
        this(initialCapacity, loadFactor, 16, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, int concurrencyLevel) {
        this(initialCapacity, 0.75F, concurrencyLevel, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, ConcurrentReferenceHashMap.ReferenceType referenceType) {
        this(initialCapacity, 0.75F, 16, referenceType);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        this(initialCapacity, loadFactor, concurrencyLevel, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel, ConcurrentReferenceHashMap.ReferenceType referenceType) {
        Assert.isTrue(initialCapacity >= 0, "Initial capacity must not be negative");
        Assert.isTrue(loadFactor > 0.0F, "Load factor must be positive");
        Assert.isTrue(concurrencyLevel > 0, "Concurrency level must be positive");
        Assert.notNull(referenceType, "Reference type must not be null");
        this.loadFactor = loadFactor;
        this.shift = calculateShift(concurrencyLevel, 65536);
        int size = 1 << this.shift;
        this.referenceType = referenceType;
        int roundedUpSegmentCapacity = (int)(((long)(initialCapacity + size) - 1L) / (long)size);
        this.segments = (ConcurrentReferenceHashMap.Segment[])((ConcurrentReferenceHashMap.Segment[])Array.newInstance(ConcurrentReferenceHashMap.Segment.class, size));

        for(int i = 0; i < this.segments.length; ++i) {
            this.segments[i] = new ConcurrentReferenceHashMap.Segment(roundedUpSegmentCapacity);
        }

    }

    protected final float getLoadFactor() {
        return this.loadFactor;
    }

    protected final int getSegmentsSize() {
        return this.segments.length;
    }

    protected final ConcurrentReferenceHashMap<K, V>.Segment getSegment(int index) {
        return this.segments[index];
    }

    protected ConcurrentReferenceHashMap<K, V>.ReferenceManager createReferenceManager() {
        return new ConcurrentReferenceHashMap.ReferenceManager();
    }

    protected int getHash(Object o) {
        int hash = o == null ? 0 : o.hashCode();
        hash += hash << 15 ^ -12931;
        hash ^= hash >>> 10;
        hash += hash << 3;
        hash ^= hash >>> 6;
        hash += (hash << 2) + (hash << 14);
        hash ^= hash >>> 16;
        return hash;
    }

    public V get(Object key) {
        ConcurrentReferenceHashMap.Reference<K, V> reference = this.getReference(key, ConcurrentReferenceHashMap.Restructure.WHEN_NECESSARY);
        ConcurrentReferenceHashMap.Entry<K, V> entry = reference != null ? reference.get() : null;
        return entry != null ? entry.getValue() : null;
    }

    public boolean containsKey(Object key) {
        ConcurrentReferenceHashMap.Reference<K, V> reference = this.getReference(key, ConcurrentReferenceHashMap.Restructure.WHEN_NECESSARY);
        ConcurrentReferenceHashMap.Entry<K, V> entry = reference != null ? reference.get() : null;
        return entry != null && ObjectUtils.nullSafeEquals(entry.getKey(), key);
    }

    protected final ConcurrentReferenceHashMap.Reference<K, V> getReference(Object key, ConcurrentReferenceHashMap.Restructure restructure) {
        int hash = this.getHash(key);
        return this.getSegmentForHash(hash).getReference(key, hash, restructure);
    }

    public V put(K key, V value) {
        return this.put(key, value, true);
    }

    public V putIfAbsent(K key, V value) {
        return this.put(key, value, false);
    }

    private V put(K key, final V value, final boolean overwriteExisting) {
        return this.doTask(key, new ConcurrentReferenceHashMap<K, V>.Task<V>(new ConcurrentReferenceHashMap.TaskOption[]{ConcurrentReferenceHashMap.TaskOption.RESTRUCTURE_BEFORE, ConcurrentReferenceHashMap.TaskOption.RESIZE}) {
            protected V execute(ConcurrentReferenceHashMap.Reference<K, V> reference, ConcurrentReferenceHashMap.Entry<K, V> entry, ConcurrentReferenceHashMap<K, V>.Entries entries) {
                if (entry != null) {
                    V previousValue = entry.getValue();
                    if (overwriteExisting) {
                        entry.setValue(value);
                    }

                    return previousValue;
                } else {
                    entries.add(value);
                    return null;
                }
            }
        });
    }

    public V remove(Object key) {
        return this.doTask(key, new ConcurrentReferenceHashMap<K, V>.Task<V>(new ConcurrentReferenceHashMap.TaskOption[]{ConcurrentReferenceHashMap.TaskOption.RESTRUCTURE_AFTER, ConcurrentReferenceHashMap.TaskOption.SKIP_IF_EMPTY}) {
            protected V execute(ConcurrentReferenceHashMap.Reference<K, V> reference, ConcurrentReferenceHashMap.Entry<K, V> entry) {
                if (entry != null) {
                    reference.release();
                    return entry.value;
                } else {
                    return null;
                }
            }
        });
    }

    public boolean remove(Object key, final Object value) {
        return (Boolean)this.doTask(key, new ConcurrentReferenceHashMap<K, V>.Task<Boolean>(new ConcurrentReferenceHashMap.TaskOption[]{ConcurrentReferenceHashMap.TaskOption.RESTRUCTURE_AFTER, ConcurrentReferenceHashMap.TaskOption.SKIP_IF_EMPTY}) {
            protected Boolean execute(ConcurrentReferenceHashMap.Reference<K, V> reference, ConcurrentReferenceHashMap.Entry<K, V> entry) {
                if (entry != null && ObjectUtils.nullSafeEquals(entry.getValue(), value)) {
                    reference.release();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    public boolean replace(K key, final V oldValue, final V newValue) {
        return (Boolean)this.doTask(key, new ConcurrentReferenceHashMap<K, V>.Task<Boolean>(new ConcurrentReferenceHashMap.TaskOption[]{ConcurrentReferenceHashMap.TaskOption.RESTRUCTURE_BEFORE, ConcurrentReferenceHashMap.TaskOption.SKIP_IF_EMPTY}) {
            protected Boolean execute(ConcurrentReferenceHashMap.Reference<K, V> reference, ConcurrentReferenceHashMap.Entry<K, V> entry) {
                if (entry != null && ObjectUtils.nullSafeEquals(entry.getValue(), oldValue)) {
                    entry.setValue(newValue);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    public V replace(K key, final V value) {
        return this.doTask(key, new ConcurrentReferenceHashMap<K, V>.Task<V>(new ConcurrentReferenceHashMap.TaskOption[]{ConcurrentReferenceHashMap.TaskOption.RESTRUCTURE_BEFORE, ConcurrentReferenceHashMap.TaskOption.SKIP_IF_EMPTY}) {
            protected V execute(ConcurrentReferenceHashMap.Reference<K, V> reference, ConcurrentReferenceHashMap.Entry<K, V> entry) {
                if (entry != null) {
                    V previousValue = entry.getValue();
                    entry.setValue(value);
                    return previousValue;
                } else {
                    return null;
                }
            }
        });
    }

    public void clear() {
        ConcurrentReferenceHashMap.Segment[] var1 = this.segments;
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ConcurrentReferenceHashMap<K, V>.Segment segment = var1[var3];
            segment.clear();
        }

    }

    public void purgeUnreferencedEntries() {
        ConcurrentReferenceHashMap.Segment[] var1 = this.segments;
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ConcurrentReferenceHashMap<K, V>.Segment segment = var1[var3];
            segment.restructureIfNecessary(false);
        }

    }

    public int size() {
        int size = 0;
        ConcurrentReferenceHashMap.Segment[] var2 = this.segments;
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            ConcurrentReferenceHashMap<K, V>.Segment segment = var2[var4];
            size += segment.getCount();
        }

        return size;
    }

    public Set<java.util.Map.Entry<K, V>> entrySet() {
        if (this.entrySet == null) {
            this.entrySet = new ConcurrentReferenceHashMap.EntrySet();
        }

        return this.entrySet;
    }

    private <T> T doTask(Object key, ConcurrentReferenceHashMap<K, V>.Task<T> task) {
        int hash = this.getHash(key);
        return this.getSegmentForHash(hash).doTask(hash, key, task);
    }

    private ConcurrentReferenceHashMap<K, V>.Segment getSegmentForHash(int hash) {
        return this.segments[hash >>> 32 - this.shift & this.segments.length - 1];
    }

    protected static int calculateShift(int minimumValue, int maximumValue) {
        int shift = 0;

        for(int value = 1; value < minimumValue && value < maximumValue; ++shift) {
            value <<= 1;
        }

        return shift;
    }

    static {
        DEFAULT_REFERENCE_TYPE = ConcurrentReferenceHashMap.ReferenceType.SOFT;
    }

    private static final class WeakEntryReference<K, V> extends WeakReference<ConcurrentReferenceHashMap.Entry<K, V>> implements ConcurrentReferenceHashMap.Reference<K, V> {
        private final int hash;
        private final ConcurrentReferenceHashMap.Reference<K, V> nextReference;

        public WeakEntryReference(ConcurrentReferenceHashMap.Entry<K, V> entry, int hash, ConcurrentReferenceHashMap.Reference<K, V> next, ReferenceQueue<ConcurrentReferenceHashMap.Entry<K, V>> queue) {
            super(entry, queue);
            this.hash = hash;
            this.nextReference = next;
        }

        public int getHash() {
            return this.hash;
        }

        public ConcurrentReferenceHashMap.Reference<K, V> getNext() {
            return this.nextReference;
        }

        public void release() {
            this.enqueue();
            this.clear();
        }
    }

    private static final class SoftEntryReference<K, V> extends SoftReference<ConcurrentReferenceHashMap.Entry<K, V>> implements ConcurrentReferenceHashMap.Reference<K, V> {
        private final int hash;
        private final ConcurrentReferenceHashMap.Reference<K, V> nextReference;

        public SoftEntryReference(ConcurrentReferenceHashMap.Entry<K, V> entry, int hash, ConcurrentReferenceHashMap.Reference<K, V> next, ReferenceQueue<ConcurrentReferenceHashMap.Entry<K, V>> queue) {
            super(entry, queue);
            this.hash = hash;
            this.nextReference = next;
        }

        public int getHash() {
            return this.hash;
        }

        public ConcurrentReferenceHashMap.Reference<K, V> getNext() {
            return this.nextReference;
        }

        public void release() {
            this.enqueue();
            this.clear();
        }
    }

    protected class ReferenceManager {
        private final ReferenceQueue<ConcurrentReferenceHashMap.Entry<K, V>> queue = new ReferenceQueue();

        protected ReferenceManager() {
        }

        public ConcurrentReferenceHashMap.Reference<K, V> createReference(ConcurrentReferenceHashMap.Entry<K, V> entry, int hash, ConcurrentReferenceHashMap.Reference<K, V> next) {
            return (ConcurrentReferenceHashMap.Reference)(ConcurrentReferenceHashMap.this.referenceType == ConcurrentReferenceHashMap.ReferenceType.WEAK ? new ConcurrentReferenceHashMap.WeakEntryReference(entry, hash, next, this.queue) : new ConcurrentReferenceHashMap.SoftEntryReference(entry, hash, next, this.queue));
        }

        public ConcurrentReferenceHashMap.Reference<K, V> pollForPurge() {
            return (ConcurrentReferenceHashMap.Reference)this.queue.poll();
        }
    }

    protected static enum Restructure {
        WHEN_NECESSARY,
        NEVER;

        private Restructure() {
        }
    }

    private class EntryIterator implements Iterator<java.util.Map.Entry<K, V>> {
        private int segmentIndex;
        private int referenceIndex;
        private ConcurrentReferenceHashMap.Reference<K, V>[] references;
        private ConcurrentReferenceHashMap.Reference<K, V> reference;
        private ConcurrentReferenceHashMap.Entry<K, V> next;
        private ConcurrentReferenceHashMap.Entry<K, V> last;

        public EntryIterator() {
            this.moveToNextSegment();
        }

        public boolean hasNext() {
            this.getNextIfNecessary();
            return this.next != null;
        }

        public ConcurrentReferenceHashMap.Entry<K, V> next() {
            this.getNextIfNecessary();
            if (this.next == null) {
                throw new NoSuchElementException();
            } else {
                this.last = this.next;
                this.next = null;
                return this.last;
            }
        }

        private void getNextIfNecessary() {
            while(this.next == null) {
                this.moveToNextReference();
                if (this.reference == null) {
                    return;
                }

                this.next = this.reference.get();
            }

        }

        private void moveToNextReference() {
            if (this.reference != null) {
                this.reference = this.reference.getNext();
            }

            while(this.reference == null && this.references != null) {
                if (this.referenceIndex >= this.references.length) {
                    this.moveToNextSegment();
                    this.referenceIndex = 0;
                } else {
                    this.reference = this.references[this.referenceIndex];
                    ++this.referenceIndex;
                }
            }

        }

        private void moveToNextSegment() {
            this.reference = null;
            this.references = null;
            if (this.segmentIndex < ConcurrentReferenceHashMap.this.segments.length) {
                this.references = ConcurrentReferenceHashMap.this.segments[this.segmentIndex].references;
                ++this.segmentIndex;
            }

        }

        public void remove() {
            Assert.state(this.last != null);
            ConcurrentReferenceHashMap.this.remove(this.last.getKey());
        }
    }

    private class EntrySet extends AbstractSet<java.util.Map.Entry<K, V>> {
        private EntrySet() {
        }

        public Iterator<java.util.Map.Entry<K, V>> iterator() {
            return ConcurrentReferenceHashMap.this.new EntryIterator();
        }

        public boolean contains(Object o) {
            if (o != null && o instanceof java.util.Map.Entry) {
                java.util.Map.Entry<?, ?> entry = (java.util.Map.Entry)o;
                ConcurrentReferenceHashMap.Reference<K, V> reference = ConcurrentReferenceHashMap.this.getReference(entry.getKey(), ConcurrentReferenceHashMap.Restructure.NEVER);
                ConcurrentReferenceHashMap.Entry<K, V> other = reference != null ? reference.get() : null;
                if (other != null) {
                    return ObjectUtils.nullSafeEquals(entry.getValue(), other.getValue());
                }
            }

            return false;
        }

        public boolean remove(Object o) {
            if (o instanceof java.util.Map.Entry) {
                java.util.Map.Entry<?, ?> entry = (java.util.Map.Entry)o;
                return ConcurrentReferenceHashMap.this.remove(entry.getKey(), entry.getValue());
            } else {
                return false;
            }
        }

        public int size() {
            return ConcurrentReferenceHashMap.this.size();
        }

        public void clear() {
            ConcurrentReferenceHashMap.this.clear();
        }
    }

    private abstract class Entries {
        private Entries() {
        }

        public abstract void add(V var1);
    }

    private static enum TaskOption {
        RESTRUCTURE_BEFORE,
        RESTRUCTURE_AFTER,
        SKIP_IF_EMPTY,
        RESIZE;

        private TaskOption() {
        }
    }

    private abstract class Task<T> {
        private final EnumSet<ConcurrentReferenceHashMap.TaskOption> options;

        public Task(ConcurrentReferenceHashMap.TaskOption... options) {
            this.options = options.length == 0 ? EnumSet.noneOf(ConcurrentReferenceHashMap.TaskOption.class) : EnumSet.of(options[0], options);
        }

        public boolean hasOption(ConcurrentReferenceHashMap.TaskOption option) {
            return this.options.contains(option);
        }

        protected T execute(ConcurrentReferenceHashMap.Reference<K, V> reference, ConcurrentReferenceHashMap.Entry<K, V> entry, ConcurrentReferenceHashMap<K, V>.Entries entries) {
            return this.execute(reference, entry);
        }

        protected T execute(ConcurrentReferenceHashMap.Reference<K, V> reference, ConcurrentReferenceHashMap.Entry<K, V> entry) {
            return null;
        }
    }

    protected static final class Entry<K, V> implements java.util.Map.Entry<K, V> {
        private final K key;
        private volatile V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public V setValue(V value) {
            V previous = this.value;
            this.value = value;
            return previous;
        }

        public String toString() {
            return this.key + "=" + this.value;
        }

        public final boolean equals(Object other) {
            if (this == other) {
                return true;
            } else if (!(other instanceof java.util.Map.Entry)) {
                return false;
            } else {
                java.util.Map.Entry otherEntry = (java.util.Map.Entry)other;
                return ObjectUtils.nullSafeEquals(this.getKey(), otherEntry.getKey()) && ObjectUtils.nullSafeEquals(this.getValue(), otherEntry.getValue());
            }
        }

        public final int hashCode() {
            return ObjectUtils.nullSafeHashCode(this.key) ^ ObjectUtils.nullSafeHashCode(this.value);
        }
    }

    protected interface Reference<K, V> {
        ConcurrentReferenceHashMap.Entry<K, V> get();

        int getHash();

        ConcurrentReferenceHashMap.Reference<K, V> getNext();

        void release();
    }

    protected final class Segment extends ReentrantLock {
        private final ConcurrentReferenceHashMap<K, V>.ReferenceManager referenceManager = ConcurrentReferenceHashMap.this.createReferenceManager();
        private final int initialSize;
        private volatile ConcurrentReferenceHashMap.Reference<K, V>[] references;
        private volatile int count = 0;
        private int resizeThreshold;

        public Segment(int initialCapacity) {
            this.initialSize = 1 << ConcurrentReferenceHashMap.calculateShift(initialCapacity, 1073741824);
            this.setReferences(this.createReferenceArray(this.initialSize));
        }

        public ConcurrentReferenceHashMap.Reference<K, V> getReference(Object key, int hash, ConcurrentReferenceHashMap.Restructure restructure) {
            if (restructure == ConcurrentReferenceHashMap.Restructure.WHEN_NECESSARY) {
                this.restructureIfNecessary(false);
            }

            if (this.count == 0) {
                return null;
            } else {
                ConcurrentReferenceHashMap.Reference<K, V>[] references = this.references;
                int index = this.getIndex(hash, references);
                ConcurrentReferenceHashMap.Reference<K, V> head = references[index];
                return this.findInChain(head, key, hash);
            }
        }

        public <T> T doTask(final int hash, final Object key, ConcurrentReferenceHashMap<K, V>.Task<T> task) {
            boolean resize = task.hasOption(ConcurrentReferenceHashMap.TaskOption.RESIZE);
            if (task.hasOption(ConcurrentReferenceHashMap.TaskOption.RESTRUCTURE_BEFORE)) {
                this.restructureIfNecessary(resize);
            }

            if (task.hasOption(ConcurrentReferenceHashMap.TaskOption.SKIP_IF_EMPTY) && this.count == 0) {
                return (T) task.execute((Reference)null, (Entry)null, (Entries)null);
            } else {
                this.lock();

                Object var10;
                try {
                    final int index = this.getIndex(hash, this.references);
                    final ConcurrentReferenceHashMap.Reference<K, V> head = this.references[index];
                    ConcurrentReferenceHashMap.Reference<K, V> reference = this.findInChain(head, key, hash);
                    ConcurrentReferenceHashMap.Entry<K, V> entry = reference != null ? reference.get() : null;
                    ConcurrentReferenceHashMap<K, V>.Entries entries = new ConcurrentReferenceHashMap<K, V>.Entries() {
                        public void add(V value) {
                            ConcurrentReferenceHashMap.Entry<K, V> newEntry = new ConcurrentReferenceHashMap.Entry(key, value);
                            ConcurrentReferenceHashMap.Reference<K, V> newReference = Segment.this.referenceManager.createReference(newEntry, hash, head);
                            Segment.this.references[index] = newReference;
                            Segment.this.count++;
                        }
                    };
                    var10 = task.execute(reference, entry, entries);
                } finally {
                    this.unlock();
                    if (task.hasOption(ConcurrentReferenceHashMap.TaskOption.RESTRUCTURE_AFTER)) {
                        this.restructureIfNecessary(resize);
                    }

                }

                return (T) var10;
            }
        }

        public void clear() {
            if (this.count != 0) {
                this.lock();

                try {
                    this.setReferences(this.createReferenceArray(this.initialSize));
                    this.count = 0;
                } finally {
                    this.unlock();
                }

            }
        }

        protected final void restructureIfNecessary(boolean allowResize) {
            boolean needsResize = this.count > 0 && this.count >= this.resizeThreshold;
            ConcurrentReferenceHashMap.Reference<K, V> reference = this.referenceManager.pollForPurge();
            if (reference != null || needsResize && allowResize) {
                this.lock();

                try {
                    int countAfterRestructure = this.count;
                    Set<ConcurrentReferenceHashMap.Reference<K, V>> toPurge = Collections.emptySet();
                    if (reference != null) {
                        for(toPurge = new HashSet(); reference != null; reference = this.referenceManager.pollForPurge()) {
                            ((Set)toPurge).add(reference);
                        }
                    }

                    countAfterRestructure -= ((Set)toPurge).size();
                    needsResize = countAfterRestructure > 0 && countAfterRestructure >= this.resizeThreshold;
                    boolean resizing = false;
                    int restructureSize = this.references.length;
                    if (allowResize && needsResize && restructureSize < 1073741824) {
                        restructureSize <<= 1;
                        resizing = true;
                    }

                    ConcurrentReferenceHashMap.Reference<K, V>[] restructured = resizing ? this.createReferenceArray(restructureSize) : this.references;

                    for(int i = 0; i < this.references.length; ++i) {
                        reference = this.references[i];
                        if (!resizing) {
                            restructured[i] = null;
                        }

                        for(; reference != null; reference = reference.getNext()) {
                            if (!((Set)toPurge).contains(reference) && reference.get() != null) {
                                int index = this.getIndex(reference.getHash(), restructured);
                                restructured[index] = this.referenceManager.createReference(reference.get(), reference.getHash(), restructured[index]);
                            }
                        }
                    }

                    if (resizing) {
                        this.setReferences(restructured);
                    }

                    this.count = Math.max(countAfterRestructure, 0);
                } finally {
                    this.unlock();
                }
            }

        }

        private ConcurrentReferenceHashMap.Reference<K, V> findInChain(ConcurrentReferenceHashMap.Reference<K, V> reference, Object key, int hash) {
            for(; reference != null; reference = reference.getNext()) {
                if (reference.getHash() == hash) {
                    ConcurrentReferenceHashMap.Entry<K, V> entry = reference.get();
                    if (entry != null) {
                        K entryKey = entry.getKey();
                        if (entryKey == key || entryKey.equals(key)) {
                            return reference;
                        }
                    }
                }
            }

            return null;
        }

        private ConcurrentReferenceHashMap.Reference<K, V>[] createReferenceArray(int size) {
            return (ConcurrentReferenceHashMap.Reference[])((ConcurrentReferenceHashMap.Reference[])Array.newInstance(ConcurrentReferenceHashMap.Reference.class, size));
        }

        private int getIndex(int hash, ConcurrentReferenceHashMap.Reference<K, V>[] references) {
            return hash & references.length - 1;
        }

        private void setReferences(ConcurrentReferenceHashMap.Reference<K, V>[] references) {
            this.references = references;
            this.resizeThreshold = (int)((float)references.length * ConcurrentReferenceHashMap.this.getLoadFactor());
        }

        public final int getSize() {
            return this.references.length;
        }

        public final int getCount() {
            return this.count;
        }
    }

    public static enum ReferenceType {
        SOFT,
        WEAK;

        private ReferenceType() {
        }
    }
}
