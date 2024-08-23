package com.walnut.sparta.pojo;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.FunctionalNodeInformation;
import com.pinecone.hydra.service.FunctionalNodeOperation;
import com.pinecone.hydra.service.GenericClassificationNode;
import com.pinecone.hydra.service.GenericClassificationRules;
import com.pinecone.hydra.service.GenericNodeMetadata;
import com.pinecone.hydra.service.ServiceTreeMapper;
import com.pinecone.hydra.unit.udsn.ClassifNodeManipinate;
import com.pinecone.hydra.unit.udsn.ClassifRulesManipinate;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.unit.udsn.NodeMetadataManipinate;
import com.pinecone.ulf.util.id.UUIDBuilder;
import com.pinecone.ulf.util.id.UidGenerator;

import java.util.List;

public class ClassifFunctionalNodeOperation implements FunctionalNodeOperation {
    private ClassifNodeManipinate classifNodeManipinate;
    private ClassifRulesManipinate classifRulesManipinate;
    private NodeMetadataManipinate nodeMetadataManipinate;
    private ServiceTreeMapper serviceTreeMapper;
    public ClassifFunctionalNodeOperation(ClassifNodeManipinate classifNodeManipinate, ClassifRulesManipinate classifRulesManipinate,
                                          NodeMetadataManipinate nodeMetadataManipinate, ServiceTreeMapper serviceTreeMapper){
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
