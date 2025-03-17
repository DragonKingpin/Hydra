package com.pinecone.hydra.task.kom.operator;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.task.kom.ServiceInstrument;
import com.pinecone.hydra.task.kom.entity.GenericJobElement;
import com.pinecone.hydra.task.kom.entity.JobElement;
import com.pinecone.hydra.task.kom.entity.GenericNamespace;
import com.pinecone.hydra.task.kom.source.JobMetaManipulator;
import com.pinecone.hydra.task.kom.source.JobNodeManipulator;
import com.pinecone.hydra.task.kom.source.TaskMasterManipulator;
import com.pinecone.hydra.system.ko.UOIUtils;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class JobElementOperator extends ArchElementOperator implements ElementOperator {
    protected JobNodeManipulator jobNodeManipulator;
    protected JobMetaManipulator jobMetaManipulator;

    public JobElementOperator(ElementOperatorFactory factory ) {
        this( factory.getTaskMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }

    public JobElementOperator(TaskMasterManipulator masterManipulator, ServiceInstrument serviceInstrument){
        super( masterManipulator, serviceInstrument);
        this.jobNodeManipulator = masterManipulator.getJobNodeManipulator();
        this.jobMetaManipulator = masterManipulator.getApplicationElementManipulator();
    }


    @Override
    public GUID insert( TreeNode treeNode ) {
        GenericJobElement applicationElement = (GenericJobElement) treeNode;

        GuidAllocator guidAllocator = this.serviceInstrument.getGuidAllocator();
        GUID applicationNodeGUID = guidAllocator.nextGUID();
        applicationElement.setGuid( applicationNodeGUID );
        this.jobNodeManipulator.insert( applicationElement );


        GUID descriptionGUID = guidAllocator.nextGUID();
        if( applicationElement.getMetaGuid() == null ){
            applicationElement.setMetaGuid( descriptionGUID );
        }
        this.jobMetaManipulator.insert( applicationElement );


        //将应用元信息存入元信息表
        this.commonDataManipulator.insert( applicationElement );


        //将节点信息存入主表
        GUIDImperialTrieNode node = new GUIDImperialTrieNode();
        node.setNodeMetadataGUID(descriptionGUID);
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
    public JobElement get(GUID guid ) {
        GUIDImperialTrieNode node = this.imperialTree.getNode( guid );
        JobElement jobElement;
        if( node.getNodeMetadataGUID() != null ){
            jobElement = this.jobMetaManipulator.getJobElement( node.getNodeMetadataGUID(), this.serviceInstrument);
        }
        else {
            jobElement = new GenericJobElement();
        }

        this.applyCommonMeta(jobElement, this.commonDataManipulator.getNodeCommonData( guid ) );

        jobElement.setName( this.jobNodeManipulator.getJobElement(guid).getName() );
        jobElement.setGuid(jobElement.getGuid());
        return jobElement;
    }

    @Override
    public JobElement get(GUID guid, int depth ) {
        return this.get( guid );
    }

    @Override
    public JobElement getSelf(GUID guid ) {
        return this.get( guid );
    }

    @Override
    public void update( TreeNode treeNode ) {
        GenericJobElement applicationElement = (GenericJobElement) treeNode;
        this.jobNodeManipulator.update( applicationElement );
        this.jobMetaManipulator.update( applicationElement );
        this.commonDataManipulator.update( applicationElement );
    }

    @Override
    public void updateName( GUID guid, String name ) {

    }

    protected void removeNode( GUID guid ){
        GUIDImperialTrieNode node = this.imperialTree.getNode( guid );
        this.imperialTree.purge( guid );
        this.imperialTree.removeCachePath(guid);
        this.jobMetaManipulator.remove( node.getAttributesGUID() );
        this.commonDataManipulator.remove( node.getNodeMetadataGUID() );
        this.jobNodeManipulator.remove( node.getGuid( ));
    }
}
