package com.pinecone.framework.system.executum;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.concurrent.TimeUnit;

public interface Chronum extends Pinenut {
    long getStartNano();

    default long getStartTime( TimeUnit unit ) {
        return unit.convert( this.getStartNano(), TimeUnit.NANOSECONDS );
    }

    default long getStartMillis() {
        return this.getStartTime( TimeUnit.MILLISECONDS );
    }

    default long getExecutedNano() {
        return System.nanoTime() - this.getStartNano();
    }

    default long getExecutedTime( TimeUnit unit) {
        long executedNano = this.getExecutedNano();
        return unit.convert(executedNano, TimeUnit.NANOSECONDS);
    }

    default long getExecutedMillis() {
        return this.getExecutedTime(TimeUnit.MILLISECONDS);
    }
}
