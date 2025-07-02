package com.pinecone.ulf.util.guid.i128;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.ulf.util.guid.i128.factory.standard.TimeOrderedEpochFactory;

public class GuidAllocatorHC128V7 extends ArchGuidAllocator128 implements GuidAllocator {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    protected TimeOrderedEpochFactory mUuidFactory;

    protected int mnMachineId;

    public GuidAllocatorHC128V7( int machineId ) {
        this.mnMachineId = machineId;

        this.mUuidFactory = new TimeOrderedEpochFactory() ;

        this.log.info( "[GuidAllocatorHC128V7] <machineId: {}>, firstGuid: {}>", machineId, this.nextGUID() );
    }

    @Override
    public GUID nextGUID() {
        long xorMask = ((long) this.mnMachineId & 0xFFFFFFFFL) << 16; // 32 ~ 48
        return this.mUuidFactory.createXorUint64LSB( xorMask );
    }

}
