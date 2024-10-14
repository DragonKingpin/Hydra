package com.pinecone.hydra.service.kom.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.UOIUtils;
import com.pinecone.hydra.service.kom.nodes.ServiceTreeNode;
import com.pinecone.hydra.service.kom.nodes.GenericNamespace;
import com.pinecone.hydra.service.kom.GenericClassificationRules;
import com.pinecone.hydra.service.kom.GenericNodeCommonData;
import com.pinecone.hydra.service.kom.source.ClassifRulesManipulator;
import com.pinecone.hydra.service.kom.source.ServiceMasterManipulator;
import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.udtt.source.TrieTreeManipulator;
import com.pinecone.hydra.service.kom.source.ClassifNodeManipulator;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.hydra.service.kom.source.CommonDataManipulator;
import com.pinecone.ulf.util.id.GUIDs;
import com.pinecone.ulf.util.id.GuidAllocator;


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
        GenericNamespace classifNodeInformation = (GenericNamespace) nodeWideData;

        //将应用节点基础信息存入信息表
        GuidAllocator guidAllocator = GUIDs.newGuidAllocator();
        GUID descriptionGUID = guidAllocator.nextGUID72();
        GenericClassificationRules classificationRules = classifNodeInformation.getClassificationRules();
        classificationRules.setGuid(descriptionGUID);

        //将信息写入数据库
        //将节点信息存入应用节点表
        GUID classifNodeGUID = guidAllocator.nextGUID72();
        classifNodeInformation.setGuid(classifNodeGUID);
        classifNodeInformation.setRulesGUID(descriptionGUID);
        this.classifNodeManipulator.insert(classifNodeInformation);

        //将应用元信息存入元信息表
        GUID metadataGUID = guidAllocator.nextGUID72();
        GenericNodeCommonData metadata = classifNodeInformation.getAttributes();
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
        GenericNamespace genericClassificationNode = new GenericNamespace();
        GenericNodeCommonData nodeAttributes = this.commonDataManipulator.getNodeMetadata(node.getNodeMetadataGUID());
        GenericClassificationRules classifRules = this.classifRulesManipulator.getClassifRules(node.getAttributesGUID());
        GUIDDistributedTrieNode guidDistributedTrieNode = this.trieTreeManipulator.getNode(node.getGuid());

        genericClassificationNode.setNodeCommonData(nodeAttributes);
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
