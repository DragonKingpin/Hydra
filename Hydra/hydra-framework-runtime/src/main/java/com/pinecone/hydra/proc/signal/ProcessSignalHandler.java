package com.pinecone.hydra.proc.signal;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ProcessSignalHandler extends Pinenut {

    default boolean supports( ProcSignal signal ) {
        return true;
    }

    SignalHandleResult signal( ProcSignal signal, long graceTimeoutMillis, String reason );
}
