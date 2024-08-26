package com.walnut.sparta.services.service.serviceImpl;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.walnut.sparta.services.mapper.ApplicationNodeMapper;
import com.walnut.sparta.services.mapper.ClassifNodeMapper;
import com.walnut.sparta.services.mapper.ServiceNodeMapper;
import com.walnut.sparta.services.service.ServiceTreeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service

public class ServiceTreeServiceImpl implements ServiceTreeService {
    @Resource
    ScopeTreeManipulator scopeTreeManipulator;
    @Resource
    ApplicationNodeMapper genericApplicationNodeManipulator;
    @Resource
    ClassifNodeMapper genericClassifNodeManipulator;
    @Resource
    ServiceNodeMapper genericServiceNodeManipulator;
    private final static String ApplicationNode="applicationNode";

    private final static String ServiceNode="serviceNode";

    private final static String ClassifNode="classifNode";
    @Override
    public void addNodeToParent(GUID nodeGUID, GUID parentGUID) {
        //将节点加入指定位置
        this.scopeTreeManipulator.addNodeToParent(nodeGUID,parentGUID);
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
    private void updatePath(GUID UUID){
        GUIDDistributedScopeNode node = this.scopeTreeManipulator.getNode(UUID);
        //如果是分类节点还要查询分类节点的分类表
        System.out.println("查询到节点:"+node);
        String nodeName = getNodeName(node);
        String pathString="";
        if(node.getType().equals(ClassifNode)){
            String classifNodeClassif = scopeTreeManipulator.getClassifNodeClassif(node.getGuid());
            if (classifNodeClassif!=null){
                pathString=pathString+"("+classifNodeClassif+")"+nodeName;
            }else {
                pathString=pathString+"("+"默认分类"+")"+nodeName;
            }
        }else {
            pathString=pathString+nodeName;
        }
        while (node.getParentGUID() != null){
            node=this.scopeTreeManipulator.getNode(node.getParentGUID());
            System.out.println("查询到节点:"+node);
            nodeName = getNodeName(node);
            if(node.getType().equals(ClassifNode)){
                String classifNodeClassif = scopeTreeManipulator.getClassifNodeClassif(node.getGuid());
                if (classifNodeClassif!=null){
                    pathString="("+classifNodeClassif+")"+nodeName+"."+pathString;
                }else {
                    pathString="("+"默认分类"+")"+nodeName+"."+pathString;
                }
            }else {
                pathString=nodeName + "." + pathString;
            }
        }
        this.scopeTreeManipulator.updatePath(UUID,pathString);
    }
    private String getNodeName(GUIDDistributedScopeNode node){
        if (node.getType().equals(ApplicationNode)){
            return this.genericApplicationNodeManipulator.getApplicationNode(node.getGuid()).getName();
        }
        else if(node.getType().equals(ServiceNode)){
            return this.genericServiceNodeManipulator.getServiceNode(node.getGuid()).getName();
        }
        else if (node.getType().equals(ClassifNode)) {
            return this.genericClassifNodeManipulator.getClassifNode(node.getGuid()).getName();
        }
        return null;
    }
}
