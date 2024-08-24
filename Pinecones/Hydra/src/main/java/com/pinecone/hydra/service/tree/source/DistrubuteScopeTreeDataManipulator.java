package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericApplicationNodeMetadata;
import com.pinecone.hydra.service.tree.nodes.GenericApplicationNode;
import com.pinecone.hydra.service.tree.nodes.GenericClassificationNode;
import com.pinecone.hydra.service.tree.GenericClassificationRules;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.GenericServiceNodeMetadata;
import com.pinecone.hydra.service.tree.nodes.GenericServiceNode;

import java.util.List;

public interface DistrubuteScopeTreeDataManipulator extends Pinenut {
    //NodeMetadata节点的CRUD
    void saveNodeMetadata(GenericNodeCommonData nodeMetadata);
    void deleteNodeMetadata(GUID UUID);
    GenericNodeCommonData selectNodeMetadata(GUID UUID);
    void updateNodeMetadata(GenericNodeCommonData nodeMetadata);

    //ServiceDescription的CRUD
    void saveServiceDescription(GenericServiceNodeMetadata UUID);
    void deleteServiceDescription(GUID UUID);
    void updateServiceDescription(GenericServiceNodeMetadata serviceDescription);
    GenericServiceNodeMetadata selectServiceDescription(GUID UUID);
    //ServiceNode的CRUD
    void saveServiceNode(GenericServiceNode serviceNode);
    void deleteServiceNode(GUID UUID);
    GenericServiceNode selectServiceNode(GUID UUID);
    void updateServiceNode(GenericServiceNode serviceNode);
    List<GenericServiceNode> selectServiceNodeByName(String name);
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
    List<GenericClassificationNode> selectClassifNodeByName(String name);

    //ApplicationNode的CRUD
    void saveApplicationNode(GenericApplicationNode applicationNode);
    void deleteApplicationNode(GUID UUID);
    GenericApplicationNode selectApplicationNode(GUID UUID);
    void updateApplicationNode(GenericApplicationNode applicationNode);
    List<GenericApplicationNode> selectApplicationNodeByName(String name);
    //ApplicationDescription的CRUD
    void saveApplicationDescription(GenericApplicationNodeMetadata applicationDescription);
    void deleteApplicationDescription(GUID UUID);
    GenericApplicationNodeMetadata selectApplicationDescription(GUID UUID);
    void updateApplicationDescription(GenericApplicationNodeMetadata applicationDescription);
    void saveClassifNodeType(GUID classifNodeUUID,GUID classifTypeUUID);
    GUID selectClassifNodeType(GUID classifNodeUUID);
}
