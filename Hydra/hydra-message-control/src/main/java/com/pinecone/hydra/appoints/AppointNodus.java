package com.pinecone.hydra.appoints;

import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.hydra.umc.msg.Messagus;

public interface AppointNodus extends Messagus {

    String getName();

    PatriarchalConfig getConfig();

    void close() ;

    void execute() throws Exception ;

}
