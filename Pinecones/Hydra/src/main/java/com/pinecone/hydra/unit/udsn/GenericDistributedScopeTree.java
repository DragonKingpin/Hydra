package com.pinecone.hydra.unit.udsn;


import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.ServiceTreeMapper;
import com.pinecone.hydra.service.tree.source.ApplicationNodeManipulator;
import com.pinecone.hydra.service.tree.source.ClassifNodeManipulator;
import com.pinecone.hydra.service.tree.source.ServiceNodeManipinate;

/**
 * 提供服务树的相应方法
 */
public class GenericDistributedScopeTree implements UniDistributedScopeTree {
    private ServiceTreeMapper serviceTreeMapper;

    private ApplicationNodeManipulator applicationNodeManipinate;

    private ServiceNodeManipinate serviceNodeManipinate;

    private ClassifNodeManipulator classifNodeManipinate;


    public GenericDistributedScopeTree(ServiceTreeMapper serviceTreeMapper, ApplicationNodeManipulator applicationNodeManipinate,
                                       ServiceNodeManipinate serviceNodeManipinate, ClassifNodeManipulator classifNodeManipinate){
        this.serviceTreeMapper=serviceTreeMapper;
        this.applicationNodeManipinate=applicationNodeManipinate;
        this.serviceNodeManipinate=serviceNodeManipinate;
        this.classifNodeManipinate=classifNodeManipinate;
    }

    private final static String ApplicationNode="com.walnut.sparta.pojo.ApplicationFunctionalNodeInformation";

    private final static String ServiceNode="com.walnut.sparta.pojo.ServiceFunctionalNodeInformation";

    private final static String ClassifNode="com.walnut.sparta.pojo.ClassifFunctionalNodeInformation";


    //打印路径信息
    public String getPath(GUID guid){
        String path = this.serviceTreeMapper.selectPath(guid);
        System.out.println("查找到路径："+path);
        //若不存在path信息则更新缓存表
        if ( path == null ){
            GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(guid);
            //如果是分类节点还要查询分类节点的分类表
            System.out.println("查询到节点:"+node);
            String nodeName = getNodeName(node);
            String pathString="";

            if( node.getType().equals( ClassifNode ) ){
                String classifNodeClassif = serviceTreeMapper.getClassifNodeClassif( node.getUUID() );
                if ( classifNodeClassif != null ){
                    pathString=pathString+"("+classifNodeClassif+")"+nodeName;
                }
                else {
                    pathString=pathString+"("+"默认分类"+")"+nodeName;
                }
            }
            else {
                pathString=pathString+nodeName;
            }

            while ( node.getParentUUID() != null ){
                node = this.serviceTreeMapper.selectNode(node.getParentUUID());
                System.out.println( "查询到节点:" + node );
                nodeName = getNodeName( node );
                if(node.getType().equals(ClassifNode)){
                    String classifNodeClassif = serviceTreeMapper.getClassifNodeClassif(node.getUUID());
                    if (classifNodeClassif!=null){
                        pathString="("+classifNodeClassif+")"+nodeName+"."+pathString;
                    }
                    else {
                        pathString="("+"默认分类"+")"+nodeName+"."+pathString;
                    }
                }
                else {
                    pathString=nodeName + "." + pathString;
                }
            }
            this.serviceTreeMapper.savePath(pathString,guid);
            return pathString;
        }
        return path;

    }

    public void addNodeToParent(GUID nodeGUID,GUID parentGUID){
        this.serviceTreeMapper.addNodeToParent(nodeGUID,parentGUID);
    }

    private String getNodeName( GUIDDistributedScopeNode node ){

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

    @Override
    public boolean hasOwnProperty(Object elm) {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }
}
