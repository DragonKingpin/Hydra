package com.pinecone.ulf.util.guid.i128;

import com.pinecone.framework.util.id.GUID;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.LongSupplier;

public class GuidAllocator128V7 extends ArchGuidAllocator128 implements GuidAllocator128{
    private  final long MASK_12 = 0x0000_0000_0000_0fffL;

    @Override
    public GUID nextGUID() {
        return this.v7();
    }

    /**
     * Returns a Unix epoch time-based unique identifier (UUIDv7).
     * <p>
     * It uses {@link ThreadLocalRandom} as random number generator.
     * <p>
     * Usage:
     *
     * <pre>{@code
     * GUID guid = GUID.v7();
     * }</pre>
     *
     * @return a GUID
     */
    public GUID v7() {
        return v7(System::currentTimeMillis, TLRandom::nextLong);
    }

    /**
     * Returns a Unix epoch time-based unique identifier (UUIDv7).
     * <p>
     * Usage:
     *
     * <pre>{@code
     * SecureRandom random = new SecureRandom();
     * GUID guid = GUID.v7(Instant.now(), random);
     * }</pre>
     *
     * @param instant an instant (optional)
     * @param random  a random generator (optional)
     * @return a GUID
     */
    public GUID v7(Instant instant, Random random) {
        return v7(optional(instant), optional(random));
    }

    private GUID v7(LongSupplier msec, LongSupplier random) {
        final long time = msec.getAsLong();
        final long msb = (time << 16) | (TLRandom.nextLong() & MASK_12);
        final long lsb = random.getAsLong();
        return version(msb, lsb, 7);
    }

    private LongSupplier optional(Instant instant) {
        return instant == null ? System::currentTimeMillis : instant::toEpochMilli;
    }

    private LongSupplier optional(Random random) {
        return random == null ? TLRandom::nextLong : random::nextLong;
    }

    private long gregorian(final long millisecons) {
        // 1582-10-15T00:00:00Z
        final long factor = 10_000L;
        final long offset = 12219292800000L;
        return ((millisecons + offset) * factor);
    }

    GUID version(long hi, long lo, int version) {
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
