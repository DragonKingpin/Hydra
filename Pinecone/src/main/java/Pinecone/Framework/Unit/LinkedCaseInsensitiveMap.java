package Pinecone.Framework.Unit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

public class LinkedCaseInsensitiveMap<V> extends LinkedHashMap<String, V> {
    private final Map<String, String> caseInsensitiveKeys;
    private final Locale locale;

    public LinkedCaseInsensitiveMap() {
        this((Locale)null);
    }

    public LinkedCaseInsensitiveMap(Locale locale) {
        this.caseInsensitiveKeys = new HashMap();
        this.locale = locale != null ? locale : Locale.getDefault();
    }

    public LinkedCaseInsensitiveMap(int initialCapacity) {
        this(initialCapacity, (Locale)null);
    }

    public LinkedCaseInsensitiveMap(int initialCapacity, Locale locale) {
        super(initialCapacity);
        this.caseInsensitiveKeys = new HashMap<>(initialCapacity);
        this.locale = locale != null ? locale : Locale.getDefault();
    }

    public V put(String key, V value) {
        String oldKey = (String)this.caseInsensitiveKeys.put(this.convertKey(key), key);
        if (oldKey != null && !oldKey.equals(key)) {
            super.remove(oldKey);
        }

        return super.put(key, value);
    }

    public void putAll(Map<? extends String, ? extends V> map) {
        if (!map.isEmpty()) {
            Iterator var2 = map.entrySet().iterator();

            while(var2.hasNext()) {
                Entry<? extends String, ? extends V> entry = (Entry)var2.next();
                this.put((String)entry.getKey(), entry.getValue());
            }

        }
    }

    public boolean containsKey(Object key) {
        return key instanceof String && this.caseInsensitiveKeys.containsKey(this.convertKey((String)key));
    }

    public V get(Object key) {
        return key instanceof String ? super.get(this.caseInsensitiveKeys.get(this.convertKey((String)key))) : null;
    }

    public V remove(Object key) {
        return key instanceof String ? super.remove(this.caseInsensitiveKeys.remove(this.convertKey((String)key))) : null;
    }

    public void clear() {
        this.caseInsensitiveKeys.clear();
        super.clear();
    }

    protected String convertKey(String key) {
        return key.toLowerCase(this.locale);
    }
}
