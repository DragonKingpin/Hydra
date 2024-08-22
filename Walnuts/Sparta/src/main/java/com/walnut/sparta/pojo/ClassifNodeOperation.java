package com.walnut.sparta.pojo;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.NodeInformation;
import com.pinecone.hydra.service.NodeOperation;
import com.pinecone.hydra.service.ServiceTreeMapper;
import com.pinecone.hydra.unit.udsn.ClassifNodeManipinate;
import com.pinecone.hydra.unit.udsn.ClassifRulesManipinate;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.unit.udsn.NodeMetadataManipinate;

public class ClassifNodeOperation implements NodeOperation {
    private ClassifNodeManipinate classifNodeManipinate;
    private ClassifRulesManipinate classifRulesManipinate;
    private NodeMetadataManipinate nodeMetadataManipinate;
    private ServiceTreeMapper serviceTreeMapper;
    public ClassifNodeOperation(ClassifNodeManipinate classifNodeManipinate,ClassifRulesManipinate classifRulesManipinate,
                                NodeMetadataManipinate nodeMetadataManipinate,ServiceTreeMapper serviceTreeMapper){
        this.classifNodeManipinate=classifNodeManipinate;
        this.nodeMetadataManipinate=nodeMetadataManipinate;
        this.classifRulesManipinate=classifRulesManipinate;
        this.serviceTreeMapper=serviceTreeMapper;
    }
    @Override
    public GUID addOperation(NodeInformation nodeInformation) {
        return null;
    }

    @Override
    public void deleteOperation(GUID guid) {
        Debug.trace("调用分类操作类");
//        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(guid);
//        this.classifNodeManipinate.deleteClassifNode(node.getUUID());
//        this.classifRulesManipinate.deleteClassifRules(node.getUUID());
//        this.nodeMetadataManipinate.deleteNodeMetadata(node.getNodeMetadataUUID());
    }

    @Override
    public NodeInformation SelectOperation(GUID guid) {
        return null;
    }

    @Override
    public void UpdateOperation(NodeInformation nodeInformation) {

    }
}
