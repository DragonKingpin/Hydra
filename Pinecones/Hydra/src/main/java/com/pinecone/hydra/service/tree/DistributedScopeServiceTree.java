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
import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulators;
import com.pinecone.hydra.unit.udsn.DistributedScopeTree;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import com.pinecone.hydra.service.tree.source.ApplicationNodeManipulator;
import com.pinecone.hydra.service.tree.source.ClassifNodeManipulator;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.service.tree.source.ServiceNodeManipulator;
import com.pinecone.hydra.service.tree.operator.MetaNodeOperatorProxy;
import com.pinecone.hydra.unit.udsn.GenericDistributedScopeTree;

import java.util.List;

public class DistributedScopeServiceTree implements ScopeServiceTree {
    //GenericDistributedScopeTree
    private DistributedScopeTree        distributedScopeTree;

    MetaNodeInstanceFactory             metaNodeInstanceFactory;

    private DefaultMetaNodeManipulators defaultMetaNodeManipulators;
    private MetaNodeOperatorProxy       metaNodeOperatorProxy;
    private ApplicationNodeManipulator  applicationNodeManipulator;
    private ServiceNodeManipulator      serviceNodeManipulator;
    private ClassifNodeManipulator      classifNodeManipulator;



    public DistributedScopeServiceTree( DefaultMetaNodeManipulators manipulators ){
        this.defaultMetaNodeManipulators = manipulators;
        this.applicationNodeManipulator  = manipulators.getApplicationNodeManipulator();
        this.serviceNodeManipulator      = manipulators.getServiceNodeManipulator();
        this.classifNodeManipulator      = manipulators.getClassifNodeManipulator();
        this.distributedScopeTree        = new GenericDistributedScopeTree(this.defaultMetaNodeManipulators);
        this.metaNodeOperatorProxy       = new MetaNodeOperatorProxy( this.defaultMetaNodeManipulators);
        this.metaNodeInstanceFactory     = new GenericMetaNodeInstanceFactory(this.defaultMetaNodeManipulators);
    }



    @Override
    public GUID addNode( ServiceTreeNode node ) {
        Debug.trace(node.getMetaType());
        MetaNodeOperator nodeOperation = this.metaNodeOperatorProxy.getOperator( node.getMetaType() );
        return nodeOperation.insert( node );
    }

    @Override
    public void removeNode(GUID guid){
        GUIDDistributedScopeNode guidDistributedScopeNode = this.distributedScopeTree.getNode(guid);
        UOI type = guidDistributedScopeNode.getType();
        ServiceTreeNode newInstance = (ServiceTreeNode)type.newInstance();
        MetaNodeOperator operator = metaNodeOperatorProxy.getOperator(newInstance.getMetaType());
        operator.remove(guid);
        this.distributedScopeTree.remove(guid);
    }

    /**
     * Affirm path exist in cache, if required.
     * 确保路径存在于缓存，如果有明确实现必要的话。
     * 对于GenericDistributedScopeTree::getPath, 默认会自动写入缓存，因此这里可以通过getPath保证路径缓存一定存在。
     * @param guid, target guid.
     * @return Path
     */
    protected void affirmPathExist( GUID guid ) {
        this.distributedScopeTree.getPath( guid );
    }

    @Override
    public ServiceTreeNode getNode( GUID guid ){
        this.affirmPathExist( guid );

        GUIDDistributedScopeNode node = this.distributedScopeTree.getNode(guid);
        UOI type = node.getType();
        ServiceTreeNode newInstance = (ServiceTreeNode)type.newInstance();
        MetaNodeOperator operator = this.metaNodeOperatorProxy.getOperator(newInstance.getMetaType());
        return operator.get(guid);
    }

    private String getNodeName( GUIDDistributedScopeNode node ){
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
        GUID guid = this.distributedScopeTree.parsePath(path);
        if (guid != null) {
            return getNode(guid);
        }

        // 如果不存在，则根据路径信息获取节点信息并且更新缓存表
        // 分割路径，并处理括号
        String[] parts = this.processPath(path).split("\\.");

        // 根据最后一个节点尝试查找 ServiceNode
        List<GenericServiceNode> genericServiceNodes = this.serviceNodeManipulator.fetchServiceNodeByName(parts[parts.length - 1]);
        for (GenericServiceNode genericServiceNode : genericServiceNodes) {
            String nodePath = this.distributedScopeTree.getPath(genericServiceNode.getGuid());
            if (nodePath.equals(path)) {
                return getNode(genericServiceNode.getGuid());
            }
        }

        // 根据最后一个节点尝试查找 ApplicationNode
        List<GenericApplicationNode> genericApplicationNodes = this.applicationNodeManipulator.fetchApplicationNodeByName(parts[parts.length - 1]);
        for (GenericApplicationNode genericApplicationNode : genericApplicationNodes) {
            String nodePath = this.distributedScopeTree.getPath(genericApplicationNode.getGuid());
            if (nodePath.equals(path)) {
                return getNode(genericApplicationNode.getGuid());
            }
        }

        // 根据最后一个节点尝试查找 ClassificationNode
        List<GenericClassificationNode> genericClassificationNodes = this.classifNodeManipulator.fetchClassifNodeByName(parts[parts.length - 1]);
        for (GenericClassificationNode genericClassificationNode : genericClassificationNodes) {
            String nodePath = this.distributedScopeTree.getPath(genericClassificationNode.getGuid());
            if (nodePath.equals(path)) {
                return getNode(genericClassificationNode.getGuid());
            }
        }

        return null;
    }

    @Override
    public void remove(GUID guid) {
        GUIDDistributedScopeNode node = this.distributedScopeTree.getNode(guid);
        MetaNodeInstance uniformObjectWideTable = this.metaNodeInstanceFactory.getUniformObjectWideTable(node.getType().getObjectName());
        uniformObjectWideTable.remove(guid);
    }

    @Override
    public MetaNodeWideEntity getWideMeta(GUID guid) {
        GUIDDistributedScopeNode node = this.distributedScopeTree.getNode(guid);
        MetaNodeInstance uniformObjectWideTable = this.metaNodeInstanceFactory.getUniformObjectWideTable(node.getType().getObjectName());
        return uniformObjectWideTable.get(guid);
    }

    private String processPath(String path) {
        // 使用正则表达式移除所有的括号及其内容
        return path.replaceAll("\\(.*?\\)", "");
    }
}
