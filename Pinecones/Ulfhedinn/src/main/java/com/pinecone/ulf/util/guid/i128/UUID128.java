package com.pinecone.ulf.util.guid.i128;

import com.pinecone.framework.util.id.Identification;

import java.util.UUID;

public class UUID128 implements GUID128 {
    /**
     * The most significant bits.
     */
    private long msb;
    /**
     * The least significant bits.
     */
    private long lsb;

    public UUID128( long mostSignificantBits, long leastSignificantBits ) {
        this.msb = mostSignificantBits;
        this.lsb = leastSignificantBits;
    }

    public UUID128( String hexId ) {
        this.parse( hexId );
    }

    @Override
    public Identification parse( String hexID ) {
        return ArchGuidAllocator128.Parser.parse( hexID );
    }

    @Override
    public long getMsb() {
        return this.msb;
    }

    @Override
    public long getLsb() {
        return this.lsb;
    }

    @Override
    public String toString() {
        return fastUUID( this.lsb, this.msb );
    }

    @Override
    public String toJSONString() {
        return "\"" + this.toString() + "\"";
    }

    public static String fastUUID(long leastSigBits, long mostSigBits) {
        char[] uuidChars = new char[36];
        hexDigits(uuidChars, 0, mostSigBits >>> 32, 8);
        uuidChars[8] = '-';
        hexDigits(uuidChars, 9, mostSigBits >>> 16, 4);
        uuidChars[13] = '-';
        hexDigits(uuidChars, 14, mostSigBits, 4);
        uuidChars[18] = '-';
        hexDigits(uuidChars, 19, leastSigBits >>> 48, 4);
        uuidChars[23] = '-';
        hexDigits(uuidChars, 24, leastSigBits, 12);
        return new String(uuidChars);
    }

    private static final char[] HEX_DIGITS = {
            '0','1','2','3','4','5','6','7',
            '8','9','a','b','c','d','e','f'
    };

    private static void hexDigits(char[] dest, int offset, long val, int digits) {
        for (int i = offset + digits - 1, shift = 0; i >= offset; i--, shift +=4) {
            dest[i] = HEX_DIGITS[(int)((val >>> shift) & 0xF)];
        }
    }

    @Override
    public UUID toUUID() {
        return new UUID( this.msb, this.lsb );
    }

    @Override
    public int version() {
        return 0;
    }

    @Override
    public int variant() {
        return 0;
    }


}
