package Pinecone.Framework.Util.JSON;

import Pinecone.Framework.System.Prototype.PineUnit;
import Pinecone.Framework.System.Prototype.Prototype;
import Pinecone.Framework.System.Prototype.TypeIndex;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JSONArray implements PineUnit, Serializable, Cloneable, Iterable {
    private List mList;

    public JSONArray() {
        this.mList = new ArrayList();
    }

    public JSONArray( JSONCursorParser x ) throws JSONException {
        this();
        if (x.nextClean() != '[') {
            throw x.syntaxError("A JSONArray text must start with '['");
        } else if (x.nextClean() != ']') {
            x.back();

            while(true) {
                if (x.nextClean() == ',') {
                    x.back();
                    this.mList.add(JSONObject.NULL);
                }
                else {
                    x.back();
                    this.mList.add(x.nextValue());
                }

                switch(x.nextClean()) {
                    case ',': {
                        if (x.nextClean() == ']') {
                            return;
                        }

                        x.back();
                        break;
                    }
                    case ']': {
                        return;
                    }
                    default: {
                        throw x.syntaxError("Expected a ',' or ']'");
                    }
                }
            }
        }
    }

    public JSONArray( String source ) throws JSONException {
        this(new JSONCursorParser(source));
    }

    public JSONArray( Collection collection ) {
        this.mList = new ArrayList();
        if (collection != null) {
            Iterator iter = collection.iterator();

            while(iter.hasNext()) {
                this.mList.add(JSONUtils.wrapValue(iter.next()));
            }
        }

    }

    public JSONArray( Object array ) throws JSONException {
        this();
        if (!array.getClass().isArray()) {
            throw new JSONException("JSONArray initial value should be a string or collection or array.");
        } else {
            int length = Array.getLength(array);

            for(int i = 0; i < length; ++i) {
                this.put(JSONUtils.wrapValue(Array.get(array, i)));
            }

        }
    }

    public JSONArray( List array, boolean bAssimilateMode ) throws JSONException {
        if( bAssimilateMode ){
            this.mList = array;
        }
        else {
            this.mList = new ArrayList();
            if ( array != null ) {
                for ( Object o : array ) {
                    this.put( JSONUtils.wrapValue(o) );
                }
            }
        }
    }

    public JSONArray( List array ) throws JSONException {
        this( array, false );
    }



    public void assimilate( ArrayList that ){
        this.mList = that;
    }

    public List getArray(){
        return this.mList;
    }

    @Override
    public Iterator iterator() {
        return this.mList.iterator();
    }

    public Object front() {
        return this.opt( 0 );
    }

    public Object back() {
        return this.opt( this.length() - 1 );
    }



    public Object get(int index) throws JSONException {
        Object object = this.opt(index);
        if (object == null) {
            throw new JSONException("JSONArray[" + index + "] not found.");
        } else {
            return object;
        }
    }

    public boolean getBoolean(int index) throws JSONException {
        Object object = this.get(index);
        if (!object.equals(Boolean.FALSE) && (!(object instanceof String) || !((String)object).equalsIgnoreCase("false"))) {
            if (!object.equals(Boolean.TRUE) && (!(object instanceof String) || !((String)object).equalsIgnoreCase("true"))) {
                throw new JSONException("JSONArray[" + index + "] is not a boolean.");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public double getDouble(int index) throws JSONException {
        Object object = this.get(index);

        try {
            return object instanceof Number ? ((Number)object).doubleValue() : Double.parseDouble((String)object);
        }
        catch (Exception e) {
            throw new JSONException("JSONArray[" + index + "] is not a number.");
        }
    }

    public int getInt(int index) throws JSONException {
        Object object = this.get(index);

        try {
            return object instanceof Number ? ((Number)object).intValue() : Integer.parseInt((String)object);
        }
        catch (Exception e) {
            throw new JSONException("JSONArray[" + index + "] is not a number.");
        }
    }

    public JSONArray getJSONArray(int index) throws JSONException {
        Object object = this.get(index);
        if (object instanceof JSONArray) {
            return (JSONArray)object;
        }
        else {
            throw new JSONException("JSONArray[" + index + "] is not a JSONArray.");
        }
    }

    public JSONObject getJSONObject(int index) throws JSONException {
        Object object = this.get(index);
        if (object instanceof JSONObject) {
            return (JSONObject)object;
        }
        else {
            throw new JSONException("JSONArray[" + index + "] is not a JSONObject.");
        }
    }

    public long getLong(int index) throws JSONException {
        Object object = this.get(index);

        try {
            return object instanceof Number ? ((Number)object).longValue() : Long.parseLong((String)object);
        }
        catch (Exception e) {
            throw new JSONException("JSONArray[" + index + "] is not a number.");
        }
    }

    public String getString(int index) throws JSONException {
        Object object = this.get(index);
        if (object instanceof String) {
            return (String)object;
        } else {
            throw new JSONException("JSONArray[" + index + "] not a string.");
        }
    }

    public boolean isNull(int index) {
        return JSONObject.NULL.equals(this.opt(index));
    }

    public String join(String separator) throws JSONException {
        int len = this.length();
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < len; ++i) {
            if (i > 0) {
                sb.append(separator);
            }

            sb.append( JSONUtils.valueToString(this.mList.get(i)) );
        }

        return sb.toString();
    }

    public int length() {
        return this.mList.size();
    }

    public int size() {
        return this.mList.size();
    }

    public boolean isEmpty() {
        return this.mList.isEmpty();
    }

    public Object opt(int index) {
        return index >= 0 && index < this.length() ? this.mList.get(index) : null;
    }

    public boolean optBoolean(int index) {
        return this.optBoolean(index, false);
    }

    public boolean optBoolean(int index, boolean defaultValue) {
        try {
            return this.getBoolean(index);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public double optDouble(int index) {
        return this.optDouble(index, 0.0D / 0.0);
    }

    public double optDouble(int index, double defaultValue) {
        try {
            return this.getDouble(index);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public int optInt(int index) {
        return this.optInt(index, 0);
    }

    public int optInt(int index, int defaultValue) {
        try {
            return this.getInt(index);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public JSONArray optJSONArray(int index) {
        Object o = this.opt(index);
        return o instanceof JSONArray ? (JSONArray)o : null;
    }

    public JSONObject optJSONObject(int index) {
        Object o = this.opt(index);
        return o instanceof JSONObject ? (JSONObject)o : null;
    }

    public long optLong(int index) {
        return this.optLong(index, 0L);
    }

    public long optLong(int index, long defaultValue) {
        try {
            return this.getLong(index);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public String optString(int index) {
        return this.optString(index, "");
    }

    public String optString(int index, String defaultValue) {
        Object object = this.opt(index);
        return JSONObject.NULL.equals(object) ? defaultValue : object.toString();
    }

    public JSONArray put(boolean value) {
        this.put((Object)(value ? Boolean.TRUE : Boolean.FALSE));
        return this;
    }

    public JSONArray put(Collection value) {
        this.put((Object)(new JSONArray(value)));
        return this;
    }

    public JSONArray put(double value) throws JSONException {
        Double d = new Double(value);
        JSONUtils.prospectNumberQualify(d);
        this.put((Object)d);
        return this;
    }

    public JSONArray put(int value) {
        this.put((Object)(new Integer(value)));
        return this;
    }

    public JSONArray put(long value) {
        this.put((Object)(new Long(value)));
        return this;
    }

    public JSONArray put(Map value) {
        this.put((Object)(new JSONObject(value)));
        return this;
    }

    public JSONArray put(Object value) {
        this.mList.add(value);
        return this;
    }

    public JSONArray put(int index, boolean value) throws JSONException {
        this.put(index, (Object)(value ? Boolean.TRUE : Boolean.FALSE));
        return this;
    }

    public JSONArray put(int index, Collection value) throws JSONException {
        this.put(index, (Object)(new JSONArray(value)));
        return this;
    }

    public JSONArray put(int index, double value) throws JSONException {
        this.put(index, (Object)(new Double(value)));
        return this;
    }

    public JSONArray put(int index, int value) throws JSONException {
        this.put(index, (Object)(new Integer(value)));
        return this;
    }

    public JSONArray put(int index, long value) throws JSONException {
        this.put(index, (Object)(new Long(value)));
        return this;
    }

    public JSONArray put(int index, Map value) throws JSONException {
        this.put(index, (Object)(new JSONObject(value)));
        return this;
    }

    public JSONArray put(int index, Object value) throws JSONException {
        JSONUtils.prospectNumberQualify(value);
        if (index < 0) {
            throw new JSONException("JSONArray[" + index + "] not found.");
        } else {
            if (index < this.length()) {
                this.mList.set(index, value);
            } else {
                while(index != this.length()) {
                    this.put(JSONObject.NULL);
                }

                this.put(value);
            }

            return this;
        }
    }

    public Object remove(int index) {
        Object o = this.opt(index);
        if (index >= 0 && index < this.length()) {
            this.mList.remove(index);
        }

        return o;
    }

    public JSONObject toJSONObject( JSONArray names ) throws JSONException {
        if (names != null && names.length() != 0 && this.length() != 0) {
            JSONObject jo = new JSONObject();

            for(int i = 0; i < names.length(); ++i) {
                jo.put(names.getString(i), this.opt(i));
            }

            return jo;
        }

        return null;
    }

    public JSONObject toJSONObject()  {
        JSONObject jo = new JSONObject();

        for( int i = 0; i < this.size(); ++i ) {
            jo.put( String.valueOf(i), this.opt( i ) );
        }

        return jo;
    }


    @Override
    public boolean hasOwnProperty( Object elm ) {
        try {
            return this.hasOwnProperty( (int)Integer.valueOf(elm.toString()) );
        }
        catch ( NumberFormatException e ){
            return false;
        }
    }

    public boolean hasOwnProperty( int elm ) {
        int nLength = this.length();
        if( elm < 0 || nLength == 0 ){
            return false;
        }
        return nLength > elm;
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public String toJSONString() {
        try {
            return this.toJSONString(0);
        }
        catch (Exception e) {
            return null;
        }
    }

    public String toJSONString( int nIndentFactor ) throws IOException {
        StringWriter sw = new StringWriter();
        synchronized(sw.getBuffer()) {
            return this.write( sw, nIndentFactor, 0 ).toString();
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
    public boolean isPrototypeOf( TypeIndex that ) {
        return that.equals( this.prototype() );
    }



    @Override
    public JSONArray clone() {
        try {
            JSONArray that = (JSONArray) super.clone();
            that.mList = new ArrayList();
            for ( Object row : this.mList ) {
                that.put( JSONUtils.cloneElement( row ) );
            }
            return that;
        }
        catch ( CloneNotSupportedException e ) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
    }

    public Writer write( Writer writer) throws IOException {
        return this.write( writer, 0, 0 );
    }

    public Writer write( Writer writer, int nIndentFactor ) throws IOException {
        return this.write( writer, nIndentFactor, 0 );
    }

    public Writer write( Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        boolean bHasNextElement = false;
        int length = this.length();
        writer.write('[');

        if ( length == 1 ) {
            JSONSerializer.serialize( this.mList.get(0),writer , nIndentFactor, nIndentBlankNum );
        }
        else if ( length != 0 ) {
            int nNewIndent = nIndentBlankNum + nIndentFactor;

            for( int i = 0; i < length; ++i ) {
                JSONSerializer.beforeJsonElementWrote( writer, nIndentFactor, nNewIndent, bHasNextElement );
                JSONSerializer.serialize( this.mList.get(i),writer , nIndentFactor, nNewIndent );
                bHasNextElement = true;
            }

            if (nIndentFactor > 0) {
                writer.write(10);
            }

            JSONSerializer.indentBlank( writer, nIndentBlankNum );
        }

        writer.write(']');
        return writer;
    }
}
