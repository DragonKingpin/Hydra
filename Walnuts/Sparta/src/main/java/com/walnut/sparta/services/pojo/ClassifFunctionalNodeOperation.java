package com.walnut.sparta.services.pojo;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.FunctionalNodeInformation;
import com.pinecone.hydra.service.tree.FunctionalNodeOperation;
import com.pinecone.hydra.service.tree.nodes.GenericClassificationNode;
import com.pinecone.hydra.service.tree.GenericClassificationRules;
import com.pinecone.hydra.service.tree.GenericNodeMetadata;
import com.pinecone.hydra.service.tree.ServiceTreeMapper;
import com.pinecone.hydra.service.tree.source.ClassifNodeManipulator;
import com.pinecone.hydra.service.tree.source.ClassifRulesManipulator;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.service.tree.source.NodeMetadataManipulator;
import com.pinecone.ulf.util.id.UUIDBuilder;
import com.pinecone.ulf.util.id.UidGenerator;

public class ClassifFunctionalNodeOperation implements FunctionalNodeOperation {
    private ClassifNodeManipulator classifNodeManipinate;
    private ClassifRulesManipulator classifRulesManipinate;
    private NodeMetadataManipulator nodeMetadataManipinate;
    private ServiceTreeMapper serviceTreeMapper;
    public ClassifFunctionalNodeOperation(ClassifNodeManipulator classifNodeManipinate, ClassifRulesManipulator classifRulesManipinate,
                                          NodeMetadataManipulator nodeMetadataManipinate, ServiceTreeMapper serviceTreeMapper){
        this.classifNodeManipinate=classifNodeManipinate;
        this.nodeMetadataManipinate=nodeMetadataManipinate;
        this.classifRulesManipinate=classifRulesManipinate;
        this.serviceTreeMapper=serviceTreeMapper;
    }
    @Override
    public GUID addOperation(FunctionalNodeInformation functionalNodeInformation) {
        ClassifFunctionalNodeInformation classifNodeInformation=(ClassifFunctionalNodeInformation) functionalNodeInformation;
        //将应用节点基础信息存入信息表
        UidGenerator uidGenerator= UUIDBuilder.getBuilder();
        GUID descriptionGUID = uidGenerator.getGUID72();
        GenericClassificationRules classificationRules = classifNodeInformation.getClassificationRules();
        classificationRules.setUUID(descriptionGUID);
        this.classifRulesManipinate.saveClassifRules(classificationRules);
        //将信息写入数据库
        //将节点信息存入应用节点表
        GUID classifNodeGUID = uidGenerator.getGUID72();
        GenericClassificationNode classificationNode = classifNodeInformation.getClassificationNode();
        classificationNode.setUUID(classifNodeGUID);
        classificationNode.setRulesUUID(descriptionGUID);
        this.classifNodeManipinate.saveClassifNode(classificationNode);
        //将应用元信息存入元信息表
        GUID metadataGUID = uidGenerator.getGUID72();
        GenericNodeMetadata metadata = classifNodeInformation.getNodeMetadata();
        metadata.setUUID(metadataGUID);
        this.nodeMetadataManipinate.saveNodeMetadata(metadata);
        //将节点信息存入主表
        GUIDDistributedScopeNode node = new GUIDDistributedScopeNode();
        node.setBaseDataUUID(descriptionGUID);
        node.setUUID(classifNodeGUID);
        node.setNodeMetadataUUID(metadataGUID);
        node.setType("com.walnut.sparta.pojo.ClassifFunctionalNodeInformation");
        this.serviceTreeMapper.saveNode(node);
        return classifNodeGUID;
    }

    @Override
    public void deleteOperation(GUID guid) {
        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(guid);
        this.classifNodeManipinate.deleteClassifNode(node.getUUID());
        this.classifRulesManipinate.deleteClassifRules(node.getUUID());
        this.nodeMetadataManipinate.deleteNodeMetadata(node.getNodeMetadataUUID());
    }

    @Override
    public FunctionalNodeInformation SelectOperation(GUID guid) {
        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(guid);
        ClassifFunctionalNodeInformation classifNodeInformation = new ClassifFunctionalNodeInformation();
        classifNodeInformation.setClassificationNode(this.classifNodeManipinate.selectClassifNode(node.getUUID()));
        classifNodeInformation.setNodeMetadata(this.nodeMetadataManipinate.selectNodeMetadata(node.getNodeMetadataUUID()));
        classifNodeInformation.setClassificationRules(this.classifRulesManipinate.selectClassifRules(node.getBaseDataUUID()));
        return classifNodeInformation;
    }

    @Override
    public void UpdateOperation(FunctionalNodeInformation functionalNodeInformation) {

    }

}
