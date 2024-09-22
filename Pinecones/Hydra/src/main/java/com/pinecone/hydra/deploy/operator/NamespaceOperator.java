package com.pinecone.hydra.deploy.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;
import com.pinecone.hydra.unit.udtt.operator.TreeNodeOperator;

public class NamespaceOperator implements TreeNodeOperator {
    @Override
    public GUID insert(TreeNode treeNode) {
        return null;
    }

    @Override
    public void remove(GUID guid) {

    }

    @Override
    public TreeNode get(GUID guid) {
        return null;
    }

    @Override
    public TreeNode getWithoutInheritance(GUID guid) {
        return null;
    }

    @Override
    public void update(TreeNode treeNode) {

    }
}
