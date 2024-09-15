package com.pinecone.hydra.config.distribute.operator;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.config.distribute.source.ConfigMasterManipulator;
import com.pinecone.hydra.unit.udsn.operator.TreeNodeOperator;
import com.pinecone.hydra.unit.udsn.source.TreeMasterManipulator;

import java.util.HashMap;
import java.util.Map;

public class GenericConfOperatorFactory implements ConfOperatorFactory {

    protected ConfigMasterManipulator configMasterManipulator;

    protected Map<String, TreeNodeOperator> registerer = new HashMap<>();

    public GenericConfOperatorFactory(ConfigMasterManipulator configMasterManipulator, TreeMasterManipulator treeManipulatorSharer){
        this.configMasterManipulator  =   configMasterManipulator;
        registerer.put(defaultConfNode,new ConfNodeOperator(this.configMasterManipulator,treeManipulatorSharer));
        registerer.put(defaultNamespaceNode,new NamespaceNodeOperator(this.configMasterManipulator,treeManipulatorSharer));
    }

    public void register( String typeName, TreeNodeOperator functionalNodeOperation ) {
        this.registerer.put( typeName, functionalNodeOperation );
    }

    public TreeNodeOperator getOperator(String typeName) {
        Debug.trace(this.registerer.toString());
        return this.registerer.get( typeName );
    }
}
