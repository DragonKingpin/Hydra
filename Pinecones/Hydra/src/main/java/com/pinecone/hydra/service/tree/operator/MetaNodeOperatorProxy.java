package com.pinecone.hydra.service.tree.operator;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.tree.ScopeServiceTree;
import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulator;

import java.util.HashMap;
import java.util.Map;

public class MetaNodeOperatorProxy implements Pinenut {
    protected DefaultMetaNodeManipulator defaultMetaNodeManipulator;
    protected Map<String, MetaNodeOperator>  registerer = new HashMap<>();

    public MetaNodeOperatorProxy( DefaultMetaNodeManipulator manipulators ){
        this.defaultMetaNodeManipulator = manipulators;

        this.register( ScopeServiceTree.DefaultMetaNodeApplication, new ApplicationNodeOperator( this.defaultMetaNodeManipulator) );
        this.register( ScopeServiceTree.DefaultMetaNodeClassification, new ClassificationNodeOperator( this.defaultMetaNodeManipulator) );
        this.register( ScopeServiceTree.DefaultMetaNodeService, new ServiceNodeOperator( this.defaultMetaNodeManipulator) );
    }

    public void register( String typeName, MetaNodeOperator functionalNodeOperation ) {
        this.registerer.put( typeName, functionalNodeOperation );
    }

    public MetaNodeOperator getOperator(String typeName) {
        return this.registerer.get( typeName );
    }
}
