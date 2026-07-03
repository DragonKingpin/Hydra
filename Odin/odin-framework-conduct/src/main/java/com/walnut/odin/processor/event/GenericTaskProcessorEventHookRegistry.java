package com.walnut.odin.processor.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.walnut.odin.processor.event.TaskProcessorEvent;
import com.walnut.odin.processor.event.TaskProcessorEventHookRegistry;
import com.walnut.odin.processor.event.TaskProcessorEventHooker;

public class GenericTaskProcessorEventHookRegistry implements TaskProcessorEventHookRegistry {

    protected final Logger log = LoggerFactory.getLogger( this.getClass() );

    protected final ReentrantLock                 mLock;
    protected final Set<TaskProcessorEventHooker> mHookers;

    public GenericTaskProcessorEventHookRegistry() {
        this.mLock    = new ReentrantLock();
        this.mHookers = new LinkedHashSet<>();
    }

    @Override
    public void addHooker( TaskProcessorEventHooker hooker ) {
        if ( hooker == null ) {
            return;
        }

        this.mLock.lock();
        try {
            this.mHookers.add( hooker );
        }
        finally {
            this.mLock.unlock();
        }
    }

    @Override
    public void removeHooker( TaskProcessorEventHooker hooker ) {
        if ( hooker == null ) {
            return;
        }

        this.mLock.lock();
        try {
            this.mHookers.remove( hooker );
        }
        finally {
            this.mLock.unlock();
        }
    }

    @Override
    public Collection<TaskProcessorEventHooker> hookers() {
        this.mLock.lock();
        try {
            return Collections.unmodifiableCollection( new ArrayList<>( this.mHookers ) );
        }
        finally {
            this.mLock.unlock();
        }
    }

    @Override
    public void dispatch( TaskProcessorEvent event ) {
        Collection<TaskProcessorEventHooker> hookers = this.hookers();
        for ( TaskProcessorEventHooker hooker : hookers ) {
            try {
                hooker.onTaskProcessorEvent( event );
            }
            catch ( Exception e ) {
                this.log.warn(
                        "[TaskProcessorEvent] [HookerFailed] (Hooker: `{}`, Type: `{}`, ClientId: `{}`) <Ignored>",
                        hooker.getClass().getName(),
                        event == null ? null : event.getType(),
                        event == null ? 0 : event.getClientId(),
                        e
                );
            }
        }
    }
}
