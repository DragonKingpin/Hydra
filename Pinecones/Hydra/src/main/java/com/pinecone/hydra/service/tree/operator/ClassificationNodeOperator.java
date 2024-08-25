package com.pinecone.hydra.service.tree.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.service.tree.nodes.ServiceTreeNode;
import com.pinecone.hydra.service.tree.nodes.GenericClassificationNode;
import com.pinecone.hydra.service.tree.GenericClassificationRules;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulators;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import com.pinecone.hydra.service.tree.source.ClassifNodeManipulator;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.service.tree.source.CommonDataManipulator;
import com.pinecone.ulf.util.id.UUIDBuilder;
import com.pinecone.ulf.util.id.UidGenerator;


public class ClassificationNodeOperator implements MetaNodeOperator {
    private ClassifNodeManipulator  classifNodeManipulator;
    private CommonDataManipulator commonDataManipulator;
    private ScopeTreeManipulator scopeTreeManipulator;

    public ClassificationNodeOperator( DefaultMetaNodeManipulators manipulators ) {
        this(
                manipulators.getClassifNodeManipulator(),
                manipulators.getCommonDataManipulator(),
                manipulators.getScopeTreeManipulator()
        );
    }

    public ClassificationNodeOperator(
            ClassifNodeManipulator classifNodeManipulator, CommonDataManipulator commonDataManipulator, ScopeTreeManipulator scopeTreeManipulator
    ){
        this.classifNodeManipulator  = classifNodeManipulator;
        this.commonDataManipulator = commonDataManipulator;
        this.scopeTreeManipulator      = scopeTreeManipulator;
    }



    @Override
    public GUID insert( ServiceTreeNode nodeWideData) {
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
        this.commonDataManipulator.insert(metadata);

        //将节点信息存入主表
        GUIDDistributedScopeNode node = new GUIDDistributedScopeNode();
        node.setBaseDataGUID(descriptionGUID);
        node.setGuid(classifNodeGUID);
        node.setNodeMetadataGUID(metadataGUID);
        node.setType( UOI.create( "java-class:///com.walnut.sparta.pojo.ClassifFunctionalNodeInformation" ) );
        this.scopeTreeManipulator.saveNode(node);
        return classifNodeGUID;
    }

    @Override
    public void remove(GUID guid) {
        GUIDDistributedScopeNode node = this.scopeTreeManipulator.selectNode(guid);
        this.classifNodeManipulator.delete(node.getGuid());
        this.commonDataManipulator.delete(node.getNodeMetadataGUID());
    }

    @Override
    public ServiceTreeNode get(GUID guid ) {
        GUIDDistributedScopeNode node = this.scopeTreeManipulator.selectNode(guid);
        ClassificationNodeWideData classifNodeInformation = new ClassificationNodeWideData();
        classifNodeInformation.setClassificationNode(this.classifNodeManipulator.getClassifNode(node.getGuid()));
        classifNodeInformation.setNodeMetadata(this.commonDataManipulator.getNodeMetadata(node.getNodeMetadataGUID()));
        return classifNodeInformation;
    }

    @Override
    public void update(ServiceTreeNode nodeWideData) {

    }

}
