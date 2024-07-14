package com.pinecone.hydra;

import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.hydra.system.HySkeleton;
import com.pinecone.hydra.system.types.HydraKingdom;

public abstract class Hydradom extends Hydra implements HydraKingdom {
    public Hydradom(){
        this( new String[0], null, null );
    }

    public Hydradom( String[] args ){
        this( args, null, null );
    }

    public Hydradom( String[] args, String szName ){
        this( args, szName, null );
    }

    public Hydradom( String[] args, CascadeSystem parent ){
        this( args, null, parent );
    }

    public Hydradom( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    public Hydradom( String[] args, String szName, CascadeSystem parent, HySkeleton manager ){
        super( args, szName, parent, manager );
    }
}
