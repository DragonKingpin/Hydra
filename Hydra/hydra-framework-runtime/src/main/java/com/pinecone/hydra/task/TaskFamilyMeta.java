package com.pinecone.hydra.task;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.Identification;

public interface TaskFamilyMeta extends Pinenut  {

    //long getEnumId();

    //GUID getGuid();

    Identification getId() ;

    String getName();

    String getScenario();

    String getMarshallingArchitecture();

    String getExtraInformation();

    String getLevel();

    String getDescription();

}
