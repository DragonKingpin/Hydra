package com.pinecone.hydra.service.tree.entity;

import com.pinecone.framework.system.prototype.ObjectiveBean;
import com.pinecone.framework.unit.affinity.DataSharer;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.meta.GenericServiceNodeMeta;
import com.pinecone.hydra.service.tree.source.ServiceMasterManipulator;
import com.pinecone.hydra.service.tree.source.ServiceMetaManipulator;
import com.pinecone.hydra.service.tree.source.ServiceNodeManipulator;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;

public class GenericServiceInstance extends ArchMetaNodeInstance {
    private ServiceNodeManipulator         serviceNodeManipulator;
    private ServiceMetaManipulator         serviceMetaManipulator;

    public GenericServiceInstance(ServiceMasterManipulator serviceMasterManipulator, TreeMasterManipulator treeManipulatorSharer){
        super(serviceMasterManipulator,treeManipulatorSharer);
       this.serviceMetaManipulator   =  serviceMasterManipulator.getServiceMetaManipulator();
       this.serviceNodeManipulator   =  serviceMasterManipulator.getServiceNodeManipulator();
    }

    @Override
    protected void removeDependence( GUID guid ) {
        GUIDDistributedTrieNode target = this.removeDependence0( guid );
        this.serviceNodeManipulator.remove(guid);
        this.serviceMetaManipulator.remove(target.getAttributesGUID());
    }

    @Override
    protected MetaNodeWideEntity getWideData(GUID guid){
        GenericServiceWideEntityMeta genericServiceWideEntityMeta = new GenericServiceWideEntityMeta();
        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode(guid);
        GUID parentGUID = this.serviceFamilyTreeManipulator.getParentByChildGUID(guid);
        GenericNodeCommonData commonData = this.commonDataManipulator.getNodeMetadata(node.getNodeMetadataGUID());
        GenericServiceNodeMeta serviceMeta = this.serviceMetaManipulator.getServiceMeta(node.getAttributesGUID());


        genericServiceWideEntityMeta.setParentGUID(parentGUID);
        genericServiceWideEntityMeta.setGuid(guid);
        genericServiceWideEntityMeta.setNodeType(node.getType());


        ObjectiveBean obApp = new ObjectiveBean( genericServiceWideEntityMeta );
        DataSharer.share( obApp, new ObjectiveBean( commonData ) );
        DataSharer.share( obApp, new ObjectiveBean( serviceMeta ) );

        return genericServiceWideEntityMeta;
    }
}
