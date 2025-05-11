package com.pinecone.hydra.deploy;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface DeployExtraMeta extends Pinenut {

    DeployFamilyMeta getKernelMeta();

    GUID getGuid() ;

    String getDeployName();

}
