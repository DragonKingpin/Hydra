package com.walnut.sparta.listener;

import com.walnut.sparta.entity.Node;
import com.walnut.sparta.event.NodeDeleteEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;

import java.util.List;

public class NodeDeletedEventListener implements ApplicationListener<NodeDeleteEvent> {
    @Override
    public void onApplicationEvent(NodeDeleteEvent event) {
        // 进行更新服务树的操作
        Node node = event.getNode();
        List<Node> childNodes = event.getChildNodes();

        // 以该节点为父节点的节点，将其挂载到该节点的父节点下
        for (Node childNode : childNodes) {
            childNode.setParentUUID(node.getParentUUID());
        }

        // 调用回调方法
        //event.getCallback().accept(childNodes);
    }

}
