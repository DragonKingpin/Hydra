package com.pinecone.hydra.config.distribute.operator;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.config.distribute.source.ConfManipulatorSharer;
import com.pinecone.hydra.service.tree.operator.MetaNodeOperator;

import java.util.HashMap;
import java.util.Map;

public class GenericOperatorFactory implements OperatorFactory{

    protected ConfManipulatorSharer       confManipulatorSharer;

    protected Map<String, TreeNodeOperator> registerer = new HashMap<>();

    public GenericOperatorFactory(ConfManipulatorSharer confManipulatorSharer){
        this.confManipulatorSharer  =   confManipulatorSharer;
        registerer.put(defaultConfNode,new ConfNodeOperator(this.confManipulatorSharer));
        registerer.put(defaultNamespaceNode,new NamespaceNodeOperator(this.confManipulatorSharer));
    }

    public void register( String typeName, TreeNodeOperator functionalNodeOperation ) {
        this.registerer.put( typeName, functionalNodeOperation );
    }

    public TreeNodeOperator getOperator(String typeName) {
        Debug.trace(this.registerer.toString());
        return this.registerer.get( typeName );
    }
}
