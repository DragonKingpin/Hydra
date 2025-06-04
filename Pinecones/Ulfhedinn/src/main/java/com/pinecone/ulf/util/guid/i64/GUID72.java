package com.pinecone.ulf.util.guid.i64;

import com.pinecone.framework.util.id.IllegalIdentificationException;

public class GUID72 extends GUID64 {
    public static final int  Sizeof        = 9; // 8 bytes for GUID64 + 1 byte for nanoSeed

    private byte nanoSeed;

    public GUID72() {

    }

    public GUID72( String hexID72 ) {
        this.parse( hexID72 );
    }

    public GUID72( long guid64, byte nanoSeed ) {
        super( guid64 );
        this.nanoSeed = nanoSeed;
    }

    public int getNanoSeed() {
        return this.nanoSeed;
    }

    public void setNanoSeed( byte nanoSeed ) {
        this.nanoSeed = nanoSeed;
    }

    @Override
    public GUID72 parse( String hexID72 ) throws IllegalIdentificationException {
        //Debug.trace( "解析字符串"+hexID72 );
        try{
            String[] parts = hexID72.split("-");
            this.parseByStringParts( parts );
            this.nanoSeed  = (byte) Integer.parseInt( parts[3], 16 );
        }
        catch ( NumberFormatException | IndexOutOfBoundsException e ) {
            throw new IllegalIdentificationException( e );
        }

        return this;
    }

    @Override
    public byte[] toBytesLE() {
        byte[] b = new byte[9];

        b[0] = (byte)  this.guid;
        b[1] = (byte) (this.guid >> 8);
        b[2] = (byte) (this.guid >> 16);
        b[3] = (byte) (this.guid >> 24);
        b[4] = (byte) (this.guid >> 32);
        b[5] = (byte) (this.guid >> 40);
        b[6] = (byte) (this.guid >> 48);
        b[7] = (byte) (this.guid >> 56);
        b[8] = this.nanoSeed;

        return b;
    }

    @Override
    public byte[] toBytesBE() {
        byte[] b = new byte[9];

        b[0] = (byte) (this.guid >> 56);
        b[1] = (byte) (this.guid >> 48);
        b[2] = (byte) (this.guid >> 40);
        b[3] = (byte) (this.guid >> 32);
        b[4] = (byte) (this.guid >> 24);
        b[5] = (byte) (this.guid >> 16);
        b[6] = (byte) (this.guid >> 8);
        b[7] = (byte)  this.guid;
        b[8] = this.nanoSeed;

        return b;
    }

    @Override
    public String toString() {
        String nanoSeedHex = String.format( "%02x", this.nanoSeed      );
        return super.toString() + "-" + nanoSeedHex;
    }

    @Override
    public int sizeof() {
        return Sizeof;
    }

    @Override
    public String toJSONString() {
        return "\"" + this.toString() + "\"";
    }

    @Override
    public boolean equals( Object obj ) {
        boolean b = false;
        if( obj instanceof GUID72 ) {
            b = this.nanoSeed == ((GUID72) obj).nanoSeed;
        }

        return super.equals(obj) && b;
    }

    @Override
    public int hashCode() {
        return Long.hashCode( this.guid ) ^ Byte.hashCode( this.nanoSeed );
    }

    @Override
    public long hashCode64() {
        return super.hashCode64() ^ Byte.hashCode( this.nanoSeed );
    }
}
