package com.pinecone.hydra.service.tree;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.service.tree.nodes.GenericApplicationNode;
import com.pinecone.hydra.service.tree.nodes.GenericClassificationNode;
import com.pinecone.hydra.service.tree.nodes.GenericServiceNode;
import com.pinecone.hydra.service.tree.nodes.ServiceNode;
import com.pinecone.hydra.service.tree.nodes.ServiceTreeNode;
import com.pinecone.hydra.service.tree.operator.MetaNodeOperator;
import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulator;
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
    private GenericDistributedScopeTree genericDistributedScopeTree;

    private DefaultMetaNodeManipulator defaultMetaNodeManipulator;
    private MetaNodeOperatorProxy       metaNodeOperatorProxy;

    private ScopeTreeManipulator        scopeTreeManipulator;
    private ApplicationNodeManipulator  applicationNodeManipulator;
    private ServiceNodeManipulator      serviceNodeManipulator;
    private ClassifNodeManipulator      classifNodeManipulator;



    public DistributedScopeServiceTree( DefaultMetaNodeManipulator manipulators ){
        this.defaultMetaNodeManipulator = manipulators;
        this.scopeTreeManipulator        = manipulators.getScopeTreeManipulator();
        this.applicationNodeManipulator  = manipulators.getApplicationNodeManipulator();
        this.serviceNodeManipulator      = manipulators.getServiceNodeManipulator();
        this.classifNodeManipulator      = manipulators.getClassifNodeManipulator();
        this.genericDistributedScopeTree = new GenericDistributedScopeTree(this.defaultMetaNodeManipulator);
        this.metaNodeOperatorProxy       = new MetaNodeOperatorProxy( this.defaultMetaNodeManipulator);
    }



    @Override
    public GUID addNode( ServiceTreeNode node ) {
        Debug.trace(node.getMetaType());
        MetaNodeOperator nodeOperation = this.metaNodeOperatorProxy.getOperator( node.getMetaType() );
        return nodeOperation.insert( node );
    }

    @Override
    public void removeNode(GUID guid){
        GUIDDistributedScopeNode guidDistributedScopeNode = this.scopeTreeManipulator.getNode(guid);
        UOI type = guidDistributedScopeNode.getType();
        ServiceTreeNode newInstance = (ServiceTreeNode)type.newInstance();
        MetaNodeOperator operator = metaNodeOperatorProxy.getOperator(newInstance.getMetaType());
        operator.remove(guid);
        this.genericDistributedScopeTree.remove(guid);
    }

    @Override
    public ServiceTreeNode getNode(GUID guid){
        String path = this.scopeTreeManipulator.selectPath(guid);

        if ( path == null ){
            GUIDDistributedScopeNode node = this.scopeTreeManipulator.getNode(guid);
            String nodeName = getNodeName(node);
            String pathString = "";
            pathString = pathString + nodeName;
            while ( node.getParentGUID() != null ){
                for (GUID parentGUID : node.getParentGUID()){
                    node = this.scopeTreeManipulator.getNode(parentGUID);
                    nodeName = getNodeName(node);
                    pathString = nodeName + "." + pathString;
                }
            }
            this.scopeTreeManipulator.savePath( pathString,guid );
        }

        GUIDDistributedScopeNode node = this.scopeTreeManipulator.getNode(guid);
        UOI type = node.getType();
        ServiceTreeNode newInstance = (ServiceTreeNode)type.newInstance();
        MetaNodeOperator operator = metaNodeOperatorProxy.getOperator(newInstance.getMetaType());
        return operator.get(guid);
    }

    private String getNodeName(GUIDDistributedScopeNode node){
        UOI type = node.getType();
        ServiceTreeNode newInstance = (ServiceTreeNode)type.newInstance();
        MetaNodeOperator operator = metaNodeOperatorProxy.getOperator(newInstance.getMetaType());
        ServiceTreeNode serviceTreeNode = operator.get(node.getGuid());
        Debug.trace("获取到了节点"+serviceTreeNode);
        return serviceTreeNode.getName();
    }

    private void updatePath(GUID guid){
        GUIDDistributedScopeNode node = this.scopeTreeManipulator.getNode(guid);
        String nodeName = getNodeName(node);
        String pathString="";
        pathString=pathString+nodeName;
        while (node.getParentGUID() != null){
            for (GUID parentGUID : node.getParentGUID()){
                node = this.scopeTreeManipulator.getNode(parentGUID);
                nodeName = getNodeName(node);
                pathString = nodeName + "." + pathString;
            }
        }
        this.scopeTreeManipulator.updatePath(guid,pathString);
    }

    @Override
    public ServiceTreeNode parsePath(String path) {
        // 先查看缓存表中是否存在路径信息
        GUID guid = this.scopeTreeManipulator.parsePath(path);
        if (guid != null) {
            return getNode(guid);
        }

        // 如果不存在，则根据路径信息获取节点信息并且更新缓存表
        // 分割路径，并处理括号
        String[] parts = processPath(path).split("\\.");

        // 根据最后一个节点尝试查找 ServiceNode
        List<GenericServiceNode> genericServiceNodes = this.serviceNodeManipulator.fetchServiceNodeByName(parts[parts.length - 1]);
        for (GenericServiceNode genericServiceNode : genericServiceNodes) {
            String nodePath = this.genericDistributedScopeTree.getPath(genericServiceNode.getGuid());
            if (nodePath.equals(path)) {
                return getNode(genericServiceNode.getGuid());
            }
        }

        // 根据最后一个节点尝试查找 ApplicationNode
        List<GenericApplicationNode> genericApplicationNodes = this.applicationNodeManipulator.fetchApplicationNodeByName(parts[parts.length - 1]);
        for (GenericApplicationNode genericApplicationNode : genericApplicationNodes) {
            String nodePath = this.genericDistributedScopeTree.getPath(genericApplicationNode.getGuid());
            if (nodePath.equals(path)) {
                return getNode(genericApplicationNode.getGuid());
            }
        }

        // 根据最后一个节点尝试查找 ClassificationNode
        List<GenericClassificationNode> genericClassificationNodes = this.classifNodeManipulator.fetchClassifNodeByName(parts[parts.length - 1]);
        for (GenericClassificationNode genericClassificationNode : genericClassificationNodes) {
            String nodePath = this.genericDistributedScopeTree.getPath(genericClassificationNode.getGuid());
            if (nodePath.equals(path)) {
                return getNode(genericClassificationNode.getGuid());
            }
        }

        return null;
    }

    private String processPath(String path) {
        // 使用正则表达式移除所有的括号及其内容
        return path.replaceAll("\\(.*?\\)", "");
    }
}
