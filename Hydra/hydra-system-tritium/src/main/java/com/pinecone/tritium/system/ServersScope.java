package com.pinecone.tritium.system;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.system.HyComponent;
import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.DirectObjectInjector;
import com.pinecone.hydra.server.ArchServer;
import com.pinecone.hydra.server.ArchServersCenter;
import com.pinecone.hydra.server.Server;
import com.pinecone.hydra.system.Hydrogen;
import com.pinecone.tritium.util.ConfigHelper;

import java.io.IOException;
import java.util.Map;

public class ServersScope extends ArchServersCenter implements Pinenut {
    protected DirectObjectInjector mServerInjector;

    public ServersScope(Namespace name, Hydrogen system, HyComponent parent ) {
        super( name, system, parent );

        this.mServerInjector = new DirectObjectInjector( ConfigHelper.fnToSmallHumpName, ArchServer.class ) ;
        this.fetchAll();
        this.reinterpret();
    }

    public ServersScope(Hydrogen system, HyComponent parent ) {
        this( null, system, parent );
    }

    public ServersScope( Hydrogen system ) {
        this( system, null );
    }


    @Override
    public TritiumSystem getSystem() {
        return (TritiumSystem) super.getSystem();
    }

    @Override
    protected void  loadConfig() {
        JSONConfig sys  = (JSONConfig) this.getSystem().getSystemConfig();
        Object jServers = sys.opt( "Servers" );
        if( jServers instanceof String ) {
            try {
                this.serversConfig = sys.fromFile( this.getSystem().getWorkingPath().resolve( (String) jServers ).toFile()  );
            }
            catch ( IOException e ) {
                this.getSystem().handleKillException( e );
            }
        }
        else {
            this.serversConfig = (JSONObject) jServers;
        }

        sys.put( "Servers", this.serversConfig );
    }

    protected void  reinterpret() {
        for ( Map.Entry<String,Object > kv: this.getNickNameMap().entrySet() ) {
            this.getSystem().getGlobalConfigScope().put( kv.getKey(), ( (BasicServer)kv.getValue() ).getLocalDomain() );
        }
    }

    @Override
    protected Server newServer( JSONObject prototype )  {
        return new BasicServer( this, prototype );
    }

    DirectObjectInjector getServerInjector() {
        return this.mServerInjector;
    }
}
