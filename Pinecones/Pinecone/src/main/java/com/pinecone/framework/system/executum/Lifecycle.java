package com.pinecone.framework.system.executum;

import com.pinecone.framework.system.ApoptosisRejectSignalException;
import com.pinecone.framework.system.prototype.Pinenut;

public interface Lifecycle extends Pinenut {
    void          apoptosis() throws ApoptosisRejectSignalException; // Notify you should die, but you can chose to be the cancer that refuse to die.

    void          kill(); // Just kill you, the darkness comes...

    void          interrupt();

    void          suspend();

    void          resume();

    void          entreatLive(); // Before you die.

    Thread.State  getState();

    int           getExceptionRestartTime();

    Lifecycle     applyExceptionRestartTime( int time );
}
