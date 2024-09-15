package com.pinecone.hydra.deploy.operator;

import com.pinecone.hydra.unit.udsn.operator.OperatorFactory;
import com.pinecone.hydra.unit.udsn.operator.TreeNodeOperator;

public class deploymentOperatorFactory implements OperatorFactory {
    @Override
    public void register(String typeName, TreeNodeOperator functionalNodeOperation) {

    }

    @Override
    public TreeNodeOperator getOperator(String typeName) {
        return null;
    }
}
