package com.pinecone.hydra.service.kom.operator;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.kom.meta.GenericApplicationNodeMeta;
import com.pinecone.hydra.service.kom.nodes.GenericApplicationNode;
import com.pinecone.hydra.service.kom.nodes.ServiceTreeNode;
import com.pinecone.hydra.service.kom.source.ApplicationMetaManipulator;
import com.pinecone.hydra.service.kom.source.ApplicationNodeManipulator;
import com.pinecone.hydra.service.kom.source.ServiceMasterManipulator;
import com.pinecone.hydra.system.ko.UOIUtils;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;
import com.pinecone.ulf.util.id.GUIDs;
import com.pinecone.ulf.util.id.GuidAllocator;

import java.util.List;

public class ApplicationNodeOperator extends ArchServiceOperator implements ServiceOperator {
    protected ApplicationNodeManipulator        applicationNodeManipulator;
    protected ApplicationMetaManipulator        applicationMetaManipulator;

    public ApplicationNodeOperator( ServiceOperatorFactory factory ) {
        this( factory.getServiceMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }

    public ApplicationNodeOperator(ServiceMasterManipulator masterManipulator, ServicesInstrument servicesInstrument){
        super( masterManipulator, servicesInstrument);
        this.applicationNodeManipulator = masterManipulator.getApplicationNodeManipulator();
        this.applicationMetaManipulator = masterManipulator.getApplicationMetaManipulator();
    }


    @Override
    public GUID insert( TreeNode nodeWideData ) {
        Debug.trace("保存节点"+nodeWideData);
        GenericApplicationNode applicationNodeInformation = (GenericApplicationNode) nodeWideData;
        //将信息写入数据库
        //将节点信息存入应用节点表
        GuidAllocator guidAllocator = GUIDs.newGuidAllocator();
        GUID applicationNodeGUID = guidAllocator.nextGUID72();
        applicationNodeInformation.setGuid(applicationNodeGUID);
        this.applicationNodeManipulator.insert(applicationNodeInformation);

        //将应用节点基础信息存入信息表
        GUID descriptionGUID = guidAllocator.nextGUID72();
        GenericApplicationNodeMeta applicationDescription = applicationNodeInformation.getApplicationNodeMeta();
        if ( applicationDescription!=null ){
            applicationDescription.setGuid(descriptionGUID);
            this.applicationMetaManipulator.insert(applicationDescription);
        }else {
            descriptionGUID = null;
        }


        //将应用元信息存入元信息表
        this.commonDataManipulator.insert( applicationNodeInformation );


        //将节点信息存入主表
        GUIDDistributedTrieNode node = new GUIDDistributedTrieNode();
        node.setBaseDataGUID(descriptionGUID);
        node.setGuid(applicationNodeGUID);
        node.setType( UOIUtils.createLocalJavaClass( nodeWideData.getClass().getName() ) );
        this.distributedTrieTree.insert( node );
        return applicationNodeGUID;
    }


    @Override
    public void purge(GUID guid) {
        //namespace节点需要递归删除其拥有节点若其引用节点，没有其他引用则进行清理
        List<GUIDDistributedTrieNode> childNodes = this.distributedTrieTree.getChildren(guid);
        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode(guid);
        if ( !childNodes.isEmpty() ){
            List<GUID > subordinates = this.distributedTrieTree.getSubordinates(guid);
            if ( !subordinates.isEmpty() ){
                for ( GUID subordinateGuid : subordinates ){
                    this.purge( subordinateGuid );
                }
            }
            childNodes = this.distributedTrieTree.getChildren( guid );
            for( GUIDDistributedTrieNode childNode : childNodes ){
                List<GUID > parentNodes = this.distributedTrieTree.getParentGuids(childNode.getGuid());
                if ( parentNodes.size() > 1 ){
                    this.distributedTrieTree.removeInheritance(childNode.getGuid(),guid);
                }
                else {
                    this.purge( childNode.getGuid() );
                }
            }
        }

        if ( node.getType().getObjectName().equals(com.pinecone.hydra.registry.entity.GenericNamespace.class.getName()) ){
            this.removeNode(guid);
        }
        else {
            UOI uoi = node.getType();
            String metaType = this.getOperatorFactory().getMetaType( uoi.getObjectName() );
            if( metaType == null ) {
                TreeNode newInstance = (TreeNode)uoi.newInstance( new Class<? >[]{ ServicesInstrument.class }, this.servicesInstrument);
                metaType = newInstance.getMetaType();
            }

            ServiceOperator operator = this.getOperatorFactory().getOperator( metaType );
            operator.purge( guid );
        }
    }

    @Override
    public ServiceTreeNode get(GUID guid) {
        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode(guid);
        GenericApplicationNode applicationNode = ( GenericApplicationNode )this.commonDataManipulator.getNodeCommonData( guid );
        if( applicationNode == null ){
            applicationNode = new GenericApplicationNode();
        }
        GenericApplicationNodeMeta applicationDescription = this.applicationMetaManipulator.getApplicationMeta(node.getAttributesGUID());


        applicationNode.setApplicationNodeMeta(applicationDescription);
        applicationNode.setDistributedTreeNode(node);

        applicationNode.setName(this.applicationNodeManipulator.getApplicationNode(guid).getName());
        applicationNode.setGuid(applicationNode.getGuid());
        return applicationNode;
    }

    @Override
    public TreeNode get(GUID guid, int depth) {
        return null;
    }

    @Override
    public TreeNode getSelf(GUID guid) {
        return this.get( guid );
    }

    @Override
    public void update(TreeNode treeNode) {

    }

    @Override
    public void updateName(GUID guid, String name) {

    }

    private void removeNode( GUID guid ){
        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode(guid);
        this.distributedTrieTree.purge( guid );
        this.distributedTrieTree.removeCachePath(guid);
        this.applicationMetaManipulator.remove(node.getAttributesGUID());
        this.commonDataManipulator.remove(node.getNodeMetadataGUID());
        this.applicationNodeManipulator.remove(node.getGuid());
    }
}
