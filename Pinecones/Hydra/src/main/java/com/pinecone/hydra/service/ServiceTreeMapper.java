package com.pinecone.hydra.service;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;

import java.util.List;

public interface ServiceTreeMapper extends Pinenut {
    void saveNode(GUIDDistributedScopeNode node);
    GUIDDistributedScopeNode selectNode(GUID UUID);
    void deleteNode(GUID UUID);
    void updateNode(GUIDDistributedScopeNode node);
    String selectPath(GUID UUID);
    void savePath( String path, GUID UUID);
    void updatePath( GUID UUID, String path);
    List<GUIDDistributedScopeNode> selectChildNode(GUID UUID);
    GUID parsePath(String path);
}
