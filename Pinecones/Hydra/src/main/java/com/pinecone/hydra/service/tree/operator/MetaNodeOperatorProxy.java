package com.pinecone.hydra.service.tree.operator;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.tree.ScopeServiceTree;
import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulators;

import java.util.HashMap;
import java.util.Map;

public class MetaNodeOperatorProxy implements Pinenut {
    protected DefaultMetaNodeManipulators    defaultMetaNodeManipulators;
    protected Map<String, MetaNodeOperator>  registerer = new HashMap<>();

    public MetaNodeOperatorProxy( DefaultMetaNodeManipulators manipulators ){
        this.defaultMetaNodeManipulators = manipulators;

        this.register( ScopeServiceTree.DefaultMetaNodeApplication, new ApplicationNodeOperator( this.defaultMetaNodeManipulators ) );
        this.register( ScopeServiceTree.DefaultMetaNodeClassification, new ClassificationNodeOperator( this.defaultMetaNodeManipulators ) );
        this.register( ScopeServiceTree.DefaultMetaNodeService, new ServiceNodeOperator( this.defaultMetaNodeManipulators ) );
    }

    public void register( String typeName, MetaNodeOperator functionalNodeOperation ) {
        this.registerer.put( typeName, functionalNodeOperation );
    }

    public MetaNodeOperator getOperator(String typeName) {
        return this.registerer.get( typeName );
    }
}
