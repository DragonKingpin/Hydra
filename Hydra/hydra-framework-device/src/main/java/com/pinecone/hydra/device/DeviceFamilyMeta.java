package com.pinecone.hydra.device;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.Identification;

public interface DeviceFamilyMeta extends Pinenut  {

    //long getEnumId();

    //GUID getGuid();

    Identification getId() ;

    String getName();

    String getAlias();

    String getCode();

    String getExtraInformation();

    String getResourceType();

    String getDeviceType();

    String getCategory();

    String getClassCode();

    String getDeploymentProfile();

    String getTopologyRole();

    String getVendor();

    String getModel();

    String getSerialNumber();

    String getIpAddress();

    String getRegion();

    String getZone();

    String getLocation();

    String getManagementProtocol();

    String getManagementHost();

    Integer getManagementPort();

    String getCredentialRef();

    String getStatus();

    String getLifecycleStatus();

    boolean isEnabled();

    String getTags();

    String getResourceSummary();

    String getDescription();

}
