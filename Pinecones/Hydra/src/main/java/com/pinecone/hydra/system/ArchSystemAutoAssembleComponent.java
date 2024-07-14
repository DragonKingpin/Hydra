package com.pinecone.hydra.system;

import com.pinecone.framework.system.architecture.CascadeComponent;
import com.pinecone.framework.unit.affinity.ObjectOverrider;
import com.pinecone.framework.unit.affinity.RecursiveUnitOverrider;
import com.pinecone.framework.util.lang.DynamicFactory;
import com.pinecone.framework.util.lang.GenericDynamicFactory;
import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.system.component.Log4jTraceable;
import org.slf4j.Logger;

public class ArchSystemAutoAssembleComponent extends ArchSystemCascadeComponent implements Log4jTraceable {
    protected Logger                             mLogger;
    protected ObjectOverrider<String, Object >   mObjectOverrider        ;
    protected DynamicFactory                     mUniformFactory         ;


    protected ArchSystemAutoAssembleComponent( Namespace name, Hydrarum system, SystemCascadeComponentManager manager, CascadeComponent parent ) {
        super( name, system, manager, parent );

        this.mLogger             = system.getTracerScope().newLogger( this.className() );
        this.mObjectOverrider    = new RecursiveUnitOverrider<>();
        this.mUniformFactory     = new GenericDynamicFactory( system.getTaskManager().getClassLoader() );
    }

    protected ArchSystemAutoAssembleComponent( Namespace name, SystemCascadeComponentManager manager, CascadeComponent parent ) {
        this( name, manager.getSystem(), manager, parent );
    }

    protected ArchSystemAutoAssembleComponent( Namespace name, SystemCascadeComponentManager manager ) {
        this( name, manager, null );
    }

    public DynamicFactory getSharedUniformFactory() {
        return this.mUniformFactory;
    }

    @Override
    public Logger getLogger() {
        return this.mLogger;
    }

    public ObjectOverrider<String, Object> getObjectOverrider() {
        return this.mObjectOverrider;
    }
}
