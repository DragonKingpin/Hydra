package com.pinecone.hydra.auto;

import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.system.prototype.Pinenut;

public interface ExceptionHandler extends Pinenut {
    void handle( Exception e ) throws ProxyProvokeHandleException, InstantKillException, AbortException, ContinueException;
}
