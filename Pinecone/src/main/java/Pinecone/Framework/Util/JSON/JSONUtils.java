package Pinecone.Framework.Util.JSON;

import Pinecone.Framework.System.util.StringUtils;
import Pinecone.Framework.Util.JSON.Prototype.JSONString;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class JSONUtils {
    public static void prospectNumberQualify(Object o) throws JSONException {
        if (o != null) {
            if (o instanceof Double) {
                if (((Double)o).isInfinite() || ((Double)o).isNaN()) {
                    throw new JSONException("JSON does not allow non-finite numbers.");
                }
            }
            else if (o instanceof Float && (((Float)o).isInfinite() || ((Float)o).isNaN())) {
                throw new JSONException("JSON does not allow non-finite numbers.");
            }
        }
    }

    private static String numberLikeStringTransfer( String string ){
        if ( string.indexOf(46) > 0 && string.indexOf(101) < 0 && string.indexOf(69) < 0 ) {
            while(string.endsWith("0")) {
                string = string.substring(0, string.length() - 1);
            }

            if (string.endsWith(".")) {
                string = string.substring(0, string.length() - 1);
            }
        }

        return string;
    }

    public static String numberToString( Number number ) throws JSONException {
        if (number == null) {
            throw new JSONException("Null pointer");
        }
        else {
            JSONUtils.prospectNumberQualify(number);
            String string = number.toString();
            return JSONUtils.numberLikeStringTransfer( string );
        }
    }

    public static String doubleToString( double d ) {
        if (!Double.isInfinite(d) && !Double.isNaN(d)) {
            String string = Double.toString(d);
            return JSONUtils.numberLikeStringTransfer( string );
        }
        else {
            return "null";
        }
    }

    public static String valueToString( Object value ) throws JSONException {
        if (value != null && !value.equals((Object)null)) {
            if (value instanceof JSONString) {
                String object;
                try {
                    object = ((JSONString)value).toJSONString();
                }
                catch (Exception e) {
                    throw new JSONException(e);
                }

                if (object instanceof String) {
                    return (String)object;
                } else {
                    throw new JSONException("Bad value from toJSONString: " + object);
                }
            }
            else if (value instanceof Number) {
                return JSONUtils.numberToString((Number)value);
            }
            else if (!(value instanceof Boolean) && !(value instanceof JSONObject) && !(value instanceof JSONArray)) {
                if (value instanceof Map) {
                    return (new JSONObject((Map)value)).toString();
                }
                else if (value instanceof Collection) {
                    return (new JSONArray((Collection)value)).toString();
                }
                else {
                    return value.getClass().isArray() ? (new JSONArray(value)).toString() : StringUtils.jsonQuote(value.toString());
                }
            }
            else {
                return value.toString();
            }
        }
        else {
            return "null";
        }
    }

    public static Object stringToValue(String string) {
        if (string.equals("")) {
            return string;
        }
        else if (string.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        }
        else if (string.equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        }
        else if ( string.equalsIgnoreCase("null") || string.equalsIgnoreCase("undefined") ) {
            return JSONObject.NULL;
        }
        else {
            char b = string.charAt(0);
            if (b >= '0' && b <= '9' || b == '-') {
                try {
                    if (string.indexOf(46) <= -1 && string.indexOf(101) <= -1 && string.indexOf(69) <= -1) {
                        Long myLong = new Long(string);
                        if (string.equals(myLong.toString())) {
                            if (myLong == (long)myLong.intValue()) {
                                return myLong.intValue();
                            }

                            return myLong;
                        }
                    }
                    else {
                        Double d = Double.valueOf(string);
                        if (!d.isInfinite() && !d.isNaN()) {
                            return d;
                        }
                    }
                }
                catch (Exception e) {
                }
            }

            return string;
        }
    }

    public static Object wrapValue( Object value ) {
        try {
            if (value == null) {
                return JSONObject.NULL;
            }
            else if (!(value instanceof JSONObject) && !(value instanceof JSONArray) && !JSONObject.NULL.equals(value) && !(value instanceof JSONString) && !(value instanceof Byte) && !(value instanceof Character) && !(value instanceof Short) && !(value instanceof Integer) && !(value instanceof Long) && !(value instanceof Boolean) && !(value instanceof Float) && !(value instanceof Double) && !(value instanceof String)) {
                if (value instanceof Collection) {
                    return new JSONArray((Collection)value);
                }
                else if (value.getClass().isArray()) {
                    return new JSONArray(value);
                }
                else if (value instanceof Map) {
                    return new JSONObject((Map)value);
                }
                else {
                    Package objectPackage = value.getClass().getPackage();
                    String objectPackageName = objectPackage != null ? objectPackage.getName() : "";
                    return !objectPackageName.startsWith("java.") && !objectPackageName.startsWith("javax.") && value.getClass().getClassLoader() != null ? new JSONObject(value) : value.toString();
                }
            }
            else {
                return value;
            }
        }
        catch ( Exception e ) {
            return null;
        }
    }

    public static String[] getOwnPropertyNames ( JSONObject that ) {
        int nSize = that.size();
        if ( nSize == 0 ) {
            return null;
        }
        else {
            Iterator iterator = that.keys();
            String[] names = new String[nSize];

            for( int i = 0; iterator.hasNext(); ++i ) {
                names[i] = (String)iterator.next();
            }

            return names;
        }
    }

    public static Object cloneElement ( Object that ) {
        if( that instanceof JSONArray ) {
            return   ( ( JSONArray ) that ).clone();
        }
        else if( that instanceof JSONObject ) {
            return   ( ( JSONObject ) that ).clone();
        }
        return that;
    }
}
