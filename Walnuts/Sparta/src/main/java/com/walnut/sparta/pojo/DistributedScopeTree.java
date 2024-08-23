package com.walnut.sparta.pojo;


import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.ServiceTreeMapper;
import com.pinecone.hydra.unit.udsn.ApplicationNodeManipinate;
import com.pinecone.hydra.unit.udsn.ClassifNodeManipinate;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.unit.udsn.ServiceNodeManipinate;

/**
 * 提供服务树的相应方法
 */public class DistributedScopeTree implements Pinenut {
    private ServiceTreeMapper serviceTreeMapper;

    private ApplicationNodeManipinate applicationNodeManipinate;

    private ServiceNodeManipinate serviceNodeManipinate;

    private ClassifNodeManipinate classifNodeManipinate;


    public DistributedScopeTree(ServiceTreeMapper serviceTreeMapper,ApplicationNodeManipinate applicationNodeManipinate,
                                ServiceNodeManipinate serviceNodeManipinate,ClassifNodeManipinate classifNodeManipinate){
        this.serviceTreeMapper=serviceTreeMapper;
        this.applicationNodeManipinate=applicationNodeManipinate;
        this.serviceNodeManipinate=serviceNodeManipinate;
        this.classifNodeManipinate=classifNodeManipinate;
    }

    private final static String ApplicationNode="applicationNode";

    private final static String ServiceNode="serviceNode";

    private final static String ClassifNode="classifNode";


    //打印路径信息
    public String getPath(GUID UUID){
        String path = this.serviceTreeMapper.selectPath(UUID);
        System.out.println("查找到路径："+path);
        //若不存在path信息则更新缓存表
        if (path==null){
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
            this.serviceTreeMapper.savePath(pathString,UUID);
            return pathString;
        }
        return path;

    }
    public void addNodeToParent(GUID nodeGUID,GUID parentGUID){
        this.serviceTreeMapper.addNodeToParent(nodeGUID,parentGUID);
    }

    private String getNodeName(GUIDDistributedScopeNode node){

        if (node.getType().equals(ApplicationNode)){
            return this.applicationNodeManipinate.selectApplicationNode(node.getUUID()).getName();
        }
        else if(node.getType().equals(ServiceNode)){
            System.out.println(node.getUUID());
            return this.serviceNodeManipinate.selectServiceNode(node.getUUID()).getName();
        }
        else if (node.getType().equals(ClassifNode)) {
            return this.classifNodeManipinate.selectClassifNode(node.getUUID()).getName();
        }
        return null;
    }



}
