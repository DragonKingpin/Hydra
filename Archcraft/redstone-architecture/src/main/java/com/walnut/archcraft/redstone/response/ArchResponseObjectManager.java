package com.walnut.archcraft.redstone.response;

import java.util.function.Supplier;

import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.system.ArchSystemCascadeComponent;
import com.pinecone.hydra.system.HyComponent;
import com.pinecone.hydra.system.Hydrogen;

public abstract class ArchResponseObjectManager extends ArchSystemCascadeComponent implements ResponseObjectManager {

    public ArchResponseObjectManager( Namespace name, Hydrogen system, HyComponent parent ) {
        super( name, system, system.getComponentManager(), parent );
    }

    public ArchResponseObjectManager( Hydrogen system, HyComponent parent ) {
        this( null, system, parent );
    }

    public ArchResponseObjectManager( Hydrogen system ) {
        this( system, null );
    }


    @Override
    public <T extends RedTraceableResponse> T newResponse(Supplier<T> cons) {
        T response = cons.get();
        response.setRequestId(this.nextTraceId());
        return response;
    }


}
