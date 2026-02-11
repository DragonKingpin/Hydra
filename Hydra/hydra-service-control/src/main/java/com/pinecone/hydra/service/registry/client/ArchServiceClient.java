package com.pinecone.hydra.service.registry.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.service.registry.ServiceControlRPCException;

public abstract class ArchServiceClient implements ServiceClient {
    protected Logger          mLogger;

    protected GuidAllocator   mGuidAllocator;

    protected GUID            mServiceId;

    protected GUID            mInstanceId;

    public ArchServiceClient( @Nullable GUID serviceId, GuidAllocator guidAllocator ) {
        this.mLogger                = LoggerFactory.getLogger( this.getClass() );
        this.mGuidAllocator         = guidAllocator;
        this.mServiceId             = serviceId;
    }

    public ArchServiceClient( GuidAllocator guidAllocator ) {
        this( null, guidAllocator );
    }

    @Override
    public void startService() throws ServiceControlRPCException {
        this.initRPCSubsystem();
    }

    @Override
    public GuidAllocator getGuidAllocator() {
        return this.mGuidAllocator;
    }

    protected abstract void initRPCSubsystem() throws ServiceControlRPCException ;

}