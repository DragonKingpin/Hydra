package com.pinecone.hydra.deploy.kom.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.entity.DeployInsMapping;

public interface DeployServiceInsMappingManipulator extends Pinenut {
    void insert( DeployInsMapping deployInsMapping );

    DeployInsMapping queryDeployInsMappingByInsGuid( GUID insGuid );

    DeployInsMapping queryDeployInsMappingByDeployGuid( GUID deployGuid );

    void removeByInsGuid( GUID insGuid );

    void removeByDeployGuid( GUID deployGuid );
}
