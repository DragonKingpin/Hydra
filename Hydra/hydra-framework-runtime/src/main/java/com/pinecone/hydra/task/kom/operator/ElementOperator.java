package com.pinecone.hydra.task.kom.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.entity.ElementNode;
import com.pinecone.hydra.unit.imperium.operator.TreeNodeOperator;

public interface ElementOperator extends TreeNodeOperator {
    @Override
    ElementNode get(GUID guid);

    @Override
    ElementNode get(GUID guid, int depth);

    @Override
    ElementNode getAsRootDepth(GUID guid);
}
