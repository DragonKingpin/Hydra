package com.pinecone.hydra.device.kom.entity;

import java.time.LocalDateTime;
import java.util.Set;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.device.kom.DeviceFamilyNode;
import com.pinecone.hydra.system.ko.meta.ElementObject;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;

public interface ElementNode extends DeviceTreeNode, DeviceFamilyNode, ElementObject {

    Set<String > UnbeanifiedKeys = Set.of( "distributedTreeNode" );

    @Override
    default String objectCategoryName() {
        return "Device";
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

    default DeviceElement evinceDeviceElement() {
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

    String getCode();

    void setCode( String code );

    String getExtraInformation();

    void setExtraInformation(String extraInformation);

    String getResourceType();

    void setResourceType(String resourceType);

    String getDeviceType();

    void setDeviceType(String deviceType);

    String getCategory();

    void setCategory( String category );

    String getClassCode();

    void setClassCode( String classCode );

    String getDeploymentProfile();

    void setDeploymentProfile( String deploymentProfile );

    String getTopologyRole();

    void setTopologyRole( String topologyRole );

    String getVendor();

    void setVendor(String vendor);

    String getModel();

    void setModel(String model);

    String getSerialNumber();

    void setSerialNumber(String serialNumber);

    String getIpAddress();

    void setIpAddress(String ipAddress);

    String getRegion();

    void setRegion( String region );

    String getZone();

    void setZone( String zone );

    String getLocation();

    void setLocation( String location );

    String getManagementProtocol();

    void setManagementProtocol( String managementProtocol );

    String getManagementHost();

    void setManagementHost( String managementHost );

    Integer getManagementPort();

    void setManagementPort( Integer managementPort );

    String getCredentialRef();

    void setCredentialRef( String credentialRef );

    String getStatus();

    void setStatus(String status);

    String getLifecycleStatus();

    void setLifecycleStatus( String lifecycleStatus );

    boolean isEnabled();

    void setEnabled( boolean enabled );

    String getTags();

    void setTags( String tags );

    String getResourceSummary();

    void setResourceSummary( String resourceSummary );

    String getDescription();

    void setDescription(String description);

    LocalDateTime getCreateTime();

    void setCreateTime(LocalDateTime createTime);

    LocalDateTime getUpdateTime();

    void setUpdateTime(LocalDateTime updateTime);

}
