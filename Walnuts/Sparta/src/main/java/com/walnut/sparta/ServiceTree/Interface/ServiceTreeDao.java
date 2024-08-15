package com.walnut.sparta.ServiceTree.Interface;

import com.pinecone.summer.prototype.Component;
import com.walnut.sparta.entity.ApplicationDescription;
import com.walnut.sparta.entity.ApplicationNode;
import com.walnut.sparta.entity.ClassificationNode;
import com.walnut.sparta.entity.ClassificationRules;
import com.walnut.sparta.entity.Node;
import com.walnut.sparta.entity.NodeMetadata;
import com.walnut.sparta.entity.ServiceDescription;
import com.walnut.sparta.entity.ServiceNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ServiceTreeDao {
    //Node节点的CRUD
    void saveNode(Node node);
    Node selectNode(String UUID);
    void deleteNode(String UUID);
    void updateNode(Node node);

    //NodeMetadata节点的CRUD
    void saveNodeMetadata(NodeMetadata nodeMetadata);
    void deleteNodeMetadata(String UUID);
    NodeMetadata selectNodeMetadata(String UUID);
    void updateNodeMetadata(NodeMetadata nodeMetadata);

    //ServiceDescription的CRUD
    void saveServiceDescription(ServiceDescription UUID);
    void deleteServiceDescription(String UUID);
    void updateServiceDescription(ServiceDescription serviceDescription);
    ServiceDescription selectServiceDescription(String UUID);

    //ServiceNode的CRUD
    void saveServiceNode(ServiceNode serviceNode);
    void deleteServiceNode(String UUID);
    ServiceNode selectServiceNode(String UUID);
    void updateServiceNode(ServiceNode serviceNode);

    //ClassifcationRules的CRUD
    void saveClassifRules(ClassificationRules classificationRules);
    void deleteClassifRules(String UUID);
    ClassificationRules selectClassifRules(String UUID);
    void updateClassifRules(ClassificationRules classificationRules);

    //ClassifcationNode的CRUD
    void saveClassifNode(ClassificationNode classificationNode);
    void deleteClassifNode(String UUID);
    ClassificationNode selectClassifNode(String UUID);
    void updateClassifNode(ClassificationNode classificationNode);

    //ApplicationNode的CRUD
    void saveApplicationNode(ApplicationNode applicationNode);
    void deleteApplicationNode(String UUID);
    ApplicationNode selectApplicationNode(String UUID);
    void updateApplicationNode(ApplicationNode applicationNode);

    //ApplicationDescription的CRUD
    void saveApplicationDescription(ApplicationDescription applicationDescription);
    void deleteApplicationDescription(String UUID);
    ApplicationDescription selectApplicationDescription(String UUID);
    void updateApplicationDescription(ApplicationDescription applicationDescription);

    //物化路径的额外方法
    String selectPath(String UUID);
    void savePath(@Param("path") String path, @Param("UUID") String UUID);
    List<Node> selectChildNode(@Param("UUID") String UUID);
    void updatePath(@Param("UUID") String UUID, @Param("Path") String path);
}
