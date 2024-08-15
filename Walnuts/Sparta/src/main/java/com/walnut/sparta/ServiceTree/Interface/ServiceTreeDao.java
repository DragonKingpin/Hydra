package com.walnut.sparta.ServiceTree.Interface;

import com.pinecone.hydra.service.GenericApplicationDescription;
import com.pinecone.hydra.service.GenericApplicationNode;
import com.pinecone.hydra.service.GenericClassificationNode;
import com.pinecone.hydra.service.GenericClassificationRules;
import com.pinecone.hydra.unit.udsn.UUIDDistributedScopeNode;
import com.pinecone.hydra.service.GenericNodeMetadata;
import com.pinecone.hydra.service.GenericServiceDescription;
import com.pinecone.hydra.service.GenericServiceNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ServiceTreeDao {
    //Node节点的CRUD
    void saveNode(UUIDDistributedScopeNode node);
    UUIDDistributedScopeNode selectNode(String UUID);
    void deleteNode(String UUID);
    void updateNode(UUIDDistributedScopeNode node);

    //NodeMetadata节点的CRUD
    void saveNodeMetadata(GenericNodeMetadata nodeMetadata);
    void deleteNodeMetadata(String UUID);
    GenericNodeMetadata selectNodeMetadata(String UUID);
    void updateNodeMetadata(GenericNodeMetadata nodeMetadata);

    //ServiceDescription的CRUD
    void saveServiceDescription(GenericServiceDescription UUID);
    void deleteServiceDescription(String UUID);
    void updateServiceDescription(GenericServiceDescription serviceDescription);
    GenericServiceDescription selectServiceDescription(String UUID);

    //ServiceNode的CRUD
    void saveServiceNode(GenericServiceNode serviceNode);
    void deleteServiceNode(String UUID);
    GenericServiceNode selectServiceNode(String UUID);
    void updateServiceNode(GenericServiceNode serviceNode);

    //ClassifcationRules的CRUD
    void saveClassifRules(GenericClassificationRules classificationRules);
    void deleteClassifRules(String UUID);
    GenericClassificationRules selectClassifRules(String UUID);
    void updateClassifRules(GenericClassificationRules classificationRules);

    //ClassifcationNode的CRUD
    void saveClassifNode(GenericClassificationNode classificationNode);
    void deleteClassifNode(String UUID);
    GenericClassificationNode selectClassifNode(String UUID);
    void updateClassifNode(GenericClassificationNode classificationNode);

    //ApplicationNode的CRUD
    void saveApplicationNode(GenericApplicationNode applicationNode);
    void deleteApplicationNode(String UUID);
    GenericApplicationNode selectApplicationNode(String UUID);
    void updateApplicationNode(GenericApplicationNode applicationNode);

    //ApplicationDescription的CRUD
    void saveApplicationDescription(GenericApplicationDescription applicationDescription);
    void deleteApplicationDescription(String UUID);
    GenericApplicationDescription selectApplicationDescription(String UUID);
    void updateApplicationDescription(GenericApplicationDescription applicationDescription);

    //物化路径的额外方法
    String selectPath(String UUID);
    void savePath(@Param("path") String path, @Param("UUID") String UUID);
    List<UUIDDistributedScopeNode> selectChildNode(@Param("UUID") String UUID);
    void updatePath(@Param("UUID") String UUID, @Param("Path") String path);
}
