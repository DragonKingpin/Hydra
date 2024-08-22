package com.walnut.sparta.pojo;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.NodeInformation;
import com.pinecone.hydra.service.NodeOperation;
import com.pinecone.hydra.service.ServiceTreeMapper;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.unit.udsn.NodeMetadataManipinate;
import com.pinecone.hydra.unit.udsn.ServiceDescriptionManipinate;
import com.pinecone.hydra.unit.udsn.ServiceNodeManipinate;

public class ServiceNodeOperation implements NodeOperation {
    private ServiceNodeManipinate serviceNodeManipinate;
    private ServiceDescriptionManipinate serviceDescriptionManipinate;
    private NodeMetadataManipinate nodeMetadataManipinate;
    private ServiceTreeMapper serviceTreeMapper;
    public ServiceNodeOperation(ServiceNodeManipinate serviceNodeManipinate,ServiceDescriptionManipinate serviceDescriptionManipinate,
                                NodeMetadataManipinate nodeMetadataManipinate,ServiceTreeMapper serviceTreeMapper){
        this.serviceNodeManipinate=serviceNodeManipinate;
        this.serviceDescriptionManipinate=serviceDescriptionManipinate;
        this.nodeMetadataManipinate=nodeMetadataManipinate;
        this.serviceTreeMapper=serviceTreeMapper;
    }
    @Override
    public GUID addOperation(NodeInformation nodeInformation) {
        return null;
    }

    @Override
    public void deleteOperation(GUID guid) {
        Debug.trace("调用服务操作类");
//        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(guid);
//        this.serviceNodeManipinate.deleteServiceNode(node.getUUID());
//        this.serviceDescriptionManipinate.deleteServiceDescription(node.getBaseDataUUID());
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
