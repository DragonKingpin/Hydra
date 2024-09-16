package com.pinecone.hydra.unit.udtt.operator;

import com.pinecone.framework.system.prototype.Pinenut;

public interface OperatorFactory extends Pinenut {
    void register( String typeName, TreeNodeOperator functionalNodeOperation );
    TreeNodeOperator getOperator(String typeName);
}
