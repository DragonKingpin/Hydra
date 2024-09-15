package com.pinecone.hydra.config.distribute.operator;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.config.distribute.source.ConfManipulatorSharer;
import com.pinecone.hydra.unit.udsn.operator.TreeNodeOperator;
import com.pinecone.hydra.unit.udsn.source.TreeManipulatorSharer;

import java.util.HashMap;
import java.util.Map;

public class GenericConfOperatorFactory implements ConfOperatorFactory {

    protected ConfManipulatorSharer       confManipulatorSharer;

    protected Map<String, TreeNodeOperator> registerer = new HashMap<>();

    public GenericConfOperatorFactory(ConfManipulatorSharer confManipulatorSharer, TreeManipulatorSharer treeManipulatorSharer){
        this.confManipulatorSharer  =   confManipulatorSharer;
        registerer.put(defaultConfNode,new ConfNodeOperator(this.confManipulatorSharer,treeManipulatorSharer));
        registerer.put(defaultNamespaceNode,new NamespaceNodeOperator(this.confManipulatorSharer,treeManipulatorSharer));
    }

    public void register( String typeName, TreeNodeOperator functionalNodeOperation ) {
        this.registerer.put( typeName, functionalNodeOperation );
    }

    public TreeNodeOperator getOperator(String typeName) {
        Debug.trace(this.registerer.toString());
        return this.registerer.get( typeName );
    }
}
