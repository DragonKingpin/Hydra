package com.pinecone.ulf.util.guid.i128;

import com.pinecone.framework.util.id.GUID;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class GuidAllocator128V5 extends ArchGuidAllocator128 implements GuidAllocator128{
    public final GUID128 NIL = new UUID128(0x0000000000000000L, 0x0000000000000000L);

    @Override
    public GUID nextGUID() {
        return this.v5( this.NIL, "" );
    }

    public GUID v5(GUID128 namespace, String name) {
        return hash(5, "SHA-1", namespace, name);
    }

    /**
     * Returns a name-based unique identifier that uses SHA-1 hashing (UUIDv5).
     * <p>
     * Usage:
     *
     * <pre>{@code
     * GUID guid = GUID.v5(myNameSpace, myBytes);
     * }</pre>
     *
     * @param namespace a GUID (optional)
     * @param bytes     a byte array
     * @return a GUID
     * @throws NullPointerException if the byte array is null
     */
    public GUID v5(GUID128 namespace, byte[] bytes) {
        return hash(5, "SHA-1", namespace, bytes);
    }

    private GUID hash(int version, String algorithm, GUID128 namespace, String name) {
        Objects.requireNonNull(name, "Null name");
        return hash(version, algorithm, namespace, name.getBytes(StandardCharsets.UTF_8));
    }

    private GUID hash(int version, String algorithm, GUID128 namespace, byte[] bytes) {

        Objects.requireNonNull(bytes, "Null bytes");
        MessageDigest hasher = hasher(algorithm);

        if (namespace != null) {
            ByteBuffer ns = ByteBuffer.allocate(16);
            ns.putLong(namespace.getMsb());
            ns.putLong(namespace.getLsb());
            hasher.update(ns.array());
        }

        hasher.update(bytes);
        ByteBuffer hash = ByteBuffer.wrap(hasher.digest());

        final long msb = hash.getLong();
        final long lsb = hash.getLong();

        return version(msb, lsb, version);
    }

    private MessageDigest hasher(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private GUID version(long hi, long lo, int version) {
        // set the 4 most significant bits of the 7th byte
        final long msb = (hi & 0xffff_ffff_ffff_0fffL) | (version & 0xfL) << 12; // RFC 9562 version
        // set the 2 most significant bits of the 9th byte to 1 and 0
        final long lsb = (lo & 0x3fff_ffff_ffff_ffffL) | 0x8000_0000_0000_0000L; // RFC 9562 variant
        return new UUID128(msb, lsb);
    }
}
