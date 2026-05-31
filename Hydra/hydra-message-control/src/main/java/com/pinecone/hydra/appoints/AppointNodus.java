package com.pinecone.hydra.appoints;

import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.hydra.umc.msg.CloseableMessgus;

public interface AppointNodus extends CloseableMessgus {

    String getName();

    PatriarchalConfig getConfig();

    void close() ;

    void execute() throws Exception ;

}
