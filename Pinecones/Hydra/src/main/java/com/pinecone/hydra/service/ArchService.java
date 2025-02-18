package com.pinecone.hydra.service;

import java.util.Map;

import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.service.kom.entity.ServiceElement;

public abstract class ArchService extends ArchServiceFamilyMeta implements Service {

    protected Identification            mServiceId;

    protected ServiceElement            mServiceMetaData;

    protected Map<String, Object >      mMetaDataScope;


}
