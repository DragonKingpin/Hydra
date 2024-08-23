package com.pinecone.hydra.service.tree;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;

import java.util.List;

public interface ServiceTreeMapper extends Pinenut {
    void saveNode(GUIDDistributedScopeNode node);

    GUIDDistributedScopeNode selectNode(GUID guid);

    void deleteNode(GUID guid);

    void updateNode(GUIDDistributedScopeNode node);

    String selectPath(GUID guid);

    void savePath( String path, GUID guid);

    void updatePath( GUID guid, String path);

    List<GUIDDistributedScopeNode> selectChildNode(GUID guid);

    GUID parsePath(String path);

    void addNodeToParent(GUID nodeGUID,GUID parentGUID);

    //方法放在这是否有问题

    String getClassifNodeClassif(GUID classifNodeGUID);

    List<GUIDDistributedScopeNode> getChildNode(GUID guid);

    void deletePath(GUID guid);
}
