package com.walnut.sparta.pojo;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.NodeInformation;
import com.pinecone.hydra.service.NodeOperation;
import com.pinecone.hydra.service.ServiceTreeMapper;
import com.pinecone.hydra.unit.udsn.ApplicationDescriptionManipinate;
import com.pinecone.hydra.unit.udsn.ApplicationNodeManipinate;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.unit.udsn.NodeMetadataManipinate;

public class ApplicationNodeOperation implements NodeOperation {
    private ApplicationNodeManipinate applicationNodeManipinate;
    private ApplicationDescriptionManipinate applicationDescriptionManipinate;
    private NodeMetadataManipinate nodeMetadataManipinate;
    private ServiceTreeMapper serviceTreeMapper;
    public ApplicationNodeOperation(ApplicationNodeManipinate applicationNodeManipinate,ApplicationDescriptionManipinate applicationDescriptionManipinate,
                                    NodeMetadataManipinate nodeMetadataManipinate,ServiceTreeMapper serviceTreeMapper){
        this.applicationNodeManipinate=applicationNodeManipinate;
        this.applicationDescriptionManipinate=applicationDescriptionManipinate;
        this.nodeMetadataManipinate=nodeMetadataManipinate;
        this.serviceTreeMapper=serviceTreeMapper;
    }
    @Override
    public GUID addOperation(NodeInformation nodeInformation) {
        return null;
    }

    @Override
    public void deleteOperation(GUID guid) {
        Debug.trace("调用应用操作类");
//        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(guid);
//        this.applicationDescriptionManipinate.deleteApplicationDescription(node.getBaseDataUUID());
//        this.nodeMetadataManipinate.deleteNodeMetadata(node.getNodeMetadataUUID());
//        this.applicationNodeManipinate.deleteApplicationNode(node.getUUID());
    }

    @Override
    public NodeInformation SelectOperation(GUID guid) {
        return null;
    }

    @Override
    public void UpdateOperation(NodeInformation nodeInformation) {

    }
}
