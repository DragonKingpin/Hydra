package com.walnut.sparta.services.pojo;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.FunctionalNodeMeta;
import com.pinecone.hydra.service.tree.MetaNodeOperator;
import com.pinecone.hydra.service.tree.nodes.GenericClassificationNode;
import com.pinecone.hydra.service.tree.GenericClassificationRules;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.ServiceTreeMapper;
import com.pinecone.hydra.service.tree.source.ClassifNodeManipulator;
import com.pinecone.hydra.service.tree.source.ClassifRulesManipulator;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.service.tree.source.NodeMetadataManipulator;
import com.pinecone.ulf.util.id.UUIDBuilder;
import com.pinecone.ulf.util.id.UidGenerator;

public class ClassifFunctionalNodeOperator implements MetaNodeOperator {
    private ClassifNodeManipulator classifNodeManipinate;
    private ClassifRulesManipulator classifRulesManipinate;
    private NodeMetadataManipulator nodeMetadataManipinate;
    private ServiceTreeMapper serviceTreeMapper;
    public ClassifFunctionalNodeOperator(ClassifNodeManipulator classifNodeManipinate, ClassifRulesManipulator classifRulesManipinate,
                                         NodeMetadataManipulator nodeMetadataManipinate, ServiceTreeMapper serviceTreeMapper){
        this.classifNodeManipinate=classifNodeManipinate;
        this.nodeMetadataManipinate=nodeMetadataManipinate;
        this.classifRulesManipinate=classifRulesManipinate;
        this.serviceTreeMapper=serviceTreeMapper;
    }
    @Override
    public GUID insert(FunctionalNodeMeta functionalNodeMeta) {
        ClassifFunctionalNodeMeta classifNodeInformation=(ClassifFunctionalNodeMeta) functionalNodeMeta;
        //将应用节点基础信息存入信息表
        UidGenerator uidGenerator= UUIDBuilder.getBuilder();
        GUID descriptionGUID = uidGenerator.getGUID72();
        GenericClassificationRules classificationRules = classifNodeInformation.getClassificationRules();
        classificationRules.setGuid(descriptionGUID);
        this.classifRulesManipinate.insertClassifRules(classificationRules);
        //将信息写入数据库
        //将节点信息存入应用节点表
        GUID classifNodeGUID = uidGenerator.getGUID72();
        GenericClassificationNode classificationNode = classifNodeInformation.getClassificationNode();
        classificationNode.setGuid(classifNodeGUID);
        classificationNode.setRulesGUID(descriptionGUID);
        this.classifNodeManipinate.insertClassifNode(classificationNode);
        //将应用元信息存入元信息表
        GUID metadataGUID = uidGenerator.getGUID72();
        GenericNodeCommonData metadata = classifNodeInformation.getNodeMetadata();
        metadata.setGuid(metadataGUID);
        this.nodeMetadataManipinate.insertNodeMetadata(metadata);
        //将节点信息存入主表
        GUIDDistributedScopeNode node = new GUIDDistributedScopeNode();
        node.setBaseDataGUID(descriptionGUID);
        node.setGuid(classifNodeGUID);
        node.setNodeMetadataGUID(metadataGUID);
        node.setType("com.walnut.sparta.pojo.ClassifFunctionalNodeInformation");
        this.serviceTreeMapper.saveNode(node);
        return classifNodeGUID;
    }

    @Override
    public void remove(GUID guid) {
        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(guid);
        this.classifNodeManipinate.deleteClassifNode(node.getGuid());
        this.classifRulesManipinate.deleteClassifRules(node.getGuid());
        this.nodeMetadataManipinate.deleteNodeMetadata(node.getNodeMetadataGUID());
    }

    @Override
    public FunctionalNodeMeta get(GUID guid) {
        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(guid);
        ClassifFunctionalNodeMeta classifNodeInformation = new ClassifFunctionalNodeMeta();
        classifNodeInformation.setClassificationNode(this.classifNodeManipinate.getClassifNode(node.getGuid()));
        classifNodeInformation.setNodeMetadata(this.nodeMetadataManipinate.getNodeMetadata(node.getNodeMetadataGUID()));
        classifNodeInformation.setClassificationRules(this.classifRulesManipinate.selectClassifRules(node.getBaseDataGUID()));
        return classifNodeInformation;
    }

    @Override
    public void update(FunctionalNodeMeta functionalNodeMeta) {

    }

}
