package com.walnut.sparta.listener;

import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.walnut.sparta.event.NodeDeleteEvent;

import org.springframework.context.ApplicationListener;

import java.util.List;

public class NodeDeletedEventListener implements ApplicationListener<NodeDeleteEvent> {
    @Override
    public void onApplicationEvent(NodeDeleteEvent event) {
        // 进行更新服务树的操作
        GUIDDistributedScopeNode node = event.getNode();
        List<GUIDDistributedScopeNode> childNodes = event.getChildNodes();

        // 以该节点为父节点的节点，将其挂载到该节点的父节点下
        for (GUIDDistributedScopeNode childNode : childNodes) {
            childNode.setParentUUID(node.getParentUUID());
        }

        // 调用回调方法
        //event.getCallback().accept(childNodes);
    }

}
