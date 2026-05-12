package com.pinecone.hydra.device;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface DeviceExtraMeta extends Pinenut {

    DeviceFamilyMeta getKernelMeta();

    GUID getGuid() ;

    String getDeviceName();

}
