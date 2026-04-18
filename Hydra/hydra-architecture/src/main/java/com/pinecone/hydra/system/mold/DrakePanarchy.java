package com.pinecone.hydra.system.mold;

import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.system.Framework;

public abstract class DrakePanarchy extends Framework implements Lepton {

    public DrakePanarchy(){
        this( new String[0], null, null );
    }

    public DrakePanarchy( String[] args ){
        this( args, null, null );
    }

    public DrakePanarchy( String[] args, String szName ){
        this( args, szName, null );
    }

    public DrakePanarchy( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public DrakePanarchy( String[] args, String szName, CascadeSystem parent ) {
        super( args, szName, parent );
    }

}
