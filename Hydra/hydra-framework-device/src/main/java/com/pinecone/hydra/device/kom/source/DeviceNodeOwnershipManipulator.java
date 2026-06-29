package com.pinecone.hydra.device.kom.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.entity.DeviceNodeOwnershipEntry;

import java.util.List;

public interface DeviceNodeOwnershipManipulator extends Pinenut {

    DeviceNodeOwnershipEntry queryDeviceNodeOwnership( GUID guid );

    List<GUID> fetchOwnedDeviceGuids( GUID ownerDeviceGuid );

    List<DeviceNodeOwnershipEntry> fetchOwnedDeviceNodes( GUID ownerDeviceGuid );

    void updateDeviceNodeOwnership( GUID guid, GUID ownerDeviceGuid, boolean deviceNode );
}
