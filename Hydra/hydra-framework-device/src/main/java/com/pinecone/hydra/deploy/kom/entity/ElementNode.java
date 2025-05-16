package com.pinecone.hydra.deploy.kom.entity;

import java.time.LocalDateTime;
import java.util.Set;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.deploy.kom.DeployFamilyNode;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;

public interface ElementNode extends DeployTreeNode, DeployFamilyNode {

    Set<String > UnbeanifiedKeys = Set.of( "distributedTreeNode" );

    default Namespace evinceNamespace() {
        return null;
    }


    default QuickElement evinceQuickElement() {
        return null;
    }
    default ClusterElement evinceJobElement() {
        return null;
    }

    default DeployElement evinceDeployElement() {
        return null;
    }

    default VirtualMachineElement evinceVirtualMachineElement() {
        return null;
    }

    GUIDImperialTrieNode getDistributedTreeNode();

    void setDistributedTreeNode(GUIDImperialTrieNode distributedTreeNode);

    JSONObject toJSONObject();

    @Override
    default ElementNode evinceElementNode(){
        return this;
    }

    GUID getMetaGuid();

    void setMetaGuid(GUID metaGuid);

    String getKomPath();



    String getName();

    void setName(String name);

    LocalDateTime getCreateTime();

    void setCreateTime(LocalDateTime createTime);

    LocalDateTime getUpdateTime();

    void setUpdateTime(LocalDateTime updateTime);

}
