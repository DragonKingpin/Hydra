package com.pinecone.hydra.service.tree.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.service.tree.UOIUtils;
import com.pinecone.hydra.service.tree.nodes.ServiceTreeNode;
import com.pinecone.hydra.service.tree.nodes.GenericClassificationNode;
import com.pinecone.hydra.service.tree.GenericClassificationRules;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.source.ClassifRulesManipulator;
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
    private ClassifRulesManipulator classifRulesManipulator;

    public ClassificationNodeOperator( DefaultMetaNodeManipulators manipulators ) {
        this(
                manipulators.getClassifNodeManipulator(),
                manipulators.getCommonDataManipulator(),
                manipulators.getScopeTreeManipulator(),
                manipulators.getClassifRulesManipulator()
        );
    }

    public ClassificationNodeOperator(
            ClassifNodeManipulator classifNodeManipulator, CommonDataManipulator commonDataManipulator, ScopeTreeManipulator scopeTreeManipulator,ClassifRulesManipulator classifRulesManipulator
    ){
        this.classifNodeManipulator  = classifNodeManipulator;
        this.commonDataManipulator = commonDataManipulator;
        this.scopeTreeManipulator      = scopeTreeManipulator;
        this.classifRulesManipulator = classifRulesManipulator;
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
        node.setType( UOIUtils.createLocalJavaClass( nodeWideData.getClass().getName() ) );
        this.scopeTreeManipulator.saveNode(node);
        return classifNodeGUID;
    }

    @Override
    public void remove(GUID guid) {
        GUIDDistributedScopeNode node = this.scopeTreeManipulator.getNode(guid);
        this.classifNodeManipulator.remove(node.getGuid());
        this.commonDataManipulator.remove(node.getNodeMetadataGUID());
    }

    @Override
    public ServiceTreeNode get(GUID guid ) {
        GUIDDistributedScopeNode node = this.scopeTreeManipulator.getNode(guid);
        GenericClassificationNode genericClassificationNode = new GenericClassificationNode();
        GenericNodeCommonData nodeCommonData = this.commonDataManipulator.getNodeMetadata(node.getNodeMetadataGUID());
        GenericClassificationRules classifRules = this.classifRulesManipulator.getClassifRules(node.getBaseDataGUID());
        GUIDDistributedScopeNode guidDistributedScopeNode = this.scopeTreeManipulator.getNode(node.getGuid());

        genericClassificationNode.setNodeCommonData(nodeCommonData);
        genericClassificationNode.setClassificationRules(classifRules);
        genericClassificationNode.setDistributedTreeNode(guidDistributedScopeNode);
        genericClassificationNode.setName(this.classifNodeManipulator.getClassifNode(guid).getName());
        genericClassificationNode.setGuid(guid);
        genericClassificationNode.setRulesGUID(classifRules.getGuid());

        return genericClassificationNode;
    }

    @Override
    public void update(ServiceTreeNode nodeWideData) {

    }

}
