package com.walnut.sparta.services.pojo;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.service.tree.NodeWideData;
import com.pinecone.hydra.service.tree.MetaNodeOperator;
import com.pinecone.hydra.service.tree.nodes.GenericClassificationNode;
import com.pinecone.hydra.service.tree.GenericClassificationRules;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.ServiceTreeMapper;
import com.pinecone.hydra.service.tree.source.ClassifNodeManipulator;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.service.tree.source.NodeMetadataManipulator;
import com.pinecone.ulf.util.id.UUIDBuilder;
import com.pinecone.ulf.util.id.UidGenerator;


public class ClassificationNodeOperator implements MetaNodeOperator {
    public static final Class<? > TargetNode = ClassificationNodeWideData.class;

    private ClassifNodeManipulator  classifNodeManipulator;
    private NodeMetadataManipulator nodeMetadataManipulator;
    private ServiceTreeMapper       serviceTreeMapper;


    public ClassificationNodeOperator(
            ClassifNodeManipulator classifNodeManipulator, NodeMetadataManipulator nodeMetadataManipulator, ServiceTreeMapper serviceTreeMapper
    ){
        this.classifNodeManipulator  = classifNodeManipulator;
        this.nodeMetadataManipulator = nodeMetadataManipulator;
        this.serviceTreeMapper      = serviceTreeMapper;
    }

    @Override
    public GUID insert( NodeWideData nodeWideData) {
        ClassificationNodeWideData classifNodeInformation = (ClassificationNodeWideData) nodeWideData;

        //将应用节点基础信息存入信息表
        UidGenerator uidGenerator = UUIDBuilder.getBuilder();
        GUID descriptionGUID = uidGenerator.getGUID72();
        GenericClassificationRules classificationRules = classifNodeInformation.getClassificationRules();
        classificationRules.setGuid(descriptionGUID);

        //将信息写入数据库
        //将节点信息存入应用节点表
        GUID classifNodeGUID = uidGenerator.getGUID72();
        GenericClassificationNode classificationNode = classifNodeInformation.getClassificationNode();
        classificationNode.setGuid(classifNodeGUID);
        classificationNode.setRulesGUID(descriptionGUID);
        this.classifNodeManipulator.insert(classificationNode);

        //将应用元信息存入元信息表
        GUID metadataGUID = uidGenerator.getGUID72();
        GenericNodeCommonData metadata = classifNodeInformation.getNodeMetadata();
        metadata.setGuid(metadataGUID);
        this.nodeMetadataManipulator.insertNodeMetadata(metadata);

        //将节点信息存入主表
        GUIDDistributedScopeNode node = new GUIDDistributedScopeNode();
        node.setBaseDataGUID(descriptionGUID);
        node.setGuid(classifNodeGUID);
        node.setNodeMetadataGUID(metadataGUID);
        node.setType( UOI.create( "java-class:///com.walnut.sparta.pojo.ClassifFunctionalNodeInformation" ) );
        this.serviceTreeMapper.saveNode(node);
        return classifNodeGUID;
    }

    @Override
    public void remove(GUID guid) {
        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(guid);
        this.classifNodeManipulator.delete(node.getGuid());
        this.nodeMetadataManipulator.deleteNodeMetadata(node.getNodeMetadataGUID());
    }

    @Override
    public NodeWideData get(GUID guid ) {
        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(guid);
        ClassificationNodeWideData classifNodeInformation = new ClassificationNodeWideData();
        classifNodeInformation.setClassificationNode(this.classifNodeManipulator.getClassifNode(node.getGuid()));
        classifNodeInformation.setNodeMetadata(this.nodeMetadataManipulator.getNodeMetadata(node.getNodeMetadataGUID()));
        return classifNodeInformation;
    }

    @Override
    public void update(NodeWideData nodeWideData) {

    }

}
