package com.pinecone.hydra;

import com.pinecone.framework.system.Framework;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.system.architecture.SystemComponentManager;
import com.pinecone.hydra.system.HySkeleton;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.system.MultiComponentSystem;
import com.pinecone.hydra.system.SystemSkeleton;

import java.nio.file.Path;

public abstract class Hydra extends Framework implements Hydrarum {
    private HySkeleton                 mComponentManager ;

    protected boolean                  mDebugMode        ;
    protected Path                     mWorkingPath      ;
    protected String                   mServiceID        ;


    public Hydra(){
        this( new String[0], null, null );
    }

    public Hydra( String[] args ){
        this( args, null, null );
    }

    public Hydra( String[] args, String szName ){
        this( args, szName, null );
    }

    public Hydra( String[] args, CascadeSystem parent ){
        this( args, null, parent );
    }

    public Hydra( String[] args, String szName, CascadeSystem parent, HySkeleton manager ){
        super( args, szName, parent );

        if( manager == null ) {
            manager = new SystemSkeleton( this );
        }
        this.mComponentManager = manager;
    }

    public Hydra( String[] args, String szName, CascadeSystem parent ){
        this( args, szName, parent, null );
    }


    protected void prepare_system_skeleton() {

    }


    @Override
    public HySkeleton getComponentManager() {
        return this.mComponentManager;
    }

    @Override
    public MultiComponentSystem apply( SystemComponentManager manager ) {
        this.mComponentManager = (HySkeleton)manager;
        return this;
    }

    @Override
    public boolean isDebugMode() {
        return this.mDebugMode;
    }

    @Override
    public Path getWorkingPath() {
        return this.mWorkingPath;
    }

    @Override
    public String getServiceID() {
        return this.mServiceID;
    }
}
