package com.pinecone.hydra.unit.udsn.operator;

import com.pinecone.hydra.service.tree.operator.MetaNodeOperator;

public interface OperatorFactory {

    void register( String typeName, Operator functionalNodeOperation );

    Operator getOperator(String typeName);
}
