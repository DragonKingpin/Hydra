package com.pinecone.hydra.conduct.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.Identification;

/**
 * Uniform Service Instance Identifier
 */
public interface USII extends Pinenut {
    Long getClientId();

    Identification getServiceId();

    String getFullKey();
}
