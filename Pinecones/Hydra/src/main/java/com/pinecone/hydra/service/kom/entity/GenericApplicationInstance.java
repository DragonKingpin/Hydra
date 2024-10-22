package com.pinecone.hydra.service.kom.entity;

import com.pinecone.framework.system.prototype.ObjectiveBean;
import com.pinecone.framework.unit.affinity.DataSharer;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.ServiceFamilyNode;
import com.pinecone.hydra.service.kom.meta.GenericApplicationNodeMeta;
import com.pinecone.hydra.service.kom.source.ApplicationMetaManipulator;
import com.pinecone.hydra.service.kom.source.ApplicationNodeManipulator;
import com.pinecone.hydra.service.kom.source.ServiceMasterManipulator;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;


public class GenericApplicationInstance extends ArchMetaNodeInstance {
    private ApplicationMetaManipulator          applicationMetaManipulator;
    private ApplicationNodeManipulator          applicationNodeManipulator;

    public GenericApplicationInstance(ServiceMasterManipulator serviceMasterManipulator, TreeMasterManipulator treeManipulatorSharer){
        super(serviceMasterManipulator,treeManipulatorSharer);
        this.applicationMetaManipulator     =  this.serviceMasterManipulator.getApplicationMetaManipulator();
        this.applicationNodeManipulator     =  this.serviceMasterManipulator.getApplicationNodeManipulator();
    }

    @Override
    protected void removeDependence( GUID guid ) {
        GUIDDistributedTrieNode target = this.removeDependence0( guid );
        this.applicationMetaManipulator.remove( target.getAttributesGUID() );
        this.applicationNodeManipulator.remove(target.getGuid());
    }

    @Override
    protected MetaNodeWideEntity getWideData(GUID guid){
        GenericApplicationWideEntityMeta genericApplicationWideEntity = new GenericApplicationWideEntityMeta();
        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode(guid);
        ServiceFamilyNode nodeMetadata = this.commonDataManipulator.getNodeCommonData(node.getNodeMetadataGUID());
        GenericApplicationNodeMeta applicationMeta = this.applicationMetaManipulator.getApplicationMeta(node.getAttributesGUID());
        GUID parentGUID = this.serviceFamilyTreeManipulator.getParentByChildGUID(guid);

        genericApplicationWideEntity.setParentGUID(parentGUID);
        genericApplicationWideEntity.setGuid(guid);
        genericApplicationWideEntity.setNodeType(node.getType());

        ObjectiveBean obApp = new ObjectiveBean( genericApplicationWideEntity );
        DataSharer.share( obApp, new ObjectiveBean( nodeMetadata ) );
        DataSharer.share( obApp, new ObjectiveBean( applicationMeta ) );
        return genericApplicationWideEntity;
    }
}
