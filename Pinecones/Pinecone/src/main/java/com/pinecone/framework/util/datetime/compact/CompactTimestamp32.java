package com.pinecone.framework.util.datetime.compact;

public class CompactTimestamp32 implements CompactTimestamp {
    public static final int MASK_TYPE            = 0xE0000000; // Hi   3 => Type
    public static final int MASK_VALUE           = 0x1FFFFFFF; // Low 31 => Time
    public static final int INFINITE             = 0xFFFFFFFF;

    public static final long MILLIS_PER_SECOND   = 1_000L;
    public static final long MILLIS_PER_MINUTE   = 60_000L;
    public static final long MILLIS_PER_HOUR     = 3_600_000L;
    public static final long MILLIS_PER_DAY      = 86_400_000L;

    public static final int BITS                 = Integer.SIZE;


    protected int mnUint32Timestamp;

    public CompactTimestamp32 ( int nUint32Timestamp, boolean raw ) {
        this.mnUint32Timestamp = nUint32Timestamp;
    }

    public CompactTimestamp32 ( int nMillis ) {
        this( nMillis, CompactTimeUnit32.MILLISECONDS );
    }

    public CompactTimestamp32 ( int val, CompactTimeUnit timeUnit ) {
        this( CompactTimestamp32.encode( val, (CompactTimeUnit32) timeUnit ), true );
    }


    @Override
    public long toMilliseconds() {
        return CompactTimestamp32.toMilliseconds( this.mnUint32Timestamp );
    }

    @Override
    public long toSeconds() {
        return CompactTimestamp32.toSeconds( this.mnUint32Timestamp );
    }

    @Override
    public long toMinutes() {
        return CompactTimestamp32.toMinutes( this.mnUint32Timestamp );
    }

    @Override
    public long toHours() {
        return CompactTimestamp32.toHours( this.mnUint32Timestamp );
    }

    @Override
    public long toDays() {
        return CompactTimestamp32.toDays( this.mnUint32Timestamp );
    }

    @Override
    public int toInt32() {
        return CompactTimestamp32.decodeValue( this.mnUint32Timestamp );
    }

    @Override
    public CompactTimeUnit32 getUnit() {
        return CompactTimestamp32.decodeType( this.mnUint32Timestamp );
    }

    @Override
    public boolean isInfinite() {
        return CompactTimestamp32.isInfinite( this.mnUint32Timestamp );
    }

    @Override
    public short bits() {
        return BITS;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null || getClass() != obj.getClass() ) {
            return false;
        }
        CompactTimestamp32 that = ( CompactTimestamp32 ) obj;
        return this.mnUint32Timestamp == that.mnUint32Timestamp;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode( this.mnUint32Timestamp );
    }

    @Override
    public String toString() {
        return CompactTimestamp32.format( this.mnUint32Timestamp );
    }



    public static CompactTimestamp32 from ( int val, CompactTimeUnit timeUnit ) {
        return new CompactTimestamp32( val, timeUnit );
    }

    public static CompactTimestamp32 from ( long millis ) {
        return new CompactTimestamp32( CompactTimestamp32.fromMilliseconds( millis ), true );
    }




    public static int encode( int value, CompactTimeUnit32 unit ) {
        if ( value < 0 || value > MASK_VALUE ) {
            throw new IllegalArgumentException( "Out of rang: " + value );
        }
        return value | unit.getMask();
    }

    public static int decodeValue( int encoded ) {
        if ( encoded == INFINITE ) {
            return INFINITE;
        }
        return encoded & MASK_VALUE;
    }

    public static CompactTimeUnit32 decodeType( int encoded ) {
        if ( encoded == INFINITE ) {
            return CompactTimeUnit32.INFINITE;
        }

        int type = encoded & MASK_TYPE;
        for ( CompactTimeUnit32 unit : CompactTimeUnit32.values() ) {
            if ( unit.getMask() == type ) {
                return unit;
            }
        }

        return null;
    }

    public static long toMilliseconds( int encoded ) {
        if ( CompactTimestamp32.isInfinite( encoded ) ) {
            return -1L;
        }

        int value = CompactTimestamp32.decodeValue( encoded );
        CompactTimeUnit32 unit = CompactTimestamp32.decodeType( encoded );

        if ( unit == null ) {
            throw new IllegalArgumentException( "Unknown `TimeUnit`." );
        }

        switch ( unit ) {
            case MILLISECONDS: {
                return value;
            }
            case SECONDS: {
                return value * MILLIS_PER_SECOND;
            }
            case MINUTES: {
                return value * MILLIS_PER_MINUTE;
            }
            case HOURS: {
                return value * MILLIS_PER_HOUR;
            }
            case DAYS: {
                return value * MILLIS_PER_DAY;
            }
            default: {
                return -1L;
            }
        }
    }

    public static long toSeconds( int val ) {
        long millis = CompactTimestamp32.toMilliseconds( val );
        return millis == -1L ? -1L : millis / MILLIS_PER_SECOND;
    }

    public static long toMinutes( int val ) {
        long millis = CompactTimestamp32.toMilliseconds( val );
        return millis == -1L ? -1L : millis / MILLIS_PER_MINUTE;
    }

    public static long toHours( int val ) {
        long millis = CompactTimestamp32.toMilliseconds( val );
        return millis == -1L ? -1L : millis / MILLIS_PER_HOUR;
    }

    public static long toDays( int val ) {
        long millis = CompactTimestamp32.toMilliseconds( val );
        return millis == -1L ? -1L : millis / MILLIS_PER_DAY;
    }

    public static int fromMilliseconds( long millis ) {
        if ( millis == -1L ) {
            return INFINITE;
        }

        if ( millis < 0 ) {
            throw new IllegalArgumentException( "Negative milliseconds unacceptable." );
        }

        if ( millis % MILLIS_PER_DAY == 0 ) {
            long days = millis / MILLIS_PER_DAY;
            if ( days <= MASK_VALUE ) {
                return CompactTimestamp32.encode( (int) days, CompactTimeUnit32.DAYS );
            }
        }
        if ( millis % MILLIS_PER_HOUR == 0 ) {
            long hours = millis / MILLIS_PER_HOUR;
            if ( hours <= MASK_VALUE ) {
                return CompactTimestamp32.encode( (int) hours, CompactTimeUnit32.HOURS );
            }
        }
        if ( millis % MILLIS_PER_MINUTE == 0 ) {
            long minutes = millis / MILLIS_PER_MINUTE;
            if ( minutes <= MASK_VALUE ) {
                return CompactTimestamp32.encode( (int) minutes, CompactTimeUnit32.MINUTES );
            }
        }
        if ( millis % MILLIS_PER_SECOND == 0 ) {
            long seconds = millis / MILLIS_PER_SECOND;
            if ( seconds <= MASK_VALUE ) {
                return CompactTimestamp32.encode( (int) seconds, CompactTimeUnit32.SECONDS );
            }
        }

        if ( millis <= MASK_VALUE ) {
            return CompactTimestamp32.encode( (int) millis, CompactTimeUnit32.MILLISECONDS );
        }

        return INFINITE;
    }

    public static String format( int encoded ) {
        if ( CompactTimestamp32.isInfinite( encoded ) ) {
            return CompactTimeUnit32.INFINITE.getSymbol();
        }

        CompactTimeUnit32 unit = CompactTimestamp32.decodeType( encoded );
        if ( unit == null ) {
            return CompactTimeUnit32.INFINITE.getSymbol();
        }

        return CompactTimestamp32.decodeValue( encoded ) + " " + unit.getSymbol();
    }

    public static boolean isInfinite( int encoded ) {
        return encoded == INFINITE;
    }

}
