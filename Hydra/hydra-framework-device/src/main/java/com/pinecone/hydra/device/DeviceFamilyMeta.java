package com.pinecone.hydra.device;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.Identification;

public interface DeviceFamilyMeta extends Pinenut  {

    //long getEnumId();

    //GUID getGuid();

    Identification getId() ;

    String getName();

    String getAlias();

    String getExtraInformation();

    String getResourceType();

    String getDeviceType();

    String getVendor();

    String getModel();

    String getSerialNumber();

    String getIpAddress();

    String getStatus();

    String getDescription();

}
