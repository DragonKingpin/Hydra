package com.pinecone.framework.util.json;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.system.prototype.PinenutTraits;
import com.pinecone.framework.util.StringUtils;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class GenericJSONEncoder implements JSONEncoder {
    public static void beforeJsonElementWrote( Writer writer, int nIndentFactor, int nIndentBlankNum, boolean bHasNextElement ) throws IOException {
        if ( bHasNextElement ) {
            writer.write(',');
        }

        if ( nIndentFactor > 0 ) {
            writer.write('\n');
        }

        GenericJSONEncoder.indentBlank( writer, nIndentBlankNum );
    }

    public static void indentBlank( Writer writer, int nIndentBlankNum ) throws IOException {
        for( int i = 0; i < nIndentBlankNum; ++i ) {
            writer.write(' ' );
        }
    }

    public GenericJSONEncoder() { }

    protected Writer writeUnidentifiedObject ( Object that, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        StringUtils.addSlashes( String.format(
                PinenutTraits.OBJ_STRINGIFY_DEFAULT,
                that.getClass().getName() + "(0x" + Integer.toHexString( that.hashCode() ) + ")"
        ), writer, true );

        return writer;
    }

    protected Writer writeUnknownAnyObject   ( Object that, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        if ( that != null ) {
            String szJsonString = "";

            try {
                szJsonString = PinenutTraits.invokeToJSONString( that, nIndentFactor, nIndentBlankNum );
            }
            catch ( Exception e ){
                try {
                    szJsonString = PinenutTraits.invokeToJSONString( that );
                }
                catch ( Exception e1 ){
                    try{
                        szJsonString = PinenutTraits.invokeCaseToString( that, null );
                        StringUtils.addSlashes( szJsonString, writer, true );
                        return writer;
                    }
                    catch ( IllegalArgumentException ea ) {
                        return this.writeUnidentifiedObject( that, writer, nIndentFactor, nIndentBlankNum ) ;
                    }
                }
            }

            writer.write( szJsonString );
        }
        else {
            writer.write( JSONEncoder.JSON_OBJ_NULL_DEFAULT );
        }
        return writer;
    }

    @Override
    public Writer write          ( Pinenut that, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        if ( that != null ) {
            writer.write( that.toJSONString() );
        }
        else {
            writer.write( JSONEncoder.JSON_OBJ_NULL_DEFAULT );
        }
        return writer;
    }

    @Override
    public Writer write          ( JSONObject that, Writer writer ) throws IOException {
        return this.write( that, writer,0,0  );
    }

    @Override
    public Writer write          ( JSONObject that, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        if ( that != null ) {
            that.write( writer, nIndentFactor, nIndentBlankNum  );
        }
        else {
            writer.write( JSONEncoder.JSON_OBJ_NULL_DEFAULT );
        }
        return writer;
    }

    @Override
    public Writer write          ( JSONArray that, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        if ( that != null ) {
            that.write( writer, nIndentFactor, nIndentBlankNum  );
        }
        else {
            writer.write( JSONEncoder.JSON_OBJ_NULL_DEFAULT );
        }
        return writer;
    }

    @Override
    public void writeKeyValue    ( Writer writer, Object key, Object val, int nIndentFactor, int nIndentBlankNum ) throws JSONException, IOException {
        writer.write( StringUtils.jsonQuote( key.toString() ) );
        writer.write(':');
        if ( nIndentFactor > 0 ) {
            writer.write( ' ');
        }

        this.write( val, writer, nIndentFactor, nIndentBlankNum  );
    }

    @Override
    public Writer write          ( Map that, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        if ( that != null ) {
            boolean bHasNextElement = false;
            int length = that.size();
            Iterator iter = that.entrySet().iterator();
            writer.write('{');
            if ( length == 1 ) {
                Object o = iter.next();
                Map.Entry kv = (Map.Entry) o;
                this.writeKeyValue( writer, kv.getKey(), kv.getValue(), nIndentFactor, nIndentBlankNum  );
            }
            else if ( length != 0 ) {
                for( int nNewIndent = nIndentBlankNum + nIndentFactor; iter.hasNext(); bHasNextElement = true ) {
                    GenericJSONEncoder.beforeJsonElementWrote( writer, nIndentFactor, nNewIndent, bHasNextElement );
                    Object o = iter.next();
                    Map.Entry kv = (Map.Entry) o;
                    this.writeKeyValue( writer, kv.getKey(), kv.getValue(), nIndentFactor, nNewIndent  );
                }

                if ( nIndentFactor > 0 ) {
                    writer.write( '\n' );
                }

                GenericJSONEncoder.indentBlank( writer, nIndentBlankNum );
            }

            writer.write( '}' );
            return writer;
        }
        else {
            writer.write( JSONEncoder.JSON_OBJ_NULL_DEFAULT );
        }
        return writer;
    }

    @Override
    public Writer writeArray     ( Object that, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        if ( that != null ) {
            boolean bHasNextElement = false;

            int length = Array.getLength( that );
            writer.write('[');

            if ( length == 1 ) {
                this.write( Array.get( that, 0 ), writer, nIndentFactor, nIndentBlankNum  );
            }
            else if ( length != 0 ) {
                int nNewIndent = nIndentBlankNum + nIndentFactor;

                for( int i = 0; i < length; ++i ) {
                    GenericJSONEncoder.beforeJsonElementWrote( writer, nIndentFactor, nNewIndent, bHasNextElement );
                    this.write( Array.get( that, i ),writer, nIndentFactor, nNewIndent  );
                    bHasNextElement = true;
                }

                if ( nIndentFactor > 0 ) {
                    writer.write( '\n' );
                }

                GenericJSONEncoder.indentBlank( writer, nIndentBlankNum );
            }

            writer.write(']');
        }
        else {
            writer.write( JSONEncoder.JSON_OBJ_NULL_DEFAULT );
        }
        return writer;
    }

    @Override
    public Writer write          ( Collection that, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        if ( that != null ) {
            boolean bHasNextElement = false;

            int length = that.size();
            writer.write('[');
            Iterator iter = that.iterator();

            if ( length == 1 ) {
                this.write( iter.next(),writer , nIndentFactor, nIndentBlankNum  );
            }
            else if ( length != 0 ) {
                int nNewIndent = nIndentBlankNum + nIndentFactor;

                while( iter.hasNext() ) {
                    GenericJSONEncoder.beforeJsonElementWrote( writer, nIndentFactor, nNewIndent, bHasNextElement );
                    this.write( iter.next(), writer, nIndentFactor, nNewIndent  );
                    bHasNextElement = true;
                }

                if ( nIndentFactor > 0 ) {
                    writer.write( '\n' );
                }
                GenericJSONEncoder.indentBlank( writer, nIndentBlankNum );
            }

            writer.write(']');
        }
        else {
            writer.write( JSONEncoder.JSON_OBJ_NULL_DEFAULT );
        }
        return writer;
    }

    public Writer write          ( Map.Entry that, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        if ( that != null ) {
            writer.write('{');
            this.writeKeyValue( writer, that.getKey(), that.getValue(), nIndentFactor, nIndentBlankNum  );
            writer.write( '}' );
            return writer;
        }
        else {
            writer.write( JSONEncoder.JSON_OBJ_NULL_DEFAULT );
        }
        return writer;
    }

    @Override
    public Writer write          ( Object that, Writer writer ) throws IOException {
        return this.write( that, writer, 0, 0 );
    }

    @Override
    public Writer write          ( Object that, Writer writer, int nIndentFactor ) throws IOException {
        return this.write( that, writer, nIndentFactor, 0 );
    }

    @Override
    public Writer write          ( Object that, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        if ( that != null ) {
            if ( that instanceof JSONObject ) {
                ((JSONObject)that).write(writer, nIndentFactor, nIndentBlankNum );
            }
            else if ( that instanceof JSONArray ) {
                ((JSONArray)that).write(writer, nIndentFactor, nIndentBlankNum );
            }
            else if ( that == JSON.NULL ) {
                writer.write( that.toString() );
            }
            else if ( that instanceof String ){
                StringUtils.addSlashes( (String) that, writer, true );
            }
            else if ( that instanceof Map ) {
                this.write( (Map) that, writer, nIndentFactor, nIndentBlankNum  );
                //(new JSONMaptron((Map)jsonValue, true)).write(writer, nIndentFactor, nIndentBlankNum );
            }
            else if ( that instanceof Collection ) {
                this.write( (Collection)that, writer, nIndentFactor, nIndentBlankNum  );
                //(new JSONArraytron((Collection)jsonValue)).write(writer, nIndentFactor, nIndentBlankNum );
            }
            else if ( that.getClass().isArray() ) {
                this.writeArray( that, writer, nIndentFactor, nIndentBlankNum  );
                //(new JSONArraytron(jsonValue)).write(writer, nIndentFactor, nIndentBlankNum );
            }
            else if ( that instanceof Number ) {
                writer.write( JSONUtils.numberToString((Number)that) );
            }
            else if ( that instanceof Boolean ) {
                writer.write(that.toString());
            }
            else if ( that instanceof JSONString ) {
                String o;
                try {
                    o = ((JSONString)that).toJSONString();
                }
                catch ( Exception e ) {
                    throw new JSONException(e);
                }

                writer.write( o != null ? o.toString() : StringUtils.jsonQuote(that.toString()) );
            }
            else if ( that instanceof Map.Entry ) {
                this.write( (Map.Entry) that, writer, nIndentFactor, nIndentBlankNum  );
            }
            else if ( that instanceof Pinenut ){
                this.write( (Pinenut)that, writer, nIndentFactor, nIndentBlankNum  );
            }
            else {
                this.writeUnknownAnyObject( that, writer, nIndentFactor, nIndentBlankNum  );
            }
        }
        else {
            writer.write( JSONEncoder.JSON_OBJ_NULL_DEFAULT );
        }
        return writer;
    }

}
