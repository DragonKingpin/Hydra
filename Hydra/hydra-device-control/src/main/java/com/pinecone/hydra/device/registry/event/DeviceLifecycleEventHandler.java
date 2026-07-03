package com.pinecone.hydra.device.registry.event;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface DeviceLifecycleEventHandler extends Pinenut {

    void fired( long clientId, GUID deviceGuid, GUID instanceGuid, DeviceLifecycleEvent event, Object caused );

}
