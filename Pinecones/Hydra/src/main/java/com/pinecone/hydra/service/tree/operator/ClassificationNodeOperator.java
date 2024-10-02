package com.pinecone.hydra.service.tree.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.UOIUtils;
import com.pinecone.hydra.service.tree.nodes.ServiceTreeNode;
import com.pinecone.hydra.service.tree.nodes.GenericClassificationNode;
import com.pinecone.hydra.service.tree.GenericClassificationRules;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.source.ClassifRulesManipulator;
import com.pinecone.hydra.service.tree.source.ServiceMasterManipulator;
import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.udtt.source.TrieTreeManipulator;
import com.pinecone.hydra.service.tree.source.ClassifNodeManipulator;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.hydra.service.tree.source.CommonDataManipulator;
import com.pinecone.ulf.util.id.UUIDBuilder;
import com.pinecone.ulf.util.id.UidGenerator;


public class ClassificationNodeOperator implements MetaNodeOperator {
    protected ClassifNodeManipulator   classifNodeManipulator;
    protected CommonDataManipulator    commonDataManipulator;
    protected TrieTreeManipulator      trieTreeManipulator;
    protected TireOwnerManipulator     tireOwnerManipulator;
    protected ClassifRulesManipulator  classifRulesManipulator;

    public ClassificationNodeOperator( ServiceMasterManipulator manipulators ) {
        this(
                manipulators.getClassifNodeManipulator(),
                manipulators.getCommonDataManipulator(),
                manipulators.getTrieTreeManipulator(),
                manipulators.getClassifRulesManipulator(),
                manipulators.getTireOwnerManipulator()
        );
    }

    public ClassificationNodeOperator(
            ClassifNodeManipulator classifNodeManipulator, CommonDataManipulator commonDataManipulator,
            TrieTreeManipulator trieTreeManipulator, ClassifRulesManipulator classifRulesManipulator,
            TireOwnerManipulator tireOwnerManipulator
    ){
        this.classifNodeManipulator  = classifNodeManipulator;
        this.commonDataManipulator   = commonDataManipulator;
        this.classifRulesManipulator = classifRulesManipulator;
        this.trieTreeManipulator     = trieTreeManipulator;
        this.tireOwnerManipulator    = tireOwnerManipulator;
    }



    @Override
    public GUID insert( ServiceTreeNode nodeWideData) {
        GenericClassificationNode classifNodeInformation = (GenericClassificationNode) nodeWideData;

        //将应用节点基础信息存入信息表
        UidGenerator uidGenerator = UUIDBuilder.getBuilder();
        GUID descriptionGUID = uidGenerator.getGUID72();
        GenericClassificationRules classificationRules = classifNodeInformation.getClassificationRules();
        classificationRules.setGuid(descriptionGUID);

        //将信息写入数据库
        //将节点信息存入应用节点表
        GUID classifNodeGUID = uidGenerator.getGUID72();
        classifNodeInformation.setGuid(classifNodeGUID);
        classifNodeInformation.setRulesGUID(descriptionGUID);
        this.classifNodeManipulator.insert(classifNodeInformation);

        //将应用元信息存入元信息表
        GUID metadataGUID = uidGenerator.getGUID72();
        GenericNodeCommonData metadata = classifNodeInformation.getNodeCommonData();
        metadata.setGuid(metadataGUID);
        this.commonDataManipulator.insert(metadata);

        //将节点信息存入主表
        GUIDDistributedTrieNode node = new GUIDDistributedTrieNode();
        node.setBaseDataGUID(descriptionGUID);
        node.setGuid(classifNodeGUID);
        node.setNodeMetadataGUID(metadataGUID);
        node.setType( UOIUtils.createLocalJavaClass( nodeWideData.getClass().getName() ) );
        this.trieTreeManipulator.insert( this.tireOwnerManipulator, node);
        return classifNodeGUID;
    }

    @Override
    public void remove(GUID guid) {
        GUIDDistributedTrieNode node = this.trieTreeManipulator.getNode(guid);
        this.classifNodeManipulator.remove(node.getGuid());
        this.commonDataManipulator.remove(node.getNodeMetadataGUID());
    }

    @Override
    public ServiceTreeNode get(GUID guid ) {
        GUIDDistributedTrieNode node = this.trieTreeManipulator.getNode(guid);
        GenericClassificationNode genericClassificationNode = new GenericClassificationNode();
        GenericNodeCommonData nodeCommonData = this.commonDataManipulator.getNodeMetadata(node.getNodeMetadataGUID());
        GenericClassificationRules classifRules = this.classifRulesManipulator.getClassifRules(node.getBaseDataGUID());
        GUIDDistributedTrieNode guidDistributedTrieNode = this.trieTreeManipulator.getNode(node.getGuid());

        genericClassificationNode.setNodeCommonData(nodeCommonData);
        genericClassificationNode.setClassificationRules(classifRules);
        genericClassificationNode.setDistributedTreeNode(guidDistributedTrieNode);
        genericClassificationNode.setName(this.classifNodeManipulator.getClassifNode(guid).getName());
        genericClassificationNode.setGuid(guid);
        genericClassificationNode.setRulesGUID(classifRules.getGuid());

        return genericClassificationNode;
    }

    @Override
    public void update(ServiceTreeNode nodeWideData) {

    }

}
