package com.sauron.radium.system;

import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.services.ArchServer;

public class BasicServer extends ArchServer {
    protected ServersScope serversScope;

    BasicServer( ServersScope scope, JSONObject prototype ) {
        this.serversScope = scope;
        this.extras       = prototype;
        this.serversScope.getServerInjector().typeInject( this.getExtras(), this );
    }
}
