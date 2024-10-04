package com.pinecone.hydra.service.tree;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.service.tree.entity.GenericMetaNodeInstanceFactory;
import com.pinecone.hydra.service.tree.entity.MetaNodeInstance;
import com.pinecone.hydra.service.tree.entity.MetaNodeInstanceFactory;
import com.pinecone.hydra.service.tree.entity.MetaNodeWideEntity;
import com.pinecone.hydra.service.tree.nodes.GenericApplicationNode;
import com.pinecone.hydra.service.tree.nodes.GenericClassificationNode;
import com.pinecone.hydra.service.tree.nodes.GenericServiceNode;
import com.pinecone.hydra.service.tree.nodes.ServiceTreeNode;
import com.pinecone.hydra.service.tree.operator.MetaNodeOperator;
import com.pinecone.hydra.service.tree.source.ServiceMasterManipulator;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.unit.udtt.DistributedTrieTree;
import com.pinecone.hydra.service.tree.source.ApplicationNodeManipulator;
import com.pinecone.hydra.service.tree.source.ClassifNodeManipulator;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.hydra.service.tree.source.ServiceNodeManipulator;
import com.pinecone.hydra.service.tree.operator.MetaNodeOperatorProxy;
import com.pinecone.hydra.unit.udtt.GenericDistributedTrieTree;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;

import java.util.List;

public class DistributedScopeServiceTree implements ScopeServiceTree {
    protected Hydrarum                  hydrarum;
    //GenericDistributedScopeTree
    private DistributedTrieTree         distributedTrieTree;

    MetaNodeInstanceFactory             metaNodeInstanceFactory;

    private ServiceMasterManipulator    serviceMasterManipulator;
    private MetaNodeOperatorProxy       metaNodeOperatorProxy;
    private ApplicationNodeManipulator  applicationNodeManipulator;
    private ServiceNodeManipulator      serviceNodeManipulator;
    private ClassifNodeManipulator      classifNodeManipulator;



    public DistributedScopeServiceTree(Hydrarum hydrarum, KOIMasterManipulator masterManipulator){
        Debug.trace(masterManipulator);
        this.hydrarum = hydrarum;
        this.serviceMasterManipulator    = (ServiceMasterManipulator) masterManipulator;
        KOISkeletonMasterManipulator skeletonMasterManipulator = this.serviceMasterManipulator.getSkeletonMasterManipulator();
        TreeMasterManipulator        treeMasterManipulator     = (TreeMasterManipulator) skeletonMasterManipulator;
        this.distributedTrieTree = new GenericDistributedTrieTree(treeMasterManipulator);
        this.applicationNodeManipulator  = this.serviceMasterManipulator.getApplicationNodeManipulator();
        this.serviceNodeManipulator      = this.serviceMasterManipulator.getServiceNodeManipulator();
        this.classifNodeManipulator      = this.serviceMasterManipulator.getClassifNodeManipulator();
        this.metaNodeOperatorProxy       = new MetaNodeOperatorProxy( this.serviceMasterManipulator);
        this.metaNodeInstanceFactory     = new GenericMetaNodeInstanceFactory(this.serviceMasterManipulator,treeMasterManipulator);
    }

    public DistributedScopeServiceTree( Hydrarum hydrarum ) {
        this.hydrarum = hydrarum;
    }

    public DistributedScopeServiceTree( KOIMappingDriver driver ) {
        this(
                driver.getSystem(),
                driver.getMasterManipulator()
        );
    }


    @Override
    public GUID addNode( ServiceTreeNode node ) {
        Debug.trace(node.getMetaType());
        MetaNodeOperator nodeOperation = this.metaNodeOperatorProxy.getOperator( node.getMetaType() );
        return nodeOperation.insert( node );
    }

    @Override
    public void removeNode(GUID guid){
        GUIDDistributedTrieNode guidDistributedTrieNode = this.distributedTrieTree.getNode(guid);
        UOI type = guidDistributedTrieNode.getType();
        ServiceTreeNode newInstance = (ServiceTreeNode)type.newInstance();
        MetaNodeOperator operator = metaNodeOperatorProxy.getOperator(newInstance.getMetaType());
        operator.remove(guid);
        this.distributedTrieTree.purge( guid );
    }

    /**
     * Affirm path exist in cache, if required.
     * 确保路径存在于缓存，如果有明确实现必要的话。
     * 对于GenericDistributedScopeTree::getPath, 默认会自动写入缓存，因此这里可以通过getPath保证路径缓存一定存在。
     * @param guid, target guid.
     * @return Path
     */
    protected void affirmPathExist( GUID guid ) {
        this.distributedTrieTree.getCachePath( guid );
    }

    @Override
    public ServiceTreeNode getNode( GUID guid ){
        this.affirmPathExist( guid );

        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode(guid);
        UOI type = node.getType();
        ServiceTreeNode newInstance = (ServiceTreeNode)type.newInstance();
        MetaNodeOperator operator = this.metaNodeOperatorProxy.getOperator(newInstance.getMetaType());
        return operator.get(guid);
    }

    private String getNodeName( GUIDDistributedTrieNode node ){
        UOI type = node.getType();
        ServiceTreeNode newInstance = (ServiceTreeNode)type.newInstance();
        MetaNodeOperator operator = this.metaNodeOperatorProxy.getOperator(newInstance.getMetaType());
        ServiceTreeNode serviceTreeNode = operator.get(node.getGuid());
        Debug.trace("获取到了节点" + serviceTreeNode);
        return serviceTreeNode.getName();
    }


    @Override
    public ServiceTreeNode parsePath(String path) {
        // 先查看缓存表中是否存在路径信息
        GUID guid = this.distributedTrieTree.queryGUIDByPath( path );
        if (guid != null) {
            return getNode(guid);
        }

        // 如果不存在，则根据路径信息获取节点信息并且更新缓存表
        // 分割路径，并处理括号
        String[] parts = this.processPath(path).split("\\.");

        // 根据最后一个节点尝试查找 ServiceNode
        List<GenericServiceNode> genericServiceNodes = this.serviceNodeManipulator.fetchServiceNodeByName(parts[parts.length - 1]);
        for (GenericServiceNode genericServiceNode : genericServiceNodes) {
            String nodePath = this.distributedTrieTree.getCachePath(genericServiceNode.getGuid());
            if (nodePath.equals(path)) {
                return getNode(genericServiceNode.getGuid());
            }
        }

        // 根据最后一个节点尝试查找 ApplicationNode
        List<GenericApplicationNode> genericApplicationNodes = this.applicationNodeManipulator.fetchApplicationNodeByName(parts[parts.length - 1]);
        for (GenericApplicationNode genericApplicationNode : genericApplicationNodes) {
            String nodePath = this.distributedTrieTree.getCachePath(genericApplicationNode.getGuid());
            if (nodePath.equals(path)) {
                return getNode(genericApplicationNode.getGuid());
            }
        }

        // 根据最后一个节点尝试查找 ClassificationNode
        List<GenericClassificationNode> genericClassificationNodes = this.classifNodeManipulator.fetchClassifNodeByName(parts[parts.length - 1]);
        for (GenericClassificationNode genericClassificationNode : genericClassificationNodes) {
            String nodePath = this.distributedTrieTree.getCachePath(genericClassificationNode.getGuid());
            if (nodePath.equals(path)) {
                return getNode(genericClassificationNode.getGuid());
            }
        }

        return null;
    }

    @Override
    public void remove(GUID guid) {
        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode(guid);
        MetaNodeInstance uniformObjectWideTable = this.metaNodeInstanceFactory.getUniformObjectWideTable(node.getType().getObjectName());
        uniformObjectWideTable.remove(guid);
    }

    @Override
    public MetaNodeWideEntity getWideMeta(GUID guid) {
        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode(guid);
        MetaNodeInstance uniformObjectWideTable = this.metaNodeInstanceFactory.getUniformObjectWideTable(node.getType().getObjectName());
        return uniformObjectWideTable.get(guid);
    }

    @Override
    public String getPath(GUID guid) {
        String cachePath = this.distributedTrieTree.getCachePath( guid );
        Debug.trace( "查找到路径：" + cachePath );
        //若不存在path信息则更新缓存表
        if ( cachePath == null ){
            GUIDDistributedTrieNode node = this.distributedTrieTree.getNode( guid );
            //查看是否具有拥有关系
            GUID owner = this.distributedTrieTree.getOwner(node.getGuid());
            if ( owner == null ){
                String nodeName = this.getNodeName(node);

                // Assemble new path, if cache path dose not exist.
                String assemblePath = nodeName;
                while ( !node.getParentGUIDs().isEmpty() ){
                    Debug.trace("获取到了节点" + node);
                    List<GUID> parentGUIDs = node.getParentGUIDs();
                    node = this.distributedTrieTree.getNode(parentGUIDs.get(0));
                    nodeName = this.getNodeName(node);
                    assemblePath = nodeName + "." + assemblePath;
                }
                this.distributedTrieTree.insertCachePath( guid, assemblePath );
                return assemblePath;
            }
            else {
                String nodeName = this.getNodeName(node);

                // Assemble new path, if cache path dose not exist.
                String assemblePath = nodeName;
                while ( !node.getParentGUIDs().isEmpty() ){
                    node = this.distributedTrieTree.getNode(owner);
                    Debug.trace("获取到了节点" + node);
                    nodeName = this.getNodeName(node);
                    assemblePath = nodeName + "." + assemblePath;
                }
                this.distributedTrieTree.insertCachePath( guid, assemblePath );
                return assemblePath;
            }

        }
        return cachePath;
    }

    private String processPath(String path) {
        // 使用正则表达式移除所有的括号及其内容
        return path.replaceAll("\\(.*?\\)", "");
    }
}
