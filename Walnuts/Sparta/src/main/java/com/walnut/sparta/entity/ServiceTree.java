package com.walnut.sparta.entity;


import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Component
public class ServiceTree {
    private Map<String, Node> serviceTree = new HashMap<>();
    public void addNode(Node node){
        if (node!=null){
            serviceTree.put(node.getUUID(),node);
        }else {
            //先使用普通输出后面使用系统内日志
            System.out.println("节点为空无法添加");
        }
    }
    public void removeNode(String UUID){
        serviceTree.remove(UUID);
    }
    //该查询为初版查询先用于前期开发调试还有自动上级配置逻辑没有完成
    public String getNode(String UUID){
        //先获取节点
        Node node = serviceTree.get(UUID);
        //根据节点类型获取节点详细信息，先使用字符串作为条件，后面改为枚举类
        String type = node.getType();
        if (type.equals("ClassificationNode")){
            //todo 获取分类节点详细信息逻辑
        }else if(type.equals("ServiceNode")){
            //todo 获取服务节点详细信息逻辑
        }else if(type.equals("ApplicationNode")){
            //todo 获取服务节点信息信息逻辑
        }
        //todo 根据获取的信息将其转化为json返回
        return null;
    }

    public void updateNode(Node node){
        if (node!=null){
            serviceTree.put(node.getUUID(),node);
            //todo 同步数据库操作
        }
    }
    public void createTree(Map<String, Node> serviceTree){
        this.serviceTree=serviceTree;
    }
}
