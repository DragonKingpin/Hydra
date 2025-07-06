package com.walnut.odin.proc;

import com.pinecone.hydra.proc.RemoteUProcess;
import com.pinecone.hydra.proc.event.ProcessEvent;
import com.pinecone.hydra.proc.event.ProcessEventHandler;
import com.walnut.odin.proc.dto.UProcessRuntimeMeta;

import java.time.LocalDateTime;


public interface RemoteProcess extends RemoteUProcess {

    long getControlClientId();

    LocalDateTime remoteGetEndTime();

    LocalDateTime remoteGetLastUpdateTime();

    UProcessRuntimeMeta retrieveRemoteRuntimeMeta() throws RemoteProcessLifecycleException;

    void addRemoteEventHandler( ProcessRemoteEventHandler handler ) ;

    void removeRemoteEventHandler( ProcessRemoteEventHandler handler ) ;

    int remoteEventHandlerSize(  ) ;

    void notifyRemoteEvent( long pmClientId, ProcessEvent event, Object caused );

}
