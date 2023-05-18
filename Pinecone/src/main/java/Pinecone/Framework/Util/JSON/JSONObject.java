package Pinecone.Framework.Util.JSON;

import Pinecone.Framework.System.Prototype.PineUnit;
import Pinecone.Framework.System.Prototype.Prototype;
import Pinecone.Framework.System.Prototype.TypeIndex;
import Pinecone.Framework.System.util.StringUtils;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.Map.Entry;

public class JSONObject implements PineUnit, Serializable, Cloneable {
    private Map mMap;
    public static final Object NULL = new JSONObject.Null();

    public JSONObject() {
        this( true );
    }

    public JSONObject( boolean bLinked ){
        this.mMap = bLinked ? new LinkedHashMap() : new HashMap();
    }

    public JSONObject( int nInitialCapacity, boolean bLinked ){
        if ( bLinked ) {
            this.mMap = new LinkedHashMap( nInitialCapacity );
        }
        else {
            this.mMap = new HashMap( nInitialCapacity );
        }
    }

    public JSONObject( JSONObject jo, String[] names ) {
        this();

        for(int i = 0; i < names.length; ++i) {
            try {
                this.putOnce(names[i], jo.opt(names[i]));
            }
            catch (Exception e) {
            }
        }

    }

    public JSONObject( JSONCursorParser x ) throws JSONException {
        this();
        if (x.nextClean() != '{') {
            throw x.syntaxError("A JSONObject text must begin with '{'");
        }
        else {
            while(true) {
                char c = x.nextClean();
                switch(c) {
                    case '\u0000': {
                        throw x.syntaxError("A JSONObject text must end with '}'");
                    }
                    case '}': {
                        return;
                    }
                    default: {
                        x.back();
                        String key = x.nextValue().toString();
                        c = x.nextClean();
                        if (c != ':') {
                            throw x.syntaxError("Expected a ':' after a key");
                        }

                        this.putOnce( key, x.nextValue() );
                        switch ( x.nextClean() ) {
                            case ',':
                            case ';': {
                                if (x.nextClean() == '}') {
                                    return;
                                }

                                x.back();
                                break;
                            }
                            case '}':{
                                return;
                            }
                            default: {
                                throw x.syntaxError("Expected a ',' or '}'");
                            }
                        }
                    }
                }
            }
        }
    }

    public JSONObject( Map map ) {
        this( map,false );
    }

    public JSONObject( Map map, boolean bAssimilateMode ) {
        if( bAssimilateMode ){
            this.mMap = map;
        }
        else {
            this.mMap = new LinkedHashMap();
            if (map != null) {
                for ( Object o : map.entrySet() ) {
                    Entry e = (Entry) o;
                    Object value = e.getValue();
                    if (value != null) {
                        this.mMap.put( e.getKey(), JSONUtils.wrapValue(value) );
                    }
                }
            }
        }
    }

    public JSONObject( Object bean ) {
        this();
        this.populateMap(bean);
    }

    public JSONObject( Object object, String[] names ) {
        this();
        Class c = object.getClass();

        for(int i = 0; i < names.length; ++i) {
            String name = names[i];

            try {
                this.putOpt(name, c.getField(name).get(object));
            }
            catch (Exception e) {
            }
        }

    }

    public JSONObject( String source ) throws JSONException {
        this(new JSONCursorParser(source));
    }

    public JSONObject( String baseName, Locale locale ) throws JSONException {
        this();
        ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale, Thread.currentThread().getContextClassLoader());
        Enumeration keys = bundle.getKeys();

        while(true) {
            Object key;
            do {
                if (!keys.hasMoreElements()) {
                    return;
                }

                key = keys.nextElement();
            } while(!(key instanceof String));

            String[] path = ((String)key).split("\\.");
            int last = path.length - 1;
            JSONObject target = this;

            for(int i = 0; i < last; ++i) {
                String segment = path[i];
                JSONObject nextTarget = target.optJSONObject(segment);
                if (nextTarget == null) {
                    nextTarget = new JSONObject();
                    target.put(segment, (Object)nextTarget);
                }

                target = nextTarget;
            }

            target.put(path[last], (Object)bundle.getString((String)key));
        }
    }


    public void assimilate( Map that ){
        this.mMap = that;
    }

    public JSONObject shareFrom( JSONObject that, String szKey ) {
        this.put( szKey, that.get( szKey ) );
        return this;
    }

    public JSONObject shareFrom( JSONObject that, String[] szKeys ) {
        for ( String szKey : szKeys ) {
            this.putOnce( szKey, that.get( szKey ) );
        }
        return this;
    }

    public JSONObject subJson ( String szKey ) {
        JSONObject that = new JSONObject();
        that.shareFrom( this, szKey );
        return that;
    }

    public JSONObject subJson ( String[] szKeys ) {
        JSONObject that = new JSONObject();
        that.shareFrom( this, szKeys );
        return that;
    }

    public JSONObject detachSub ( String szKey ) {
        JSONObject that = new JSONObject();
        that.put( szKey, this.get( szKey ) );
        this.remove( szKey );
        return that;
    }

    public JSONObject detachSub ( String[] szKeys ) {
        JSONObject that = new JSONObject();
        for ( String szKey : szKeys ) {
            that.putOnce( szKey, this.get( szKey ) );
            this.remove( szKey );
        }
        return that;
    }

    public JSONObject moveSubFrom ( JSONObject that, String szKey ) {
        this.put( szKey, that.get( szKey ) );
        that.remove( szKey );
        return this;
    }

    public JSONObject moveSubFrom ( JSONObject that, String[] szKeys ) {
        for ( String szKey : szKeys ) {
            this.putOnce( szKey, that.get( szKey ) );
            that.remove( szKey );
        }
        return this;
    }



    public Map getMap(){
        return this.mMap;
    }

    public Set entrySet() {
        return this.mMap.entrySet();
    }





    public JSONObject accumulate(String key, Object value) throws JSONException {
        JSONUtils.prospectNumberQualify(value);
        Object object = this.opt(key);
        if (object == null) {
            this.put(key, value instanceof JSONArray ? (new JSONArray()).put(value) : value);
        } else if (object instanceof JSONArray) {
            ((JSONArray)object).put(value);
        } else {
            this.put(key, (Object)(new JSONArray()).put(object).put(value));
        }

        return this;
    }

    public JSONObject append(String key, Object value) throws JSONException {
        JSONUtils.prospectNumberQualify(value);
        Object object = this.opt(key);
        if (object == null) {
            this.put(key, (Object)(new JSONArray()).put(value));
        } else {
            if (!(object instanceof JSONArray)) {
                throw new JSONException("JSONObject[" + key + "] is not a JSONArray.");
            }

            this.put(key, (Object)((JSONArray)object).put(value));
        }

        return this;
    }

    public Object get(String key) throws JSONException {
        if (key == null) {
            throw new JSONException("Null key.");
        } else {
            Object object = this.opt(key);
            if (object == null) {
                throw new JSONException("JSONObject[" + StringUtils.jsonQuote(key) + "] not found.");
            } else {
                return object;
            }
        }
    }

    public boolean getBoolean(String key) throws JSONException {
        Object object = this.get(key);
        if (!object.equals(Boolean.FALSE) && (!(object instanceof String) || !((String)object).equalsIgnoreCase("false"))) {
            if (!object.equals(Boolean.TRUE) && (!(object instanceof String) || !((String)object).equalsIgnoreCase("true"))) {
                throw new JSONException("JSONObject[" + StringUtils.jsonQuote(key) + "] is not a Boolean.");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public double getDouble(String key) throws JSONException {
        Object object = this.get(key);

        try {
            return object instanceof Number ? ((Number)object).doubleValue() : Double.parseDouble((String)object);
        }
        catch (Exception e) {
            throw new JSONException("JSONObject[" + StringUtils.jsonQuote(key) + "] is not a number.");
        }
    }

    public int getInt(String key) throws JSONException {
        Object object = this.get(key);

        try {
            return object instanceof Number ? ((Number)object).intValue() : Integer.parseInt((String)object);
        }
        catch (Exception e) {
            throw new JSONException("JSONObject[" + StringUtils.jsonQuote(key) + "] is not an int.");
        }
    }

    public JSONArray getJSONArray(String key) throws JSONException {
        Object object = this.get(key);
        if (object instanceof JSONArray) {
            return (JSONArray)object;
        } else {
            throw new JSONException("JSONObject[" + StringUtils.jsonQuote(key) + "] is not a JSONArray.");
        }
    }

    public JSONObject getJSONObject(String key) throws JSONException {
        Object object = this.get(key);
        if (object instanceof JSONObject) {
            return (JSONObject)object;
        } else {
            throw new JSONException("JSONObject[" + StringUtils.jsonQuote(key) + "] is not a JSONObject.");
        }
    }

    public long getLong(String key) throws JSONException {
        Object object = this.get(key);

        try {
            return object instanceof Number ? ((Number)object).longValue() : Long.parseLong((String)object);
        }
        catch (Exception e) {
            throw new JSONException("JSONObject[" + StringUtils.jsonQuote(key) + "] is not a long.");
        }
    }

    public String getString(String key) throws JSONException {
        Object object = this.get(key);
        if (object instanceof String) {
            return (String)object;
        } else {
            throw new JSONException("JSONObject[" + StringUtils.jsonQuote(key) + "] not a string.");
        }
    }

    public JSONObject increment(String key) throws JSONException {
        Object value = this.opt(key);
        if (value == null) {
            this.put(key, 1);
        }
        else if (value instanceof Integer) {
            this.put(key, (Integer)value + 1);
        }
        else if (value instanceof Long) {
            this.put(key, (Long)value + 1L);
        }
        else if (value instanceof Double) {
            this.put(key, (Double)value + 1.0D);
        }
        else {
            if (!(value instanceof Float)) {
                throw new JSONException("Unable to increment [" + StringUtils.jsonQuote(key) + "].");
            }

            this.put(key, (double)((Float)value + 1.0F));
        }

        return this;
    }

    public boolean isNull(String key) {
        return NULL.equals(this.opt(key));
    }

    public Iterator keys() {
        return this.keySet().iterator();
    }

    public Set keySet() {
        return this.mMap.keySet();
    }

    public int size() {
        return this.mMap.size();
    }

    public boolean isEmpty() {
        return this.mMap.isEmpty();
    }

    public void clear(){
        this.mMap.clear();
    }

    public JSONArray names() {
        JSONArray ja = new JSONArray();
        Iterator keys = this.keys();

        while(keys.hasNext()) {
            ja.put(keys.next());
        }

        return ja.length() == 0 ? null : ja;
    }

    public String[] getOwnPropertyNames () {
        return JSONUtils.getOwnPropertyNames( this );
    }


    public JSONArray affirmArray( String key ) {
        Object o = this.opt(key);
        if( o instanceof JSONArray ){
            return (JSONArray)o;
        }
        JSONArray jNew = new JSONArray();
        this.put( key, jNew );
        return jNew;
    }

    public JSONObject affirmObject( String key ) {
        Object o = this.opt(key);
        if( o instanceof JSONObject ){
            return (JSONObject) o;
        }
        JSONObject jNew = new JSONObject();
        this.put( key, jNew );
        return jNew;
    }


    public Object opt(String key) {
        return key == null ? null : this.mMap.get(key);
    }

    public boolean optBoolean(String key) {
        return this.optBoolean(key, false);
    }

    public boolean optBoolean(String key, boolean defaultValue) {
        try {
            return this.getBoolean(key);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public double optDouble(String key) {
        return this.optDouble(key, 0.0D / 0.0);
    }

    public double optDouble(String key, double defaultValue) {
        try {
            return this.getDouble(key);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public int optInt(String key) {
        return this.optInt(key, 0);
    }

    public int optInt(String key, int defaultValue) {
        try {
            return this.getInt(key);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public JSONArray optJSONArray(String key) {
        Object o = this.opt(key);
        return o instanceof JSONArray ? (JSONArray)o : null;
    }

    public JSONObject optJSONObject(String key) {
        Object object = this.opt(key);
        return object instanceof JSONObject ? (JSONObject)object : null;
    }

    public long optLong(String key) {
        return this.optLong(key, 0L);
    }

    public long optLong(String key, long defaultValue) {
        try {
            return this.getLong(key);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public String optString(String key) {
        return this.optString(key, "");
    }

    public String optString(String key, String defaultValue) {
        Object object = this.opt(key);
        return NULL.equals(object) ? defaultValue : object.toString();
    }

    private void populateMap(Object bean) {
        Class klass = bean.getClass();
        boolean includeSuperClass = klass.getClassLoader() != null;
        Method[] methods = includeSuperClass ? klass.getMethods() : klass.getDeclaredMethods();

        for(int i = 0; i < methods.length; ++i) {
            try {
                Method method = methods[i];
                if (Modifier.isPublic(method.getModifiers())) {
                    String name = method.getName();
                    String key = "";
                    if (name.startsWith("get")) {
                        if (!"getClass".equals(name) && !"getDeclaringClass".equals(name)) {
                            key = name.substring(3);
                        } else {
                            key = "";
                        }
                    } else if (name.startsWith("is")) {
                        key = name.substring(2);
                    }

                    if (key.length() > 0 && Character.isUpperCase(key.charAt(0)) && method.getParameterTypes().length == 0) {
                        if (key.length() == 1) {
                            key = key.toLowerCase();
                        } else if (!Character.isUpperCase(key.charAt(1))) {
                            key = key.substring(0, 1).toLowerCase() + key.substring(1);
                        }

                        Object result = method.invoke(bean, (Object[])null);
                        if (result != null) {
                            this.mMap.put(key, JSONUtils.wrapValue(result));
                        }
                    }
                }
            }
            catch (Exception e) {
            }
        }

    }

    public JSONObject put( String key, boolean value ) throws JSONException {
        this.put(key, (Object)(value ? Boolean.TRUE : Boolean.FALSE));
        return this;
    }

    public JSONObject put( String key, Collection value ) throws JSONException {
        this.put(key, (Object)(new JSONArray(value)));
        return this;
    }

    public JSONObject put( String key, double value ) throws JSONException {
        this.put(key, (Object)(new Double(value)));
        return this;
    }

    public JSONObject put( String key, int value ) throws JSONException {
        this.put(key, (Object)(new Integer(value)));
        return this;
    }

    public JSONObject put( String key, long value ) throws JSONException {
        this.put(key, (Object)(new Long(value)));
        return this;
    }

    public JSONObject put( String key, Map value ) throws JSONException {
        this.put(key, (Object)(new JSONObject(value)));
        return this;
    }

    public JSONObject put( String key, Object value ) throws JSONException {
        if (key == null) {
            throw new NullPointerException("Null key.");
        }
        else {
            if ( value != null ) {
                JSONUtils.prospectNumberQualify(value);
                this.mMap.put( key, value );
            }
            else {
                this.remove( key );
            }
            return this;
        }
    }

    public JSONObject embed( String key, Object value ) throws JSONException {
        if (key == null) {
            throw new NullPointerException("Null key.");
        }
        else {
            if ( value != null ) {
                JSONUtils.prospectNumberQualify(value);
                this.mMap.put( key, value );
            }
            else {
                this.mMap.put( key, JSONObject.NULL );
            }
            return this;
        }
    }

    public JSONObject putOnce( String key, Object value ) throws JSONException {
        if (key != null && value != null) {
            if (this.opt(key) != null) {
                throw new JSONException("Duplicate key \"" + key + "\"");
            }

            this.put(key, value);
        }

        return this;
    }

    public JSONObject putOpt( String key, Object value ) throws JSONException {
        if (key != null && value != null) {
            this.put(key, value);
        }

        return this;
    }



    public Object remove( String key ) {
        return this.mMap.remove(key);
    }

    public JSONArray toJSONArray( JSONArray names ) throws JSONException {
        if (names != null && names.length() != 0) {
            JSONArray ja = new JSONArray();

            for( int i = 0; i < names.length(); ++i ) {
                ja.put(this.opt(names.getString(i)));
            }

            return ja;
        } else {
            return null;
        }
    }

    public JSONArray toJSONArray() {
        JSONArray jRegressed = new JSONArray();

        for ( Object obj : this.entrySet() ) {
            Map.Entry kv = ( Map.Entry ) obj;
            jRegressed.put( kv.getValue() );
        }

        return jRegressed;
    }

    public Map.Entry front() {
        return (Map.Entry) this.mMap.entrySet().iterator().next();
    }

    public Map.Entry back() {
        try {
            if( this.mMap instanceof LinkedHashMap ){
                Field tail = this.mMap.getClass().getDeclaredField("tail" );
                tail.setAccessible( true );
                return (Map.Entry) tail.get(this.mMap);
            }
            else if( this.mMap instanceof TreeMap ){
                return ( (TreeMap)this.mMap ).lastEntry();
            }
            else { // It seem there is the only way, fuck.
                Iterator iterator = this.mMap.entrySet().iterator();
                Map.Entry tail    = null;
                while ( iterator.hasNext() ) {
                    tail = (Map.Entry)iterator.next();
                }
                return tail;
            }
        }
        catch ( NoSuchFieldException | IllegalAccessException e ){
            return null;
        }
    }



    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public String toJSONString() {
        try {
            return this.toJSONString( 0 );
        }
        catch ( Exception e ) {
            return null;
        }
    }

    public String toJSONString( int nIndentFactor ) throws IOException {
        StringWriter w = new StringWriter();
        synchronized(w.getBuffer()) {
            return this.write( w, nIndentFactor,0 ).toString();
        }
    }

    @Override
    public TypeIndex prototype() {
        return Prototype.typeid( this );
    }

    @Override
    public String prototypeName() {
        return Prototype.prototypeName( this );
    }

    @Override
    public boolean isPrototypeOf  ( TypeIndex that ) {
        return that.equals( this.prototype() );
    }

    public boolean hasOwnProperty ( Object key ) {
        return this.mMap.containsKey(key);
    }


    @Override
    public JSONObject clone() {
        try {
            JSONObject that = (JSONObject) super.clone();
            that.mMap = new LinkedHashMap();
            for ( Object o : this.mMap.entrySet() ) {
                Entry e = (Entry) o;
                Object value = e.getValue();
                that.mMap.put( e.getKey(), JSONUtils.cloneElement( value ) );
            }
           return that;
        }
        catch ( CloneNotSupportedException e ) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
    }

    public Writer write( Writer writer ) throws IOException {
        return this.write( writer, 0, 0 );
    }

    public Writer write( Writer writer, int nIndentFactor ) throws IOException {
        return this.write( writer, nIndentFactor, 0 );
    }

    public Writer write( Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        return JSONSerializer.serialize( this.mMap, writer, nIndentFactor, nIndentBlankNum );
    }

    private static final class Null {
        private Null() {
        }

        @Override
        protected final Object clone() {
            try{
                super.clone();
            }
            catch ( CloneNotSupportedException e ) {
                throw new InternalError(e);
            }
            return this;
        }

        @Override
        public boolean equals( Object that ) {
            if ( that == this || that instanceof Null ) {
                return true;
            }
            return that == null;
        }

        @Override
        public String toString() {
            return "null";
        }
    }
}
