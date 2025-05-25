package com.pinecone.tritium.system;

import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.system.ArchSystemAutoAssembleComponent;
import com.pinecone.hydra.system.HyComponent;
import com.pinecone.hydra.system.Hydrogen;
import com.pinecone.hydra.ware.WareManager;
import com.pinecone.tritium.ally.messengers.MessagersManager;
import com.pinecone.tritium.ally.rdb.RDBManager;

public class KnittedMiddlewareDirector extends ArchSystemAutoAssembleComponent implements InterWareDirector {
    protected JSONConfig       mjoMiddlewareConf         ;

    protected RDBManager       mRDBManager;

    protected MessagersManager mMessagersManager;

    public KnittedMiddlewareDirector(Namespace name, Hydrogen system, HyComponent parent ) {
        super( name, system, system.getComponentManager(), parent );

        this.mjoMiddlewareConf = (JSONConfig) system.getSystemConfig().getChild( "Middleware" );
        //this.getSystem().getPrimaryConfigScope().autoInject( MiddlewareManager.class, this.mjoMiddlewareConf, this );

        this.mRDBManager       = new RDBManager( this );
        this.mMessagersManager = new MessagersManager( this );
        this.addChildComponent( this.mRDBManager       );
        this.addChildComponent( this.mMessagersManager );

        this.infoLifecycleInitializationDone();
    }

    public KnittedMiddlewareDirector(Hydrogen system, HyComponent parent ) {
        this( null, system, parent );
    }

    public KnittedMiddlewareDirector( Hydrogen system ) {
        this( system, null );
    }

    @Override
    public TritiumSystem getSystem() {
        return (TritiumSystem) super.getSystem();
    }

    @Override
    public JSONConfig getMiddlewareConfig() {
        return this.mjoMiddlewareConf;
    }

    @Override
    public WareManager getManager( String name ) {
        return null;
    }

    @Override
    public RDBManager getRDBManager() {
        return this.mRDBManager;
    }

    @Override
    public MessagersManager getMessagersManager() {
        return this.mMessagersManager;
    }
}

