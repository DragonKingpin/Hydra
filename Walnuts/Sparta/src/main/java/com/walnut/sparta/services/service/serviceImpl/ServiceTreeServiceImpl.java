package com.walnut.sparta.services.service.serviceImpl;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.service.tree.DistributedScopeServiceTree;
import com.pinecone.hydra.service.tree.entity.GenericMetaNodeInstanceFactory;
import com.pinecone.hydra.service.tree.nodes.ServiceTreeNode;
import com.pinecone.hydra.service.tree.operator.MetaNodeOperator;
import com.pinecone.hydra.service.tree.operator.MetaNodeOperatorProxy;
import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulator;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.walnut.sparta.services.mapper.ApplicationNodeMapper;
import com.walnut.sparta.services.mapper.ClassifNodeMapper;
import com.walnut.sparta.services.mapper.ServiceNodeMapper;
import com.walnut.sparta.services.service.ServiceTreeService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Service

public class ServiceTreeServiceImpl implements ServiceTreeService {
    @Resource
    ScopeTreeManipulator                scopeTreeManipulator;
    @Resource
    private DefaultMetaNodeManipulator  defaultMetaNodeManipulator;

    private MetaNodeOperatorProxy       metaNodeOperatorProxy;

    @PostConstruct
    public void init() {
        this.metaNodeOperatorProxy = new MetaNodeOperatorProxy(this.defaultMetaNodeManipulator);
    }


    @Override
    public void addNodeToParent(GUID nodeGUID, GUID parentGUID) {
        //将节点加入指定位置
        this.scopeTreeManipulator.insertNodeToParent(nodeGUID,parentGUID);
        //添加后要更新节点路径
        //递归查询所有要更新的节点
        upDateAllPath(nodeGUID);
    }

    @Override
    public void removeNode(GUID nodeGUID) {
        //像文件夹一样删除父文件会连带一起输出子文件
        removeAllNode(nodeGUID);
    }

    private void removeAllNode(GUID nodeGUID){
        List<GUIDDistributedScopeNode> childNodes = this.scopeTreeManipulator.getChildNode(nodeGUID);
        this.scopeTreeManipulator.removeNode(nodeGUID);
        this.scopeTreeManipulator.removePath(nodeGUID);
        if (childNodes==null) return;
        for (GUIDDistributedScopeNode guidDistributedScopeNode:childNodes){
            removeNode(guidDistributedScopeNode.getGuid());
        }
    }

    private void upDateAllPath(GUID guid){
        updatePath(guid);
        List<GUIDDistributedScopeNode> childNodes = this.scopeTreeManipulator.getChildNode(guid);
        Debug.trace("节点"+guid+"的子节点有"+childNodes.toString());
        for(GUIDDistributedScopeNode guidDistributedScopeNode:childNodes){
            if (guidDistributedScopeNode!=null){
                upDateAllPath(guidDistributedScopeNode.getGuid());
            }
        }
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
    private String getNodeName(GUIDDistributedScopeNode node){
        UOI type = node.getType();
        ServiceTreeNode newInstance = (ServiceTreeNode)type.newInstance();
        MetaNodeOperator operator = metaNodeOperatorProxy.getOperator(newInstance.getMetaType());
        ServiceTreeNode serviceTreeNode = operator.get(node.getGuid());
        Debug.trace("获取到了节点"+serviceTreeNode);
        return serviceTreeNode.getName();
    }
}
