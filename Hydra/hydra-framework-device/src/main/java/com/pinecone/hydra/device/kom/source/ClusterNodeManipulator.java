package com.pinecone.hydra.device.kom.source;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.entity.ClusterElement;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.device.kom.DeviceInstrument;

public interface ClusterNodeManipulator extends GUIDNameManipulator {

    void insert( ClusterElement clusterElement );

    void remove( GUID guid );

    ClusterElement getClusterElement( GUID guid, DeviceInstrument instrument );

    void update( ClusterElement clusterElement );

    List<ClusterElement> fetchJobNodeByName( String name );

}
