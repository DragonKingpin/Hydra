package com.pinecone.hydra.proc;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ProcessActionTape extends Pinenut {

    Throwable getLastError();

    void setLastError( Throwable lastError );

    int getExitCode();

    void setExitCode( int exitCode );

}
