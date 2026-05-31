package com.pinecone.hydra.service.kom.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.kom.entity.ApplicationElement;
import com.pinecone.hydra.service.kom.entity.GenericApplicationElement;
import com.pinecone.hydra.service.kom.entity.GenericNamespace;
import com.pinecone.hydra.service.kom.source.ApplicationNodeManipulator;
import com.pinecone.hydra.service.kom.source.ServiceMasterManipulator;
import com.pinecone.hydra.system.ko.UOIUtils;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.framework.util.id.GuidAllocator;

import java.util.List;

public class ApplicationElementOperator extends ArchElementOperator implements ElementOperator {
    protected ApplicationNodeManipulator        applicationNodeManipulator;

    public ApplicationElementOperator(ElementOperatorFactory factory ) {
        this( factory.getServiceMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }

    public ApplicationElementOperator(ServiceMasterManipulator masterManipulator, ServiceInstrument serviceInstrument){
        super( masterManipulator, serviceInstrument);
        this.applicationNodeManipulator = masterManipulator.getApplicationNodeManipulator();
    }


    @Override
    public GUID insert( TreeNode treeNode ) {
        GenericApplicationElement applicationElement = (GenericApplicationElement) treeNode;

        GuidAllocator guidAllocator = this.serviceInstrument.getGuidAllocator();
        GUID applicationNodeGUID = guidAllocator.nextGUID();
        applicationElement.setGuid( applicationNodeGUID );
        this.applicationNodeManipulator.insert( applicationElement );

        //将节点信息存入主表
        GUIDImperialTrieNode node = new GUIDImperialTrieNode();
        node.setGuid(applicationNodeGUID);
        node.setType( UOIUtils.createLocalJavaClass( treeNode.getClass().getName() ) );
        this.imperialTree.insert( node );
        return applicationNodeGUID;
    }


    @Override
    public void purge( GUID guid ) {
        //namespace节点需要递归删除其拥有节点若其引用节点，没有其他引用则进行清理
        List<GUIDImperialTrieNode> childNodes = this.imperialTree.getChildren(guid);
        GUIDImperialTrieNode node = this.imperialTree.getNode(guid);
        if ( !childNodes.isEmpty() ){
            List<GUID > subordinates = this.imperialTree.getSubordinates(guid);
            if ( !subordinates.isEmpty() ){
                for ( GUID subordinateGuid : subordinates ){
                    this.purge( subordinateGuid );
                }
            }
            childNodes = this.imperialTree.getChildren( guid );
            for( GUIDImperialTrieNode childNode : childNodes ){
                List<GUID > parentNodes = this.imperialTree.fetchParentGuids(childNode.getGuid());
                if ( parentNodes.size() > 1 ){
                    this.imperialTree.removeInheritance(childNode.getGuid(),guid);
                }
                else {
                    this.purge( childNode.getGuid() );
                }
            }
        }

        if ( node.getType().getObjectName().equals( GenericNamespace.class.getName() ) ){
            this.removeNode(guid);
        }
        else {
            UOI uoi = node.getType();
            String metaType = this.getOperatorFactory().getMetaType( uoi.getObjectName() );
            if( metaType == null ) {
                TreeNode newInstance = (TreeNode)uoi.newInstance( new Class<? >[]{ ServiceInstrument.class }, this.serviceInstrument);
                metaType = newInstance.getMetaType();
            }

            ElementOperator operator = this.getOperatorFactory().getOperator( metaType );
            operator.purge( guid );
        }
    }

    @Override
    public ApplicationElement get( GUID guid ) {
        GUIDImperialTrieNode node = this.imperialTree.getNode( guid );
        ApplicationElement applicationElement = this.applicationNodeManipulator.getApplicationNode(guid);
        if ( applicationElement == null ) {
            applicationElement = new GenericApplicationElement();
        }

        applicationElement.setDistributedTreeNode( node );
        applicationElement.setGuid( guid );
        return applicationElement;
    }

    @Override
    public ApplicationElement get( GUID guid, int depth ) {
        return this.get( guid );
    }

    @Override
    public ApplicationElement getAsRootDepth( GUID guid ) {
        return this.get( guid );
    }

    @Override
    public void update( TreeNode treeNode ) {
        GenericApplicationElement applicationElement = (GenericApplicationElement) treeNode;
        this.applicationNodeManipulator.update( applicationElement );
    }

    @Override
    public void updateName( GUID guid, String name ) {

    }

    protected void removeNode( GUID guid ){
        GUIDImperialTrieNode node = this.imperialTree.getNode( guid );
        this.imperialTree.purge( guid );
        this.imperialTree.removeCachePath(guid);
        this.applicationNodeManipulator.remove( node.getGuid( ));
    }
}
