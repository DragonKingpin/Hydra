package com.pinecone.hydra.reign;

import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.system.ArchSystemCascadeComponent;
import com.pinecone.hydra.system.HyComponent;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.system.imperium.ImperiumPrivy;
import com.pinecone.hydra.system.ko.kom.ExpressInstrument;
import com.pinecone.hydra.system.ko.runtime.GenericRuntimeInstrumentConfig;
import com.pinecone.hydra.system.ko.runtime.KernelExpressInstrument;

public class UnixInstitutionalizedMetaImperiumPrivy extends ArchSystemCascadeComponent implements ImperiumPrivy {
    protected ExpressInstrument expressInstrument;

    public UnixInstitutionalizedMetaImperiumPrivy( Namespace name, Hydrarum system, HyComponent parent, JSONConfig config ) {
        super( name, system, system.getComponentManager(), parent );

        this.expressInstrument = new KernelExpressInstrument( "", new GenericRuntimeInstrumentConfig());
    }

    public UnixInstitutionalizedMetaImperiumPrivy( Hydrarum system, HyComponent parent, JSONConfig config ) {
        this( (Namespace) null, system, parent, config );
    }

    public UnixInstitutionalizedMetaImperiumPrivy( Hydrarum system, JSONConfig config ) {
        this( system, null,config );
    }

    public UnixInstitutionalizedMetaImperiumPrivy( String name, Hydrarum system, HyComponent parent, JSONConfig config ) {
        this( system, parent, config );

        this.setTargetingName( name );
    }

    public UnixInstitutionalizedMetaImperiumPrivy( String name, Hydrarum system, JSONConfig config ) {
        this( name, system, null, config );
    }

    @Override
    public ExpressInstrument getExpressInstrument() {
        return this.expressInstrument;
    }
}
