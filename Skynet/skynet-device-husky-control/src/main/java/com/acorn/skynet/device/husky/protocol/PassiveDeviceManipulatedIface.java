package com.acorn.skynet.device.husky.protocol;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umct.stereotype.Iface;

@Iface
public interface PassiveDeviceManipulatedIface extends Pinenut {

    void shutdownDevice( String instanceGuid, String reason );
}
