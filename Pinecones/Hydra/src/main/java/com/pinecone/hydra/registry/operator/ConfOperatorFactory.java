package com.pinecone.hydra.registry.operator;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.unit.udtt.operator.TreeNodeOperator;

public interface ConfOperatorFactory extends Pinenut {
    String defaultNamespaceNode = "NamespaceNode";
    String defaultConfNode      = "ConfNode";

    void register( String typeName, TreeNodeOperator functionalNodeOperation );
    TreeNodeOperator getOperator(String typeName);
}
