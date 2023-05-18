package Pinecone.Framework.Util.JSON;

import Pinecone.Framework.System.Prototype.Pinenut;
import Pinecone.Framework.System.Prototype.PinenutTraits;
import Pinecone.Framework.System.util.StringUtils;
import Pinecone.Framework.Util.JSON.Prototype.JSONString;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class JSONSerializer {
    public static String JSON_OBJ_STRINGIFY_DEFAULT             = "[object %s]";

    public static String JSON_OBJ_NULL_DEFAULT                  = "null";



    public static void beforeJsonElementWrote( Writer writer, int nIndentFactor, int nIndentBlankNum, boolean bHasNextElement ) throws IOException {
        if ( bHasNextElement ) {
            writer.write(',');
        }

        if ( nIndentFactor > 0 ) {
            writer.write('\n');
        }

        JSONSerializer.indentBlank( writer, nIndentBlankNum );
    }

    public static void indentBlank( Writer writer, int nIndentBlankNum ) throws IOException {
        for( int i = 0; i < nIndentBlankNum; ++i ) {
            writer.write(' ' );
        }
    }


    public static Writer serialize( Object data, Writer writer ) throws IOException {
        return JSONSerializer.serialize( data, writer, 0, 0 );
    }

    public static Writer serialize( Object data, Writer writer, int nIndentFactor ) throws IOException {
        return JSONSerializer.serialize( data, writer, nIndentFactor, 0 );
    }

    public static Writer serialize( Object data, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        if ( data != null ) {
            if ( data instanceof JSONObject ) {
                ((JSONObject)data).write(writer, nIndentFactor, nIndentBlankNum );
            }
            else if ( data instanceof JSONArray ) {
                ((JSONArray)data).write(writer, nIndentFactor, nIndentBlankNum );
            }
            else if ( data == JSONObject.NULL ) {
                writer.write( data.toString() );
            }
            else if ( data instanceof Map ) {
                JSONSerializer.serialize( (Map) data, writer, nIndentFactor, nIndentBlankNum  );
                //(new JSONObject((Map)jsonValue, true)).write(writer, nIndentFactor, nIndentBlankNum );
            }
            else if ( data instanceof Collection ) {
                JSONSerializer.serialize( (Collection)data, writer, nIndentFactor, nIndentBlankNum  );
                //(new JSONArray((Collection)jsonValue)).write(writer, nIndentFactor, nIndentBlankNum );
            }
            else if ( data.getClass().isArray() ) {
                JSONSerializer.serializeArray( data, writer, nIndentFactor, nIndentBlankNum  );
                //(new JSONArray(jsonValue)).write(writer, nIndentFactor, nIndentBlankNum );
            }
            else if ( data instanceof Number ) {
                writer.write( JSONUtils.numberToString((Number)data) );
            }
            else if ( data instanceof Boolean ) {
                writer.write(data.toString());
            }
            else if ( data instanceof JSONString ) {
                String o;
                try {
                    o = ((JSONString)data).toJSONString();
                }
                catch ( Exception e ) {
                    throw new JSONException(e);
                }

                writer.write(o != null ? o.toString() : StringUtils.jsonQuote(data.toString()));
            }
            else if ( data instanceof String ){
                StringUtils.addSlashes( (String) data, writer, true );
            }
            else if ( data instanceof Pinenut ){
                JSONSerializer.serialize( (Pinenut)data, writer, nIndentFactor, nIndentBlankNum  );
            }
            else {
                JSONSerializer.serializeAtomicObject( data, writer, nIndentFactor, nIndentBlankNum  );
            }
        }
        else {
            writer.write( JSONSerializer.JSON_OBJ_NULL_DEFAULT );
        }
        return writer;
    }

    protected static Writer serializeAtomicObject( Object data, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        if ( data != null ) {
            String szJsonString = "";

            try {
                szJsonString = PinenutTraits.invokeToJSONString( data, nIndentFactor, nIndentBlankNum );
            }
            catch ( Exception e ){
                try {
                    szJsonString = PinenutTraits.invokeToJSONString( data );
                }
                catch ( Exception e1 ){
                    szJsonString = PinenutTraits.invokeToString( data, null );
                }
            }

            StringUtils.addSlashes( szJsonString, writer, true );
        }
        else {
            writer.write(JSONSerializer.JSON_OBJ_NULL_DEFAULT);
        }
        return writer;
    }

    public static Writer serialize( Pinenut data, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        if ( data != null ) {
            writer.write( data.toJSONString() );
        }
        else {
            writer.write(JSONSerializer.JSON_OBJ_NULL_DEFAULT);
        }
        return writer;
    }

    public static Writer serialize( JSONObject data, Writer writer ) throws IOException {
        return JSONSerializer.serialize( data, writer,0,0  );
    }

    public static Writer serialize( JSONObject data, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        if ( data != null ) {
            data.write( writer, nIndentFactor, nIndentBlankNum  );
        }
        else {
            writer.write(JSONSerializer.JSON_OBJ_NULL_DEFAULT);
        }
        return writer;
    }

    public static Writer serialize( JSONArray data, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        if ( data != null ) {
            data.write( writer, nIndentFactor, nIndentBlankNum  );
        }
        else {
            writer.write(JSONSerializer.JSON_OBJ_NULL_DEFAULT);
        }
        return writer;
    }

    public static void serializeMapKeyValue( Writer writer, Map that, Object key, int nIndentFactor, int nIndentBlankNum ) throws JSONException, IOException {
        writer.write( StringUtils.jsonQuote( key.toString() ) );
        writer.write(':');
        if ( nIndentFactor > 0 ) {
            writer.write( ' ');
        }

        JSONSerializer.serialize( that.get(key), writer, nIndentFactor, nIndentBlankNum  );
    }

    public static Writer serialize( Map data, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        if ( data != null ) {
            boolean bHasNextElement = false;
            int length = data.size();
            Iterator keysIter = data.keySet().iterator();
            writer.write('{');
            if ( length == 1 ) {
                JSONSerializer.serializeMapKeyValue( writer, data, keysIter.next(), nIndentFactor, nIndentBlankNum  );
            }
            else if ( length != 0 ) {
                for( int nNewIndent = nIndentBlankNum + nIndentFactor; keysIter.hasNext(); bHasNextElement = true ) {
                    Object key = keysIter.next();
                    JSONSerializer.beforeJsonElementWrote( writer, nIndentFactor, nNewIndent, bHasNextElement );
                    JSONSerializer.serializeMapKeyValue( writer, data, key, nIndentFactor, nNewIndent  );
                }

                if ( nIndentFactor > 0 ) {
                    writer.write(10);
                }

                JSONSerializer.indentBlank( writer, nIndentBlankNum );
            }

            writer.write( '}' );
            return writer;
        }
        else {
            writer.write(JSONSerializer.JSON_OBJ_NULL_DEFAULT);
        }
        return writer;
    }

    public static Writer serializeArray( Object data, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        if ( data != null ) {
            boolean bHasNextElement = false;

            int length = Array.getLength( data );
            writer.write('[');

            if ( length == 1 ) {
                JSONSerializer.serialize( Array.get( data, 0 ), writer, nIndentFactor, nIndentBlankNum  );
            }
            else if ( length != 0 ) {
                int nNewIndent = nIndentBlankNum + nIndentFactor;

                for( int i = 0; i < length; ++i ) {
                    JSONSerializer.beforeJsonElementWrote( writer, nIndentFactor, nNewIndent, bHasNextElement );
                    JSONSerializer.serialize( Array.get( data, i ),writer, nIndentFactor, nNewIndent  );
                    bHasNextElement = true;
                }

                if (nIndentFactor > 0) {
                    writer.write(10);
                }

                JSONSerializer.indentBlank( writer, nIndentBlankNum );
            }

            writer.write(']');
        }
        else {
            writer.write(JSONSerializer.JSON_OBJ_NULL_DEFAULT);
        }
        return writer;
    }

    public static Writer serialize( Collection data, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        if ( data != null ) {
            boolean bHasNextElement = false;

            int length = data.size();
            writer.write('[');
            Iterator iter = data.iterator();

            if ( length == 1 ) {
                JSONSerializer.serialize( iter.next(),writer , nIndentFactor, nIndentBlankNum  );
            }
            else if ( length != 0 ) {
                int nNewIndent = nIndentBlankNum + nIndentFactor;

                while(iter.hasNext()) {
                    JSONSerializer.beforeJsonElementWrote( writer, nIndentFactor, nNewIndent, bHasNextElement );
                    JSONSerializer.serialize( iter.next(), writer, nIndentFactor, nNewIndent  );
                    bHasNextElement = true;
                }

                if (nIndentFactor > 0) {
                    writer.write(10);
                }
                JSONSerializer.indentBlank( writer, nIndentBlankNum );
            }

            writer.write(']');
        }
        else {
            writer.write(JSONSerializer.JSON_OBJ_NULL_DEFAULT);
        }
        return writer;
    }



    public static String stringify( Object data, int nIndentFactor ){
        StringWriter w = new StringWriter();
        try {
            synchronized(w.getBuffer()) {
                return JSONSerializer.serialize( data, w, nIndentFactor,0 ).toString();
            }
        }
        catch ( IOException e ){
            return null;
        }
    }

    public static String stringify( Object data ){
        StringWriter w = new StringWriter();
        try {
            synchronized(w.getBuffer()) {
                return JSONSerializer.serialize( data, w, 0,0 ).toString();
            }
        }
        catch ( IOException e ){
            return null;
        }
    }

}
