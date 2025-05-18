package com.pinecone.hydra.deploy.kom.source;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.entity.ClusterElement;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.deploy.kom.DeployInstrument;

public interface ClusterNodeManipulator extends GUIDNameManipulator {

    void insert( ClusterElement clusterElement );

    void remove( GUID guid );

    ClusterElement getClusterElement( GUID guid, DeployInstrument instrument );

    void update( ClusterElement clusterElement );

    List<ClusterElement> fetchJobNodeByName( String name );

}
