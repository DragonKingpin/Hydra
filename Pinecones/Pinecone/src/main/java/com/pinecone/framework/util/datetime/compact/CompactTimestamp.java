package com.pinecone.framework.util.datetime.compact;

import com.pinecone.framework.system.prototype.Pinenut;

public interface CompactTimestamp extends Pinenut {
    long toMilliseconds();

    long toSeconds();

    long toMinutes();

    long toHours();

    long toDays();

    int toInt32();

    CompactTimeUnit getUnit();

    boolean isInfinite();

    short bits();
}
