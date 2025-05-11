package com.pinecone.hydra.deploy.kom.operator;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.ClusterElement;
import com.pinecone.hydra.deploy.kom.entity.GenericClusterElement;
import com.pinecone.hydra.system.ko.UOIUtils;
import com.pinecone.hydra.deploy.kom.entity.GenericNamespace;
import com.pinecone.hydra.deploy.kom.source.JobNodeManipulator;
import com.pinecone.hydra.deploy.kom.source.DeployMasterManipulator;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class JobElementOperator extends ArchElementOperator implements ElementOperator {
    protected JobNodeManipulator jobNodeManipulator;

    public JobElementOperator(ElementOperatorFactory factory ) {
        this( factory.getTaskMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }

    public JobElementOperator(DeployMasterManipulator masterManipulator, DeployInstrument deployInstrument){
        super( masterManipulator, deployInstrument);
        this.jobNodeManipulator = masterManipulator.getJobNodeManipulator();
    }


    @Override
    public GUID insert( TreeNode treeNode ) {
        GenericClusterElement jobElement = (GenericClusterElement) treeNode;

        GuidAllocator guidAllocator = this.deployInstrument.getGuidAllocator();
        GUID jobNodeGUID = guidAllocator.nextGUID();
        jobElement.setGuid( jobNodeGUID );
        this.jobNodeManipulator.insert( jobElement );

        //将应用元信息存入元信息表
        this.nodeMetaManipulator.insert( jobElement );


        //将节点信息存入主表
        GUIDImperialTrieNode node = new GUIDImperialTrieNode();
        node.setNodeMetadataGUID(jobNodeGUID);
        node.setGuid(jobNodeGUID);
        node.setType( UOIUtils.createLocalJavaClass( treeNode.getClass().getName() ) );
        this.imperialTree.insert( node );
        return jobNodeGUID;
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
                TreeNode newInstance = (TreeNode)uoi.newInstance( new Class<? >[]{ DeployInstrument.class }, this.deployInstrument);
                metaType = newInstance.getMetaType();
            }

            ElementOperator operator = this.getOperatorFactory().getOperator( metaType );
            operator.purge( guid );
        }
    }

    @Override
    public ClusterElement get(GUID guid ) {
        ClusterElement clusterElement;
        clusterElement = this.jobNodeManipulator.getJobElement( guid, this.deployInstrument);
        this.applyCommonMeta(clusterElement, this.nodeMetaManipulator.getNodeCommonMeta( guid ) );

        clusterElement.setGuid(clusterElement.getGuid());
        return clusterElement;
    }

    @Override
    public ClusterElement get(GUID guid, int depth ) {
        return this.get( guid );
    }

    @Override
    public ClusterElement getAsRootDepth(GUID guid ) {
        return this.get( guid );
    }

    @Override
    public void update( TreeNode treeNode ) {
        GenericClusterElement applicationElement = (GenericClusterElement) treeNode;
        this.jobNodeManipulator.update( applicationElement );
        this.nodeMetaManipulator.update( applicationElement );
    }

    @Override
    public void updateName( GUID guid, String name ) {

    }

    protected void removeNode( GUID guid ){
        GUIDImperialTrieNode node = this.imperialTree.getNode( guid );
        this.imperialTree.purge( guid );
        this.imperialTree.removeCachePath(guid);
        this.nodeMetaManipulator.remove( node.getNodeMetadataGUID() );
        this.jobNodeManipulator.remove( node.getGuid( ));
    }
}
