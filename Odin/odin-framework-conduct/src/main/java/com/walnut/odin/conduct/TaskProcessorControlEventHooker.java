package com.walnut.odin.conduct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.walnut.odin.dispatch.TaskDispatcher;
import com.walnut.odin.proc.server.transport.RemoteProcessControlEventHooker;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransport;

public class TaskProcessorControlEventHooker implements RemoteProcessControlEventHooker {

    protected final Logger log = LoggerFactory.getLogger( this.getClass() );

    protected final TaskDispatcher mTaskDispatcher;

    public TaskProcessorControlEventHooker( TaskDispatcher taskDispatcher ) {
        this.mTaskDispatcher = taskDispatcher;
    }

    @Override
    public void onClientInitialized( RemoteProcessControlTransport transport, long clientId ) {

    }

    @Override
    public void onClientDetached( RemoteProcessControlTransport transport, long clientId ) {
        if ( this.mTaskDispatcher == null ) {
            return;
        }

        this.mTaskDispatcher.unregisterProcessor( clientId );
        this.log.info(
                "[TaskProcessorControl] [ClientDetached] (ClientId: `{}`) <ProcessorUnregistered>",
                clientId
        );
    }
}
