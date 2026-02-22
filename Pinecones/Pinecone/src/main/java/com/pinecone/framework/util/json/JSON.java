package com.pinecone.framework.util.json;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;

public final class JSON {
    public static final Object NULL      = new JSON.Null();


    public static Object parse     ( String szJsonString ) {
        return ( new JSONCursorParser( szJsonString ) ).nextValue();
    }

    public static String stringify ( Object that ) {
        return JSON.encode( that, JSONEncoder.BASIC_JSON_ENCODER );
    }

    public static String stringify ( Object that, int nIndentFactor ) {
        return JSON.encode( that, nIndentFactor, JSONEncoder.BASIC_JSON_ENCODER );
    }

    public static String marshal   ( Object that ) {
        return JSON.encode( that, JSONEncoder.BASIC_JSON_MARSHAL );
    }

    public static String marshal   ( Object that, boolean bOnlyMarshalAnnotated ) {
        return JSON.encode( that, new JSONMarshal( bOnlyMarshalAnnotated ) );
    }

    public static <T> T unmarshal ( String szJsonString, Class<T > classType ) {
        ObjectJSONCursorUnmarshal unmarshal = new ObjectJSONCursorUnmarshal( szJsonString, classType );
        return classType.cast( unmarshal.nextValue() ) ;
    }

    public static <T> T unmarshal ( Reader reader, Class<T > classType ) {
        ObjectJSONCursorUnmarshal unmarshal = new ObjectJSONCursorUnmarshal( reader, classType );
        return classType.cast( unmarshal.nextValue() ) ;
    }

    @SuppressWarnings( "unchecked" )
    public static <T> T unmarshal( String json, TypeReference<T> typeRef ) {
        ObjectJSONCursorUnmarshal u = new ObjectJSONCursorUnmarshal( json, typeRef.getType() );
        return (T) u.nextValue();
    }

    @SuppressWarnings( "unchecked" )
    public static <T> T unmarshal ( Reader reader, TypeReference<T > typeRef ) {
        ObjectJSONCursorUnmarshal u = new ObjectJSONCursorUnmarshal( reader, typeRef.getType() );
        return (T) u.nextValue();
    }



    public static String encode    ( Object that, JSONEncoder encoder ) {
        return JSON.encode( that, 0, encoder );
    }

    public static String encode    ( Object that, int nIndentFactor, JSONEncoder encoder ) {
        StringWriter w = new StringWriter();
        try {
            synchronized( w.getBuffer() ) {
                return encoder.write( that, w, nIndentFactor,0 ).toString();
            }
        }
        catch ( IOException e ){
            return null;
        }
    }


    public static final class Null {
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
            return this.toJSONString();
        }

        public String toJSONString() {
            return "null";
        }
    }
}
