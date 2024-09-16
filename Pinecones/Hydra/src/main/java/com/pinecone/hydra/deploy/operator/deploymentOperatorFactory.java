package com.pinecone.hydra.deploy.operator;

import com.pinecone.hydra.unit.udtt.operator.OperatorFactory;
import com.pinecone.hydra.unit.udtt.operator.TreeNodeOperator;

public class deploymentOperatorFactory implements OperatorFactory {
    @Override
    public void register(String typeName, TreeNodeOperator functionalNodeOperation) {

    }

    @Override
    public TreeNodeOperator getOperator(String typeName) {
        return null;
    }
}
