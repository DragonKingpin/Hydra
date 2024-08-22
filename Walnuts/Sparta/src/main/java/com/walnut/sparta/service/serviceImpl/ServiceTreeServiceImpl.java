package com.walnut.sparta.service.serviceImpl;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.ServiceTreeMapper;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.walnut.sparta.mapper.GenericApplicationNodeManipinate;
import com.walnut.sparta.mapper.GenericClassifNodeManipinate;
import com.walnut.sparta.mapper.GenericServiceNodeManipinate;
import com.walnut.sparta.service.ServiceTreeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service

public class ServiceTreeServiceImpl implements ServiceTreeService {
    @Resource
    ServiceTreeMapper serviceTreeMapper;
    @Resource
    GenericApplicationNodeManipinate genericApplicationNodeManipinate;
    @Resource
    GenericClassifNodeManipinate genericClassifNodeManipinate;
    @Resource
    GenericServiceNodeManipinate genericServiceNodeManipinate;
    private final static String ApplicationNode="applicationNode";

    private final static String ServiceNode="serviceNode";

    private final static String ClassifNode="classifNode";
    @Override
    public void addNodeToParent(GUID nodeGUID, GUID parentGUID) {
        //将节点加入指定位置
        this.serviceTreeMapper.addNodeToParent(nodeGUID,parentGUID);
        //添加后要更新节点路径
        //递归查询所有要更新的节点
        upDateAllPath(nodeGUID);
    }

    @Override
    public void deleteNode(GUID nodeGUID) {
        //像文件夹一样删除父文件会连带一起输出子文件
        deleteAllNode(nodeGUID);
    }

    private void deleteAllNode(GUID nodeGUID){
        List<GUIDDistributedScopeNode> childNodes = this.serviceTreeMapper.getChildNode(nodeGUID);
        this.serviceTreeMapper.deleteNode(nodeGUID);
        this.serviceTreeMapper.deletePath(nodeGUID);
        if (childNodes==null) return;
        for (GUIDDistributedScopeNode guidDistributedScopeNode:childNodes){
            deleteNode(guidDistributedScopeNode.getUUID());
        }
    }

    private void upDateAllPath(GUID guid){
        updatePath(guid);
        List<GUIDDistributedScopeNode> childNodes = this.serviceTreeMapper.getChildNode(guid);
        Debug.trace("节点"+guid+"的子节点有"+childNodes.toString());
        for(GUIDDistributedScopeNode guidDistributedScopeNode:childNodes){
            if (guidDistributedScopeNode!=null){
                upDateAllPath(guidDistributedScopeNode.getUUID());
            }
        }
    }
    private void updatePath(GUID UUID){
        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(UUID);
        //如果是分类节点还要查询分类节点的分类表
        System.out.println("查询到节点:"+node);
        String nodeName = getNodeName(node);
        String pathString="";
        if(node.getType().equals(ClassifNode)){
            String classifNodeClassif = serviceTreeMapper.getClassifNodeClassif(node.getUUID());
            if (classifNodeClassif!=null){
                pathString=pathString+"("+classifNodeClassif+")"+nodeName;
            }else {
                pathString=pathString+"("+"默认分类"+")"+nodeName;
            }
        }else {
            pathString=pathString+nodeName;
        }
        while (node.getParentUUID() != null){
            node=this.serviceTreeMapper.selectNode(node.getParentUUID());
            System.out.println("查询到节点:"+node);
            nodeName = getNodeName(node);
            if(node.getType().equals(ClassifNode)){
                String classifNodeClassif = serviceTreeMapper.getClassifNodeClassif(node.getUUID());
                if (classifNodeClassif!=null){
                    pathString="("+classifNodeClassif+")"+nodeName+"."+pathString;
                }else {
                    pathString="("+"默认分类"+")"+nodeName+"."+pathString;
                }
            }else {
                pathString=nodeName + "." + pathString;
            }
        }
        this.serviceTreeMapper.updatePath(UUID,pathString);
    }
    private String getNodeName(GUIDDistributedScopeNode node){
        if (node.getType().equals(ApplicationNode)){
            return this.genericApplicationNodeManipinate.selectApplicationNode(node.getUUID()).getName();
        }
        else if(node.getType().equals(ServiceNode)){
            return this.genericServiceNodeManipinate.selectServiceNode(node.getUUID()).getName();
        }
        else if (node.getType().equals(ClassifNode)) {
            return this.genericClassifNodeManipinate.selectClassifNode(node.getUUID()).getName();
        }
        return null;
    }
}
