package com.pinecone.ulf.util.bson;

import com.pinecone.framework.util.Bytes;
import com.pinecone.framework.util.json.JSON;
import com.pinecone.framework.util.json.JSONCompilerException;
import com.pinecone.framework.util.json.JSONDecompiler;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public abstract class ArchJSONDecompiler implements JSONDecompiler {
    protected int             mnParseAt ;
    protected InputStream     mInputStream;

    public ArchJSONDecompiler( InputStream is ) {
        this.mInputStream = is;
        this.mnParseAt    = 0;
    }

    protected int nextByte() throws JSONCompilerException {
        try {
            int b = this.mInputStream.read();
            if( b < 0 ) {
                throw new JSONCompilerException( "Illegal decompiled byte or IO error.", this.mnParseAt );
            }
            ++this.mnParseAt;
            return b;
        }
        catch ( IOException e ){
            throw new JSONCompilerException( e, this.mnParseAt );
        }
    }

    protected byte[] nextBytes( int length ) throws JSONCompilerException {
        byte[] bytes = new byte[ length ];
        try {
            int read = this.mInputStream.read( bytes );
            if ( read != length ) {
                throw new JSONCompilerException( "Unexpected end of stream.", this.mnParseAt );
            }
            this.mnParseAt += length;
            return bytes;
        }
        catch ( IOException e ) {
            throw new JSONCompilerException( e, this.mnParseAt );
        }
    }

    protected String nextString() throws JSONCompilerException {
        int   length = Bytes.bytesToInt32LE( this.nextBytes( 4 ) );
        byte[] bytes = this.nextBytes( length );
        return new String( bytes );
    }

    protected short nextInt16() throws JSONCompilerException {
        return Bytes.bytesToInt16LE( this.nextBytes(2) );
    }

    protected int nextInt32() throws JSONCompilerException {
        return Bytes.bytesToInt32LE( this.nextBytes(4) );
    }

    protected long nextInt64() throws JSONCompilerException {
        return Bytes.bytesToInt64LE( this.nextBytes(8) );
    }

    protected float nextFloat32() throws JSONCompilerException {
        return Bytes.bytesToFloat32LE( this.nextBytes(4) );
    }

    protected double nextFloat64() throws JSONCompilerException {
        return Bytes.bytesToFloat64LE( this.nextBytes(8) );
    }

    protected boolean nextBool() throws JSONCompilerException {
        return this.nextByte() != 0;
    }

    protected BigInteger nextBigInteger() throws JSONCompilerException {
        int   length = Bytes.bytesToInt32LE( this.nextBytes(4) );
        byte[] bytes = this.nextBytes(length);
        return new BigInteger(bytes);
    }

    protected BigDecimal nextBigDecimal() throws JSONCompilerException {
        int   length = Bytes.bytesToInt32LE( this.nextBytes(4) );
        byte[] bytes = nextBytes(length);
        int    scale = Bytes.bytesToInt32LE( this.nextBytes(4) );
        return new BigDecimal( new BigInteger(bytes), scale );
    }

    protected abstract List<Object > newJSONArray( Object parent );

    protected abstract Map<String, Object > newJSONObject( Object parent );

    protected Object nextJSONObject( Object parent ) throws JSONCompilerException {
        Map<String, Object > map = this.newJSONObject( parent );
        int length = (int) Bytes.bytesToInt64LE( this.nextBytes(8) );
        for ( int i = 0; i < length; ++i ) {
            Object k     = this.nextValue();
            String key ;
            if( !(k instanceof String) ) {
                throw new JSONCompilerException( "Illegal JSONObject::Key, key should be String.", this.mnParseAt );
            }
            key          = (String) k;
            Object value = this.nextValue( map );
            map.put( key, value );
        }
        int endType = this.nextByte();
        if ( endType != DataTypeCode.JSONOBJECT_END.getValue() ) {
            throw new JSONCompilerException("Expected end of JSON object.", this.mnParseAt);
        }
        return map;
    }

    protected Object nextJSONArray( Object parent ) throws JSONCompilerException {
        List<Object > list = this.newJSONArray( parent );
        int length = (int) Bytes.bytesToInt64LE( this.nextBytes(8) );
        for ( int i = 0; i < length; ++i ) {
            Object value = this.nextValue( list );
            list.add( value );
        }
        int endType = this.nextByte();
        if ( endType != DataTypeCode.JSONARRAY_END.getValue() ) {
            throw new JSONCompilerException( "Expected end of JSON array.", this.mnParseAt );
        }
        return list;
    }

    protected Object nextUnidentifiedObject( int type ) throws JSONCompilerException {
        throw new JSONCompilerException( "Unidentified compiled bytecode `[0x" + Integer.toHexString( type ).toUpperCase() + "]`, with unknown version or damaged binary data.", this.mnParseAt );
    }

    @Override
    public Object nextValue( Object parent ) throws JSONCompilerException {
        int type = this.nextByte();

        try{
            DataTypeCode typeCode = DataTypeCode.asCode( type );

            switch ( typeCode ) {
                case NULL:
                case UNDEFINED: {
                    return JSON.NULL;
                }
                case STRING: {
                    return this.nextString();
                }
                case BYTE8: {
                    return this.nextByte();
                }
                case INT16: {
                    return this.nextInt16();
                }
                case INT32: {
                    return this.nextInt32();
                }
                case INT64: {
                    return this.nextInt64();
                }
                case FLOAT32: {
                    return this.nextFloat32();
                }
                case FLOAT64: {
                    return this.nextFloat64();
                }
                case BOOL: {
                    return this.nextBool();
                }
                case BIG_INTEGER: {
                    return this.nextBigInteger();
                }
                case BIG_DECIMAL: {
                    return this.nextBigDecimal();
                }
                case JSONOBJECT: {
                    return this.nextJSONObject( parent );
                }
                case JSONARRAY: {
                    return this.nextJSONArray( parent );
                }
                default: {
                    return this.nextUnidentifiedObject( type );
                }
            }
        }
        catch ( IllegalArgumentException e ) {
            return this.nextUnidentifiedObject( type );
        }
    }

    @Override
    public Object nextValue() throws JSONCompilerException {
        return this.nextValue( null );
    }

    @Override
    public Object decompile( Object parent ) {
        try{
            return this.nextValue( parent );
        }
        catch ( JSONCompilerException e ) {
            return null;
        }
    }

    @Override
    public Object decompile() {
        return this.decompile( null );
    }
}
