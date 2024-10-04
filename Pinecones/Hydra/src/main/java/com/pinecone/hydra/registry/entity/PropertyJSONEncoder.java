package com.pinecone.hydra.registry.entity;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;

import com.pinecone.framework.unit.UniScopeMap;
import com.pinecone.framework.util.StringUtils;
import com.pinecone.framework.util.json.GenericJSONEncoder;

public class PropertyJSONEncoder extends GenericJSONEncoder {
    protected boolean mbSimpleEncode;

    public PropertyJSONEncoder( boolean bSimpleEncode ) {
        this.mbSimpleEncode = bSimpleEncode;
    }

    public PropertyJSONEncoder() {
        this( true );
    }

    @Override
    public Writer write                    ( Object that, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        if( that instanceof Properties ) {
            this.write( (Properties) that, writer, nIndentFactor, nIndentBlankNum );
            return writer;
        }

        return super.write( that, writer, nIndentFactor, nIndentBlankNum );
    }

    public Writer write( Properties that, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        writer.write("{");

        UniScopeMap<String, Property > propertyMap = that.getPropertiesMap();
        int nNewIndent          = nIndentBlankNum + nIndentFactor;
        boolean bHasNextElement = false;
        if ( that.getParent() != null ) {
            GenericJSONEncoder.beforeJsonElementWrote( writer, nIndentFactor, nNewIndent, false );
            writer.write("\"__parent__\": ");
            this.write( that.getParent(), writer, nIndentFactor, nNewIndent );
            bHasNextElement = true;
        }

        Iterator<Map.Entry<String, Property > > iter = propertyMap.entrySet().iterator();
        for( ; iter.hasNext(); bHasNextElement = true ) {
            GenericJSONEncoder.beforeJsonElementWrote( writer, nIndentFactor, nNewIndent, bHasNextElement );
            Map.Entry<String, Property > kv = iter.next();

            writer.write( StringUtils.jsonQuote( kv.getKey() ) );
            writer.write(':');
            if ( nIndentFactor > 0 ) {
                writer.write( ' ');
            }

            if( this.mbSimpleEncode ) {
                this.write( kv.getValue().getValue(), writer, nIndentFactor, nIndentBlankNum  );
            }
            else {
                this.write( kv.getValue(), writer, nIndentFactor, nIndentBlankNum  );
            }
        }

        if ( nIndentFactor > 0 ) {
            writer.write( '\n' );
        }

        GenericJSONEncoder.indentBlank( writer, nIndentBlankNum );

        writer.write("}");

        return writer;
    }
}
