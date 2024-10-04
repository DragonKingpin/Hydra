package com.pinecone.framework.util.json;

import com.pinecone.framework.system.prototype.Pinenut;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public interface JSONEncoder extends Pinenut {
    String          JSON_OBJ_STRINGIFY_DEFAULT             = "[object %s]";
    String          JSON_OBJ_NULL_DEFAULT                  = "null";
    JSONEncoder     BASIC_JSON_ENCODER                     = new GenericJSONEncoder();
    JSONMarshal     BASIC_JSON_MARSHAL                     = new JSONMarshal();


    Writer write              ( Pinenut that, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException ;

    Writer write              ( JSONObject that, Writer writer ) throws IOException ;

    Writer write              ( JSONObject that, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException ;

    Writer write              ( JSONArray that, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException ;

    void writeKeyValue        ( Writer writer, Object key, Object val, int nIndentFactor, int nIndentBlankNum ) throws JSONException, IOException ;

    default Writer write      ( Map that, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        return this.writeMapFmtEntries( that.entrySet(), writer, nIndentFactor, nIndentBlankNum );
    }

    default <T extends Map.Entry > Writer writeMapFmtEntriesT ( Collection<T> that, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException  {
        return this.writeMapFmtEntries( that, writer, nIndentFactor, nIndentBlankNum );
    }

    Writer writeMapFmtEntries ( Collection that, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException ;

    Writer writeArray         ( Object that, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException ;

    Writer write              ( Collection that, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException ;

    Writer write              ( Object that, Writer writer ) throws IOException ;

    Writer write              ( Object that, Writer writer, int nIndentFactor ) throws IOException ;

    Writer write              ( Object that, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException ;




    static Writer writeMapFormat     ( Writer writer, Object[] those, int nIndentFactor, int nIndentBlankNum, JSONEncoder encoder ) throws IOException {
        int length = those.length;

        writer.write('{');

        if ( length == 1 ) {
            if( !(those[0] instanceof Map.Entry ) ){
                throw new IllegalArgumentException( "Serialized object should be [Map.Entry]." );
            }

            Map.Entry kv = (Map.Entry)those[0];
            encoder.writeKeyValue( writer, kv.getKey(), kv.getValue(), nIndentFactor, nIndentBlankNum  );
        }
        else if ( length != 0 ) {
            int nNewIndent = nIndentBlankNum + nIndentFactor;
            for ( int i = 0; i < length; ++i ) {
                Map.Entry kv = (Map.Entry)those[i];
                GenericJSONEncoder.beforeJsonElementWrote( writer, nIndentFactor, nNewIndent, i !=0 );
                encoder.writeKeyValue( writer, kv.getKey(), kv.getValue(), nIndentFactor, nNewIndent  );
            }
            if ( nIndentFactor > 0 ) {
                writer.write( '\n' );
            }

            GenericJSONEncoder.indentBlank( writer, nIndentBlankNum );
        }


        writer.write( '}' );

        return writer;
    }

    static Writer writeMapFormat     ( Writer writer, Object[] those, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        return JSONEncoder.writeMapFormat( writer, those, nIndentFactor, nIndentBlankNum, JSONEncoder.BASIC_JSON_ENCODER );
    }

    static String writeMapFormat     ( Object[] those, int nIndentFactor ) {
        StringWriter w = new StringWriter();
        try {
            synchronized( w.getBuffer() ) {
                JSONEncoder.writeMapFormat( w, (Object[])those, nIndentFactor,0 );
                return w.toString();
            }
        }
        catch ( IOException e ){
            return null;
        }
    }

    static String stringifyMapFormat ( Object[] those ) {
        return JSONEncoder.writeMapFormat( those, 0 );
    }




    static Writer writeMapFormat     ( Writer writer, Collection those, int nIndentFactor, int nIndentBlankNum, JSONEncoder encoder ) throws IOException {
        boolean bHasNextElement = false;

        int length = those.size();
        writer.write('{');
        Iterator iter = those.iterator();

        if ( length == 1 ) {
            Map.Entry kv = (Map.Entry)iter.next();
            encoder.writeKeyValue( writer, kv.getKey(), kv.getValue(), nIndentFactor, nIndentBlankNum  );
        }
        else if ( length != 0 ) {
            int nNewIndent = nIndentBlankNum + nIndentFactor;

            while( iter.hasNext() ) {
                GenericJSONEncoder.beforeJsonElementWrote( writer, nIndentFactor, nNewIndent, bHasNextElement );
                Map.Entry kv = (Map.Entry)iter.next();
                encoder.writeKeyValue( writer, kv.getKey(), kv.getValue(), nIndentFactor, nNewIndent  );
                bHasNextElement = true;
            }

            if ( nIndentFactor > 0 ) {
                writer.write( '\n' );
            }
            GenericJSONEncoder.indentBlank( writer, nIndentBlankNum );
        }

        writer.write('}');
        return writer;
    }

    static Writer writeMapFormat     ( Writer writer, Collection those, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        return JSONEncoder.writeMapFormat( writer, those, nIndentFactor, nIndentBlankNum, JSONEncoder.BASIC_JSON_ENCODER );
    }

    static String writeMapFormat     ( Collection those, int nIndentFactor ) {
        StringWriter w = new StringWriter();
        try {
            synchronized( w.getBuffer() ) {
                JSONEncoder.writeMapFormat( w, (Collection)those, nIndentFactor,0 );
                return w.toString();
            }
        }
        catch ( IOException e ){
            return null;
        }
    }

    static String stringifyMapFormat ( Collection those ) {
        return JSONEncoder.writeMapFormat( those, 0 );
    }
}
