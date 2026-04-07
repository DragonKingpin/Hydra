package com.pinecone.hydra.service;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.service.entity.USII;

import java.time.LocalDateTime;

public interface ServiceInstance extends Pinenut {
    Identification getId();

    Identification getServiceId();

    USII getUSII();

    Object getProcessObject();

    Service getService();

}
