package com.sauron.radium.system;

import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.system.ArchSystemAutoAssembleComponent;
import com.pinecone.hydra.system.HyComponent;
import com.pinecone.hydra.system.Hydrarum;
import com.sauron.radium.ally.messengers.MessagersManager;
import com.sauron.radium.ally.rdb.RDBManager;

public class MiddlewareManager extends ArchSystemAutoAssembleComponent implements Saunut, HyComponent {
    protected JSONConfig           mjoMiddlewareConf         ;

    protected RDBManager           mRDBManager;

    protected MessagersManager     mMessagersManager;

    public MiddlewareManager( Namespace name, Hydrarum system, HyComponent parent ) {
        super( name, system, system.getComponentManager(), parent );

        this.mjoMiddlewareConf = (JSONConfig) system.getSystemConfig().getChild( "Middleware" );
        //this.getSystem().getPrimaryConfigScope().autoInject( MiddlewareManager.class, this.mjoMiddlewareConf, this );

        this.mRDBManager       = new RDBManager( this );
        this.mMessagersManager = new MessagersManager( this );
        this.addChildComponent( this.mRDBManager       );
        this.addChildComponent( this.mMessagersManager );

        this.infoLifecycleInitializationDone();
    }

    public MiddlewareManager( Hydrarum system, HyComponent parent ) {
        this( null, system, parent );
    }

    public MiddlewareManager( Hydrarum system ) {
        this( system, null );
    }

    @Override
    public RadiumSystem getSystem() {
        return ( RadiumSystem ) super.getSystem();
    }

    public JSONConfig getMiddlewareConfig() {
        return this.mjoMiddlewareConf;
    }

    public RDBManager getRDBManager() {
        return this.mRDBManager;
    }

    public MessagersManager getMessagersManager() {
        return this.mMessagersManager;
    }
}
