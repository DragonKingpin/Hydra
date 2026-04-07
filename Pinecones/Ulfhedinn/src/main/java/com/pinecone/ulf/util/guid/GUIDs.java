package com.pinecone.ulf.util.guid;

import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.ulf.util.guid.i128.GUID128;
import com.pinecone.ulf.util.guid.i128.GuidAllocator128V2;
import com.pinecone.ulf.util.guid.i128.GuidAllocator128V7;
import com.pinecone.ulf.util.guid.i128.GuidAllocatorHC128V7;
import com.pinecone.ulf.util.guid.i128.UUID128;
import com.pinecone.ulf.util.guid.i64.GUID64;
import com.pinecone.ulf.util.guid.i64.GUID72;
import com.pinecone.ulf.util.guid.i64.GuidAllocator72V2;
import com.pinecone.ulf.util.guid.i64.worker.WorkerIdAssigner;

public final class GUIDs {
    public static GUID64 GUID64( String s ) {
        return new GUID64( s );
    }

    public static GUID72 GUID72( String s ) {
        return new GUID72( s );
    }

    public static GUID128 GUID128( String s ) {
        UUID128 uuid128 = new UUID128(s);
        return uuid128;
    }

    public static GUID72 Dummy72() {
        return new GUID72();
    }

    public static UUID128 Dummy128() {
        return new UUID128();
    }

    public static GuidAllocator newGuidAllocator( WorkerIdAssigner idAssigner ) {
        if( idAssigner == null ) {
            return new GuidAllocator72V2();
        }
        return new GuidAllocator72V2( idAssigner );
    }

    public static GuidAllocator newGuidAllocator() {
        return newGuidAllocator( 0 );
    }

    public static GuidAllocator newGuidAllocator( int machineId ) {
        if ( machineId <= 0 ) {
            return new GuidAllocator128V7();
        }
        return new GuidAllocatorHC128V7( machineId );
    }
}
