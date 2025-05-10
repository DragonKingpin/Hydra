package com.pinecone.hydra.deploy;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.Identification;

public interface DeployFamilyMeta extends Pinenut  {

    //long getEnumId();

    //GUID getGuid();

    Identification getId() ;

    String getName();

    String getExtraInformation();

    String getDescription();

}
