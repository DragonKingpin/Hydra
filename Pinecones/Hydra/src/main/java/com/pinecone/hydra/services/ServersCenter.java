package com.pinecone.hydra.services;

import com.pinecone.hydra.system.HyComponent;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.json.JSONObject;

public interface ServersCenter extends Pinenut, HyComponent {

    JSONObject getNameMap() ;

    JSONObject getNickNameMap() ;

    ServersCenter addServer   ( Server server );

    ServersCenter removeServer( Server server );

    Hydrarum getSystem();
}
