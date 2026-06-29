package com.walnut.odin.conduct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.walnut.odin.dispatch.TaskDispatcher;
import com.walnut.odin.proc.server.transport.RemoteProcessControlEventHooker;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransport;
import com.walnut.odin.processor.runtime.TaskProcessorUnregisterResult;

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

        TaskProcessorUnregisterResult result = this.mTaskDispatcher.processorRuntime().unregister( clientId );
        this.log.info(
                "[TaskProcessorControl] [ClientDetached] (ClientId: `{}`, Removed: `{}`) <ProcessorUnregistered>",
                clientId, result != null && result.hasAny()
        );
    }
}
