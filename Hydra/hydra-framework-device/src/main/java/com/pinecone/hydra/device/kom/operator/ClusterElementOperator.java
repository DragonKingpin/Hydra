package com.pinecone.hydra.device.kom.operator;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.ClusterElement;
import com.pinecone.hydra.device.kom.entity.GenericClusterElement;
import com.pinecone.hydra.device.kom.entity.GenericNamespace;
import com.pinecone.hydra.device.kom.source.ClusterNodeManipulator;
import com.pinecone.hydra.device.kom.source.DeviceMasterManipulator;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class ClusterElementOperator extends ArchElementOperator implements ElementOperator {
    protected ClusterNodeManipulator jobNodeManipulator;

    public ClusterElementOperator(ElementOperatorFactory factory ) {
        this( factory.getTaskMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }

    public ClusterElementOperator(DeviceMasterManipulator masterManipulator, DeviceInstrument deviceInstrument){
        super( masterManipulator, deviceInstrument);
        this.jobNodeManipulator = masterManipulator.getJobNodeManipulator();
    }


    @Override
    public GUID insert( TreeNode treeNode ) {
        GenericClusterElement jobElement = (GenericClusterElement) treeNode;

        GUID jobNodeGUID = this.affirmGuid( jobElement );
        jobElement.setGuid( jobNodeGUID );
        this.jobNodeManipulator.insert( jobElement );

        GUIDImperialTrieNode node = this.newKernelNode( treeNode, jobNodeGUID );
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

        if ( node.getType().getObjectName().equals( GenericNamespace.class.getName() ) || node.getType().getObjectName().equals( GenericClusterElement.class.getName() ) ){
            this.removeNode(guid);
        }
        else {
            UOI uoi = node.getType();
            String metaType = this.getOperatorFactory().getMetaType( uoi.getObjectName() );
            if( metaType == null ) {
                TreeNode newInstance = (TreeNode)uoi.newInstance( new Class<? >[]{ DeviceInstrument.class }, this.deviceInstrument);
                metaType = newInstance.getMetaType();
            }

            ElementOperator operator = this.getOperatorFactory().getOperator( metaType );
            operator.purge( guid );
        }
    }

    @Override
    public ClusterElement get(GUID guid ) {
        GUIDImperialTrieNode node = this.imperialTree.getNode( guid );
        ClusterElement clusterElement = this.jobNodeManipulator.getClusterElement( guid, this.deviceInstrument);
        if( clusterElement == null ) {
            return null;
        }
        this.applyTreeNode( clusterElement, node );
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
        this.touchForUpdate( applicationElement );
        this.jobNodeManipulator.update( applicationElement );
        this.imperialTree.removeCachePath( applicationElement.getGuid() );
    }

    @Override
    public void updateName( GUID guid, String name ) {

    }

    protected void removeNode( GUID guid ){
        GUIDImperialTrieNode node = this.imperialTree.getNode( guid );
        this.imperialTree.purge( guid );
        this.imperialTree.removeCachePath(guid);
        this.jobNodeManipulator.remove( node.getGuid( ));
    }
}
