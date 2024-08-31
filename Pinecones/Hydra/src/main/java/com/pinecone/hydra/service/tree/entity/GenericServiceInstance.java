package com.pinecone.hydra.service.tree.entity;

import com.pinecone.framework.system.prototype.ObjectiveBean;
import com.pinecone.framework.unit.affinity.DataSharer;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.meta.GenericServiceNodeMeta;
import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulator;
import com.pinecone.hydra.service.tree.source.ServiceMetaManipulator;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;

public class GenericServiceInstance extends ArchMetaNodeInstance {
    private ServiceMetaManipulator              serviceMetaManipulator;

    public GenericServiceInstance( DefaultMetaNodeManipulator defaultMetaNodeManipulator ){
        super( defaultMetaNodeManipulator );
        this.serviceMetaManipulator           =   defaultMetaNodeManipulator.getServiceMetaManipulator();
    }

    @Override
    protected void removeDependence( GUID guid ) {
        GUIDDistributedScopeNode target = this.removeDependence0( guid );
        this.serviceMetaManipulator.remove( target.getBaseDataGUID() );
    }

    @Override
    protected MetaNodeWideEntity getWideData(GUID guid){
        GenericServiceWideEntityMeta serviceWideEntityMeta = new GenericServiceWideEntityMeta();
        GUIDDistributedScopeNode node = this.scopeTreeManipulator.getNode(guid);
        GUID parentGUID = this.serviceFamilyTreeManipulator.getParentByChildGUID(guid);
        GenericNodeCommonData commonData = this.commonDataManipulator.getNodeMetadata(node.getNodeMetadataGUID());
        GenericServiceNodeMeta serviceMeta = this.serviceMetaManipulator.getServiceMeta(node.getBaseDataGUID());

        Debug.trace(commonData);

        serviceWideEntityMeta.setGuid(guid);
        serviceWideEntityMeta.setParentGUID(parentGUID);

        ObjectiveBean obApp = new ObjectiveBean( serviceWideEntityMeta );
        DataSharer.share( obApp, new ObjectiveBean( commonData ) );
        DataSharer.share( obApp, new ObjectiveBean( serviceMeta ) );
        return serviceWideEntityMeta;
    }
}
