package com.pinecone.hydra.deploy.kom.entity;

import java.time.LocalDateTime;
import java.util.Set;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.deploy.kom.DeployFamilyNode;
import com.pinecone.hydra.system.ko.meta.ElementObject;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;

public interface ElementNode extends DeployTreeNode, DeployFamilyNode, ElementObject {

    Set<String > UnbeanifiedKeys = Set.of( "distributedTreeNode" );

    @Override
    default String objectCategoryName() {
        return "Deploy";
    }

    default Namespace evinceNamespace() {
        return null;
    }

    default QuickElement evinceQuickElement() {
        return null;
    }

    default ClusterElement evinceClusterElement() {
        return null;
    }

    default DeployElement evinceDeployElement() {
        return null;
    }

    default VirtualMachineElement evinceVirtualMachineElement() {
        return null;
    }

    default PhysicalHostElement evincePhysicalHostElement() {
        return null;
    }

    default ContainerElement evinceContainerElement() {
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

    String getAlias();

    void setAlias(String alias);

    String getExtraInformation();

    void setExtraInformation(String extraInformation);

    String getResourceType();

    void setResourceType(String resourceType);

    String getDeviceType();

    void setDeviceType(String deviceType);

    String getVendor();

    void setVendor(String vendor);

    String getModel();

    void setModel(String model);

    String getSerialNumber();

    void setSerialNumber(String serialNumber);

    String getIpAddress();

    void setIpAddress(String ipAddress);

    String getStatus();

    void setStatus(String status);

    String getDescription();

    void setDescription(String description);

    LocalDateTime getCreateTime();

    void setCreateTime(LocalDateTime createTime);

    LocalDateTime getUpdateTime();

    void setUpdateTime(LocalDateTime updateTime);

}
