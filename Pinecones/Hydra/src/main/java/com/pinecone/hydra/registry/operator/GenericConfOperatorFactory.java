package com.pinecone.hydra.registry.operator;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.registry.source.RegistryMasterManipulator;
import com.pinecone.hydra.unit.udtt.operator.TreeNodeOperator;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;

import java.util.HashMap;
import java.util.Map;

public class GenericConfOperatorFactory implements ConfOperatorFactory {

    protected RegistryMasterManipulator registryMasterManipulator;

    protected Map<String, TreeNodeOperator> registerer = new HashMap<>();

    public GenericConfOperatorFactory(RegistryMasterManipulator registryMasterManipulator, TreeMasterManipulator treeManipulatorSharer){
        this.registryMasterManipulator = registryMasterManipulator;
        registerer.put(defaultConfNode,new ConfNodeOperator(this.registryMasterManipulator,treeManipulatorSharer));
        registerer.put(defaultNamespaceNode,new NamespaceNodeOperator(this.registryMasterManipulator,treeManipulatorSharer));
    }

    public void register( String typeName, TreeNodeOperator functionalNodeOperation ) {
        this.registerer.put( typeName, functionalNodeOperation );
    }

    public TreeNodeOperator getOperator(String typeName) {
        Debug.trace(this.registerer.toString());
        return this.registerer.get( typeName );
    }
}
