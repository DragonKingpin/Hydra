package com.acorn.skynet.device.husky.protocol;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umct.stereotype.Iface;

@Iface
public interface HuskyDeviceControlIface extends Pinenut {

    HuskyDeviceRegisterResult registerDevice( String connectionId, HuskyDeviceRegisterInstruction instruction );

    HuskyDeviceDeregisterResult deregisterDevice( String connectionId, HuskyDeviceDeregisterInstruction instruction );

    void detachDevice( String connectionId );
}
