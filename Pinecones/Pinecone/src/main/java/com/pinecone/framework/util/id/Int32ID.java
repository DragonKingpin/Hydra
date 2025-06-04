package com.pinecone.framework.util.id;

import com.pinecone.framework.util.Bytes;

public class Int32ID implements NumericID {
    protected int mId;

    public Int32ID( int id ) {
        this.mId = id;
    }

    @Override
    public Identification parse( String hexID ) {
        this.mId = Integer.parseInt( hexID, 16 );
        return this;
    }

    @Override
    public long longVal() {
        return this.mId;
    }

    @Override
    public int intVal() {
        return this.mId;
    }

    @Override
    public String toString() {
        return Integer.toUnsignedString( this.mId );
    }

    @Override
    public byte[] toBytesLE() {
        return Bytes.int32ToBytesLE( this.mId );
    }

    @Override
    public byte[] toBytesBE() {
        return Bytes.int32ToBytesBE( this.mId );
    }

    @Override
    public int sizeof() {
        return Integer.BYTES;
    }

    @Override
    public int compareTo( Identification that ) {
        Int32ID val;
        if ( that instanceof Int32ID ) {
            val = (Int32ID) that;
        }
        else {
            throw new IllegalArgumentException( "Not Int32ID" );
        }

        return Integer.compare( this.mId, val.mId );
    }

}
