package com.pinecone.framework.util.id;

import com.pinecone.framework.util.Bytes;

public class Int64ID implements NumericID {
    protected long mId;

    public Int64ID( long id ) {
        this.mId = id;
    }

    @Override
    public Identification parse( String hexID ) {
        this.mId = Long.parseLong( hexID, 16 );
        return this;
    }

    @Override
    public long longVal() {
        return this.mId;
    }

    @Override
    public int intVal() {
        return (int) this.mId;
    }

    @Override
    public String toString() {
        return Long.toUnsignedString( this.mId );
    }

    @Override
    public byte[] toBytesLE() {
        return Bytes.int64ToBytesLE( this.mId );
    }

    @Override
    public byte[] toBytesBE() {
        return Bytes.int64ToBytesBE( this.mId );
    }

    @Override
    public int sizeof() {
        return Long.BYTES;
    }

    @Override
    public int compareTo( Identification that ) {
        Int64ID val;
        if ( that instanceof Int64ID ) {
            val = (Int64ID) that;
        }
        else {
            throw new IllegalArgumentException( "Not Int64ID" );
        }

        return Long.compare( this.mId, val.mId );
    }
}