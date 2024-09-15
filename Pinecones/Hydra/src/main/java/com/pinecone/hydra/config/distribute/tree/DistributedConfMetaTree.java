package com.pinecone.hydra.config.distribute.tree;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.config.distribute.entity.Properties;
import com.pinecone.hydra.unit.udsn.entity.TreeNode;

public interface DistributedConfMetaTree extends Pinenut {

    String getPath(GUID guid);

    GUID insert(TreeNode treeNode);

    TreeNode get(GUID guid);

    void insertProperties(Properties properties,GUID confNodeGuid);

    TreeNode parsePath(String path);

    void remove(GUID guid);

    void insertTextValue(GUID guid,String text,String type);

    TreeNode getWithoutInheritance(GUID guid);
}
