package com.pinecone.hydra.service.tree.operator;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.tree.ScopeServiceTree;
import com.pinecone.hydra.service.tree.source.ServiceMasterManipulator;

import java.util.HashMap;
import java.util.Map;

public class MetaNodeOperatorProxy implements Pinenut {
    protected ServiceMasterManipulator serviceMasterManipulator;
    protected Map<String, MetaNodeOperator>  registerer = new HashMap<>();

    public MetaNodeOperatorProxy( ServiceMasterManipulator manipulators ){
        this.serviceMasterManipulator = manipulators;

        this.register( ScopeServiceTree.DefaultMetaNodeApplication, new ApplicationNodeOperator( this.serviceMasterManipulator) );
        this.register( ScopeServiceTree.DefaultMetaNodeClassification, new ClassificationNodeOperator( this.serviceMasterManipulator) );
        this.register( ScopeServiceTree.DefaultMetaNodeService, new ServiceNodeOperator( this.serviceMasterManipulator) );
    }

    public void register( String typeName, MetaNodeOperator functionalNodeOperation ) {
        this.registerer.put( typeName, functionalNodeOperation );
    }

    public MetaNodeOperator getOperator(String typeName) {
        return this.registerer.get( typeName );
    }
}
