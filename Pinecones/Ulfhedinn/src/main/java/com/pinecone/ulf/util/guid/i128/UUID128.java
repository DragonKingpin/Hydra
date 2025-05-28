package com.pinecone.ulf.util.guid.i128;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;

import java.util.UUID;

public class UUID128 implements GUID128 {
    /**
     * The most significant bits.
     */
    long mostSigBits;
    /**
     * The least significant bits.
     */
    long leastSigBits;

    public UUID128 () {
        this( 0, 0  );
    }

    public UUID128( long mostSignificantBits, long leastSignificantBits ) {
        this.mostSigBits = mostSignificantBits;
        this.leastSigBits = leastSignificantBits;
    }

    public UUID128( String hexId ) {
        ArchGuidAllocator128.Parser.parse( hexId, this );
    }

    @Override
    public Identification parse( String hexID ) {
        ArchGuidAllocator128.Parser.parse( hexID, this );
        return this;
    }

    @Override
    public long getMostSignificantBits() {
        return this.mostSigBits;
    }

    @Override
    public long getLeastSignificantBits() {
        return this.leastSigBits;
    }

    @Override
    public String toString() {
        return stringify( this.leastSigBits, this.mostSigBits );
    }

    @Override
    public String toJSONString() {
        return "\"" + this.toString() + "\"";
    }

    public static String stringify( long leastSigBits, long mostSigBits ) {
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

    private static void hexDigits( char[] dest, int offset, long val, int digits ) {
        for (int i = offset + digits - 1, shift = 0; i >= offset; i--, shift +=4) {
            dest[i] = HEX_DIGITS[(int)((val >>> shift) & 0xF)];
        }
    }

    @Override
    public UUID toUUID() {
        return new UUID( this.mostSigBits, this.leastSigBits );
    }

    @Override
    public int version() {
        // Version is bits masked by 0x000000000000F000 in MS long
        return (int)((this.mostSigBits >> 12) & 0x0f);
    }

    @Override
    public int variant() {
        // This field is composed of a varying number of bits.
        // 0    -    -    Reserved for NCS backward compatibility
        // 1    0    -    The IETF aka Leach-Salz variant (used by this class)
        // 1    1    0    Reserved, Microsoft backward compatibility
        // 1    1    1    Reserved for future definition.
        return (int) ((this.leastSigBits >>> (64 - (this.leastSigBits >>> 62)))
                & (this.leastSigBits >> 63));
    }

    @Override
    public int clockSequence() {
        if (version() != 1) {
            throw new UnsupportedOperationException("Not a time-based GUID");
        }

        return (int)((this.leastSigBits & 0x3FFF000000000000L) >>> 48);
    }

    @Override
    public long node() {
        if (version() != 1) {
            throw new UnsupportedOperationException("Not a time-based UUID");
        }

        return this.leastSigBits & 0x0000FFFFFFFFFFFFL;
    }

    @Override
    public int hashCode() {
        long hilo = this.mostSigBits ^ this.leastSigBits;
        return ((int)(hilo >> 32)) ^ (int) hilo;
    }

    @Override
    public boolean equals(Object obj) {
        if ( !(obj instanceof GUID128) ) {
            return false;
        }
        GUID128 id = (GUID128)obj;
        return (
                this.mostSigBits == id.getMostSignificantBits() &&
                this.leastSigBits == id.getLeastSignificantBits()
        );
    }

    @Override
    public int compareTo( Identification that ) {
        GUID128 val;
        if ( that instanceof GUID128 ) {
            val = (GUID128) that;
        }
        else {
            throw new IllegalArgumentException( "Not GUID128" );
        }

        // The ordering is intentionally set up so that the UUIDs
        // can simply be numerically compared as two numbers
        return (
                this.mostSigBits < val.getMostSignificantBits() ? -1 :
                (
                        this.mostSigBits > val.getMostSignificantBits() ? 1 :
                        (
                                this.leastSigBits < val.getLeastSignificantBits() ? -1 :
                                (
                                        this.leastSigBits > val.getLeastSignificantBits() ? 1 :
                                        0
                                )
                        )
                )
        );
    }


}
