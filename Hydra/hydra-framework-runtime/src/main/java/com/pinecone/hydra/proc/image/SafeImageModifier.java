package com.pinecone.hydra.proc.image;

import java.util.List;

import com.pinecone.framework.system.Unsafe;
import com.pinecone.hydra.proc.event.ProcessEventHandler;

public class SafeImageModifier implements ImageModifier {
    public SafeImageModifier() {

    }

    protected List<ProcessEventHandler> retrieveSysProcEventHandlers( EntryPointRunnable runnable ) {
        List<ProcessEventHandler> those = ArchEntryPointRunnable.getSysProcEventHandlers( runnable );
        if ( those != null ) {
            return those;
        }
        throw new IllegalArgumentException( "EntryPointRunnable has no SystemProcessEventHandles." );
    }

    @Override
    @Unsafe
    public void addSystemProcessEventHandler( EntryPointRunnable runnable, ProcessEventHandler handler ) {
        List<ProcessEventHandler> those = this.retrieveSysProcEventHandlers( runnable );
        those.add( handler );
    }

    @Override
    @Unsafe
    public void removeSystemProcessEventHandler( EntryPointRunnable runnable, ProcessEventHandler handler ) {
        List<ProcessEventHandler> those = this.retrieveSysProcEventHandlers( runnable );
        those.remove( handler );
    }

    @Override
    @Unsafe
    public int querySystemProcessEventHandlersSize( EntryPointRunnable runnable ) {
        List<ProcessEventHandler> those = this.retrieveSysProcEventHandlers( runnable );
        return those.size();
    }
}
