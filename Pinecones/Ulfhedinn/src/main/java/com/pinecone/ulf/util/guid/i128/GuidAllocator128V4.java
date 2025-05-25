package com.pinecone.ulf.util.guid.i128;

import com.pinecone.framework.util.id.GUID;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class GuidAllocator128V4 extends ArchGuidAllocator128 implements GuidAllocator128 {
    @Override
    public GUID nextGUID() {
        return this.v4();
    }

    public GUID v4() {
        return version(TLRandom.nextLong(), TLRandom.nextLong(), 4);
    }

    /**
     * Returns a random-based unique identifier (UUIDv4).
     * <p>
     * It is equivalent to {@link UUID#randomUUID()}.
     * <p>
     * Usage:
     *
     * <pre>{@code
     * SecureRandom random = new SecureRandom();
     * GUID guid = GUID.v4(random);
     * }</pre>
     *
     * @param random a random generator
     * @return a GUID
     * @throws NullPointerException if the random is null
     */
    public GUID v4(Random random) {
        Objects.requireNonNull(random, "Null random");
        return version(random.nextLong(), random.nextLong(), 4);
    }

    private GUID version(long hi, long lo, int version) {
        // set the 4 most significant bits of the 7th byte
        final long msb = (hi & 0xffff_ffff_ffff_0fffL) | (version & 0xfL) << 12; // RFC 9562 version
        // set the 2 most significant bits of the 9th byte to 1 and 0
        final long lsb = (lo & 0x3fff_ffff_ffff_ffffL) | 0x8000_0000_0000_0000L; // RFC 9562 variant
        return new UUID128(msb, lsb);
    }

    static private class TLRandom {

        // The JVM unique number tries to mitigate the fact that the thread
        // local random is not seeded with a secure random seed by default.
        // Their seeds are based on temporal data and predefined constants.
        // Although the seeds are unique per JVM, they are not across JVMs.
        // It helps to generate different sequences of numbers even if two
        // ThreadLocalRandom are by chance instantiated with the same seed.
        // Of course it doesn't better the output, but doesn't hurt either.
        static final long JVM_UNIQUE_NUMBER = new SecureRandom().nextLong();

        static private long nextLong() {
            return ThreadLocalRandom.current().nextLong() ^ JVM_UNIQUE_NUMBER;
        }
    }
}
