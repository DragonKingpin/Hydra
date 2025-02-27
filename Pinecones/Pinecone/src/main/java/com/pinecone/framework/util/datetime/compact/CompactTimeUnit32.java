package com.pinecone.framework.util.datetime.compact;

public enum CompactTimeUnit32 implements CompactTimeUnit {
    INFINITE     ( 0xFFFFFFFF, "INF"  ),
    MILLISECONDS ( 0x00000000, "ms" ),
    SECONDS      ( 0x20000000, "s"  ),
    MINUTES      ( 0x40000000, "m"  ),
    HOURS        ( 0x60000000, "h"  ),
    DAYS         ( 0x80000000, "d"  );

    private final int    mask;
    private final String symbol;

    CompactTimeUnit32( int mask, String symbol ) {
        this.mask = mask;
        this.symbol = symbol;
    }

    public int getMask() {
        return this.mask;
    }

    @Override
    public String getSymbol() {
        return this.symbol;
    }

    @Override
    public boolean isInfinite() {
        return this.getMask() == CompactTimeUnit32.INFINITE.getMask();
    }

    @Override
    public boolean isMilliseconds() {
        return this.getMask() == CompactTimeUnit32.MILLISECONDS.getMask();
    }

    @Override
    public boolean isSeconds() {
        return this.getMask() == CompactTimeUnit32.SECONDS.getMask();
    }

    @Override
    public boolean isMinutes() {
        return this.getMask() == CompactTimeUnit32.MINUTES.getMask();
    }

    @Override
    public boolean isHours() {
        return this.getMask() == CompactTimeUnit32.HOURS.getMask();
    }

    @Override
    public boolean isDays() {
        return this.getMask() == CompactTimeUnit32.DAYS.getMask();
    }

    @Override
    public long toMask64() {
        return this.getMask();
    }

    @Override
    public short bits() {
        return CompactTimestamp32.BITS;
    }

}
