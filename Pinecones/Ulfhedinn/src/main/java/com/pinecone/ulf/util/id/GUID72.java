package com.pinecone.ulf.util.id;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.IllegalIdentificationException;

public class GUID72 extends GUID64 {
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
    public String toString() {
        String nanoSeedHex = String.format( "%02x", this.nanoSeed      );
        return super.toString() + "-" + nanoSeedHex;
    }

    @Override
    public GUID72 parse( String hexID72 ) throws IllegalIdentificationException {
        Debug.trace( "解析字符串"+hexID72 );
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
}