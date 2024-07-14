package com.pinecone.framework.system.architecture;

import com.pinecone.framework.unit.KeyValue;
import com.pinecone.framework.util.json.JSONEncoder;

public abstract class ArchComponent implements Component {
    private ComponentManager  mComponentManager;

    protected ArchComponent( ComponentManager manager ) {
        this.mComponentManager = manager;
    }

    @Override
    public ComponentManager getComponentManager() {
        return this.mComponentManager;
    }

    @Override
    public void setComponentManager( ComponentManager componentManager ) {
        this.mComponentManager = componentManager;
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public String toJSONString() {
        return JSONEncoder.stringifyMapFormat( new KeyValue[]{
                new KeyValue<>( "class", this.className() ),
                new KeyValue<>( "name", this.getSimpleName() ),
                new KeyValue<>( "fullName", this.getFullName() )
        } );
    }
}
