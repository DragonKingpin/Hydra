package com.pinecone.hydra.device.registry.server.connection;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.instance.DeviceInstanceEntry;

public interface DeviceConnectionRegistry extends Pinenut {

    void bindConnection( DeviceConnection connection );

    DeviceConnection detachConnection( String connectionId );

    void bindInstance( String connectionId, DeviceInstanceEntry instance );

    DeviceInstanceEntry unbindInstance( String connectionId );

    DeviceConnection queryConnection( String connectionId );

    DeviceInstanceEntry queryBoundInstance( String connectionId );

    String queryBoundConnectionId( GUID instanceGuid );
}
