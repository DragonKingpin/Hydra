package com.pinecone.hydra.config.distribute.operator;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.config.distribute.entity.ConfNode;
import com.pinecone.hydra.config.distribute.entity.NamespaceNode;

public interface OperatorFactory extends Pinenut {
    String defaultNamespaceNode = "NamespaceNode";
    String defaultConfNode      = "ConfNode";

    void register( String typeName, TreeNodeOperator functionalNodeOperation );
    TreeNodeOperator getOperator(String typeName);
}
