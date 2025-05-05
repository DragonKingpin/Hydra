package com.walnut.archcraft.ender;

import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.radium.Radium;
import com.walnut.archcraft.ender.system.Centrum;

public class EnderHydra extends Radium implements Centrum {
    public EnderHydra( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public EnderHydra( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }
}
