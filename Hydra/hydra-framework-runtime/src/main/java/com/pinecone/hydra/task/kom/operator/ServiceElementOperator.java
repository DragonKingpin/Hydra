package com.pinecone.hydra.task.kom.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.task.kom.ServiceInstrument;
import com.pinecone.hydra.task.kom.entity.GenericTaskElement;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.source.ServiceMasterManipulator;
import com.pinecone.hydra.task.kom.source.ServiceMetaManipulator;
import com.pinecone.hydra.task.kom.source.TaskNodeManipulator;
import com.pinecone.hydra.system.ko.UOIUtils;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class ServiceElementOperator extends ArchElementOperator implements ElementOperator {
    protected TaskNodeManipulator taskNodeManipulator;
    protected ServiceMetaManipulator  serviceMetaManipulator;

    public ServiceElementOperator( ElementOperatorFactory factory ) {
        this( factory.getServiceMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }

    public ServiceElementOperator( ServiceMasterManipulator masterManipulator, ServiceInstrument serviceInstrument){
        super( masterManipulator, serviceInstrument);
       this.taskNodeManipulator = masterManipulator.getServiceNodeManipulator();
       this.serviceMetaManipulator = masterManipulator.getServiceMetaManipulator();

    }


    @Override
    public GUID insert( TreeNode treeNode ) {
        GenericTaskElement serviceElement = (GenericTaskElement) treeNode;

        //将信息写入数据库
        //将节点信息存入应用节点表
        GuidAllocator guidAllocator = this.serviceInstrument.getGuidAllocator();
        GUID serviceNodeGUID = guidAllocator.nextGUID();
        serviceElement.setGuid(serviceNodeGUID);
        this.taskNodeManipulator.insert( serviceElement );

        //将应用节点基础信息存入信息表
        GUID metaGUID = guidAllocator.nextGUID();
        if ( serviceElement.getMetaGuid() == null ){
            serviceElement.setMetaGuid( metaGUID );
        }
        this.serviceMetaManipulator.insert( serviceElement );


        //将应用元信息存入元信息表
       this.commonDataManipulator.insert( serviceElement );


        //将节点信息存入主表
        GUIDImperialTrieNode node = new GUIDImperialTrieNode();
        node.setNodeMetadataGUID( metaGUID );
        node.setGuid( serviceNodeGUID );
        node.setType( UOIUtils.createLocalJavaClass( treeNode.getClass().getName() ) );
        this.imperialTree.insert( node );
        return serviceNodeGUID;
    }

    @Override
    public void purge( GUID guid ) {
        this.removeNode( guid );
    }

    @Override
    public TaskElement get(GUID guid ) {
        GUIDImperialTrieNode node = this.imperialTree.getNode(guid);
        TaskElement serviceElement = new GenericTaskElement();
        if( node.getNodeMetadataGUID() != null ){
            serviceElement = this.serviceMetaManipulator.getServiceMeta( node.getNodeMetadataGUID() );
        }

        this.applyCommonMeta( serviceElement, this.commonDataManipulator.getNodeCommonData( guid ) );

        serviceElement.setDistributedTreeNode(node);
        serviceElement.setGuid( guid );
        serviceElement.setName( this.taskNodeManipulator.getServiceNode(guid).getName() );

        return serviceElement;
    }

    @Override
    public TaskElement get(GUID guid, int depth ) {
        return this.get( guid );
    }

    @Override
    public TaskElement getSelf(GUID guid ) {
        return this.get( guid );
    }

    @Override
    public void update( TreeNode nodeWideData ) {
        GenericTaskElement serviceElement = (GenericTaskElement) nodeWideData;
        this.taskNodeManipulator.update( serviceElement );
        this.serviceMetaManipulator.update( serviceElement );
        this.commonDataManipulator.update( serviceElement );
    }

    @Override
    public void updateName(GUID guid, String name) {

    }

    private void removeNode( GUID guid ){
        GUIDImperialTrieNode node = this.imperialTree.getNode(guid);
        this.imperialTree.purge( guid );
        this.imperialTree.removeCachePath( guid );
        this.taskNodeManipulator.remove( node.getGuid() );
        this.serviceMetaManipulator.remove( node.getAttributesGUID() );
        this.commonDataManipulator.remove( node.getNodeMetadataGUID() );
    }
}
