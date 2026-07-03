package com.walnut.odin.processor.anonymous;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import com.walnut.odin.processor.anonymous.AnonymousTaskProcessorMetadataParser;
import com.walnut.odin.processor.anonymous.AnonymousTaskProcessorRegistration;
import com.walnut.odin.processor.anonymous.AnonymousTaskProcessorRegistry;

public class RavenAnonymousTaskProcessorRegistry implements AnonymousTaskProcessorRegistry {

    protected final ReentrantLock mLock;

    protected final Map<Long, AnonymousTaskProcessorRegistration> mRegistrations;

    protected AnonymousTaskProcessorMetadataParser mMetadataParser;

    public RavenAnonymousTaskProcessorRegistry( AnonymousTaskProcessorMetadataParser metadataParser ) {
        this.mLock          = new ReentrantLock();
        this.mRegistrations = new LinkedHashMap<>();
        this.mMetadataParser = metadataParser == null
                ? new GenericAnonymousTaskProcessorMetadataParser()
                : metadataParser;
    }

    public RavenAnonymousTaskProcessorRegistry() {
        this( new GenericAnonymousTaskProcessorMetadataParser() );
    }

    @Override
    public AnonymousTaskProcessorRegistration register( String szNodeName, long nClientId, Map<String, String> metadata ) {
        AnonymousTaskProcessorRegistration registration = new GenericAnonymousTaskProcessorRegistration(
                szNodeName,
                nClientId,
                this.mMetadataParser.alias( metadata ),
                this.mMetadataParser.bizPath( metadata ),
                this.mMetadataParser.runtime( metadata ),
                this.mMetadataParser.execCaps( metadata ),
                metadata
        );

        this.mLock.lock();
        try {
            this.mRegistrations.put( nClientId, registration );
            return registration;
        }
        finally {
            this.mLock.unlock();
        }
    }

    @Override
    public AnonymousTaskProcessorRegistration unregister( long nClientId ) {
        this.mLock.lock();
        try {
            return this.mRegistrations.remove( nClientId );
        }
        finally {
            this.mLock.unlock();
        }
    }

    @Override
    public AnonymousTaskProcessorRegistration getByClientId( long nClientId ) {
        this.mLock.lock();
        try {
            return this.mRegistrations.get( nClientId );
        }
        finally {
            this.mLock.unlock();
        }
    }

    @Override
    public Collection<AnonymousTaskProcessorRegistration> snapshot() {
        this.mLock.lock();
        try {
            return Collections.unmodifiableCollection( new ArrayList<>( this.mRegistrations.values() ) );
        }
        finally {
            this.mLock.unlock();
        }
    }
}
