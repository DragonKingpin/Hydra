package com.pinecone.ulf.util.guid.i128;

import com.pinecone.framework.util.id.GUID;

public class GuidAllocator128V2 extends ArchGuidAllocator128 implements GuidAllocator128{
    private GuidAllocator128 v1 = new GuidAllocator128V1();

    private final long MASK_32 = 0x0000_0000_ffff_ffffL;

    private final long MASK_08 = 0x0000_0000_0000_00ffL;

    @Override
    public GUID nextGUID() {
        return this.v2((byte) 0, (int) 0);
    }

    public GUID v2(byte localDomain, int localIdentifier) {
        return v2(localDomain, localIdentifier, (GUID128) v1.nextGUID());
    }

    private GUID v2(byte localDomain, int localIdentifier, GUID128 guid) {
        final long msb = (guid.getMsb() & MASK_32) | ((localIdentifier & MASK_32) << 32);
        final long lsb = (guid.getLsb() & 0x3f00_ffff_ffff_ffffL) | ((localDomain & MASK_08) << 48);
        return version(msb, lsb, 2);
    }

     GUID version(long hi, long lo, int version) {
        // set the 4 most significant bits of the 7th byte
        final long msb = (hi & 0xffff_ffff_ffff_0fffL) | (version & 0xfL) << 12; // RFC 9562 version
        // set the 2 most significant bits of the 9th byte to 1 and 0
        final long lsb = (lo & 0x3fff_ffff_ffff_ffffL) | 0x8000_0000_0000_0000L; // RFC 9562 variant
        return new UUID128(msb, lsb);
    }
}
