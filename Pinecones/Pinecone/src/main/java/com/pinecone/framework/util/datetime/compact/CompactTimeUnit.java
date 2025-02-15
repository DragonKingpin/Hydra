package com.pinecone.framework.util.datetime.compact;

import com.pinecone.framework.system.prototype.Pinenut;

public interface CompactTimeUnit extends Pinenut {
    boolean isInfinite();

    boolean isMilliseconds() ;

    boolean isSeconds() ;

    boolean isMinutes() ;

    boolean isHours() ;

    boolean isDays() ;

    long toMask64() ;

    String getSymbol();

    short bits();
}
