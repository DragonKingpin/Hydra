package com.pinecone.hydra.servgram;

import com.pinecone.framework.system.RuntimeSystem;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.config.PatriarchalConfig;

public interface Servgram extends Pinenut {
    String getName();

    default String gramName(){
        return this.className();
    }

    PatriarchalConfig getConfig();

    RuntimeSystem getSystem();

    void terminate() ;

    void execute() throws Exception ;

    // Who summoned me ?
    ServgramOrchestrator getAttachedOrchestrator();

    String ConfigServgramsKey = "Servgrams";
}
