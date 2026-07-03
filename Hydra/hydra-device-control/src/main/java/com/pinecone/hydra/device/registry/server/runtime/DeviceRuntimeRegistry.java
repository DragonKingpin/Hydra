package com.pinecone.hydra.device.registry.server.runtime;

import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.instance.DeviceInstanceEntry;

public interface DeviceRuntimeRegistry extends Pinenut {

    DeviceInstanceEntry bind( DeviceInstanceEntry instance );

    DeviceInstanceEntry unbindByDeviceGuid( GUID deviceGuid );

    DeviceInstanceEntry unbindByInstanceGuid( GUID instanceGuid );

    DeviceInstanceEntry queryByDeviceGuid( GUID deviceGuid );

    DeviceInstanceEntry queryByInstanceGuid( GUID instanceGuid );

    DeviceInstanceEntry queryByClientId( long clientId );

    Collection<DeviceInstanceEntry> fetchInstances();

    boolean containsDevice( GUID deviceGuid );

    boolean containsInstance( GUID instanceGuid );
}
