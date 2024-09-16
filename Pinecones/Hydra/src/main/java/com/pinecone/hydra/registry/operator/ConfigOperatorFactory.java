package com.pinecone.hydra.registry.operator;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.registry.entity.NamespaceNode;
import com.pinecone.hydra.unit.udtt.operator.TreeNodeOperator;

public interface ConfigOperatorFactory extends Pinenut {
    String DefaultNamespaceNodeKey   = NamespaceNode.class.getSimpleName();
    String DefaultConfigNodeKey      = ConfigNode.class.getSimpleName();

    void register( String typeName, TreeNodeOperator functionalNodeOperation );
    TreeNodeOperator getOperator(String typeName);
}
