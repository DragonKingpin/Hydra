package com.pinecone.hydra.unit.udsn;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.GenericApplicationDescription;
import com.pinecone.hydra.service.GenericApplicationNode;
import com.pinecone.hydra.service.GenericClassificationNode;
import com.pinecone.hydra.service.GenericClassificationRules;
import com.pinecone.hydra.service.GenericNodeMetadata;
import com.pinecone.hydra.service.GenericServiceDescription;
import com.pinecone.hydra.service.GenericServiceNode;

import java.util.UUID;

public interface DistrubuteScopeTreeDataManipinate extends Pinenut {
    //NodeMetadata节点的CRUD
    void saveNodeMetadata(GenericNodeMetadata nodeMetadata);
    void deleteNodeMetadata(GUID UUID);
    GenericNodeMetadata selectNodeMetadata(GUID UUID);
    void updateNodeMetadata(GenericNodeMetadata nodeMetadata);

    //ServiceDescription的CRUD
    void saveServiceDescription(GenericServiceDescription UUID);
    void deleteServiceDescription(GUID UUID);
    void updateServiceDescription(GenericServiceDescription serviceDescription);
    GenericServiceDescription selectServiceDescription(GUID UUID);

    //ServiceNode的CRUD
    void saveServiceNode(GenericServiceNode serviceNode);
    void deleteServiceNode(GUID UUID);
    GenericServiceNode selectServiceNode(GUID UUID);
    void updateServiceNode(GenericServiceNode serviceNode);

    //ClassifcationRules的CRUD
    void saveClassifRules(GenericClassificationRules classificationRules);
    void deleteClassifRules(GUID UUID);
    GenericClassificationRules selectClassifRules(GUID UUID);
    void updateClassifRules(GenericClassificationRules classificationRules);

    //ClassifcationNode的CRUD
    void saveClassifNode(GenericClassificationNode classificationNode);
    void deleteClassifNode(GUID UUID);
    GenericClassificationNode selectClassifNode(GUID UUID);
    void updateClassifNode(GenericClassificationNode classificationNode);

    //ApplicationNode的CRUD
    void saveApplicationNode(GenericApplicationNode applicationNode);
    void deleteApplicationNode(GUID UUID);
    GenericApplicationNode selectApplicationNode(GUID UUID);
    void updateApplicationNode(GenericApplicationNode applicationNode);

    //ApplicationDescription的CRUD
    void saveApplicationDescription(GenericApplicationDescription applicationDescription);
    void deleteApplicationDescription(GUID UUID);
    GenericApplicationDescription selectApplicationDescription(GUID UUID);
    void updateApplicationDescription(GenericApplicationDescription applicationDescription);
}
