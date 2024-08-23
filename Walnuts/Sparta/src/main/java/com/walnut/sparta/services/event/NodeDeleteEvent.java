package com.walnut.sparta.services.event;

import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class NodeDeleteEvent extends ApplicationEvent {

    private final GUIDDistributedScopeNode node;
    private final List<GUIDDistributedScopeNode> childNodes;

    public NodeDeleteEvent(Object source, GUIDDistributedScopeNode node, List<GUIDDistributedScopeNode> childNodes) {
        super(source); // 调用父类的带参数构造方法
        this.node = node;
        this.childNodes = childNodes;
    }

    public NodeDeleteEvent(Object source) {
        super(source); // 无参构造方法也必须调用父类的构造方法
        this.node = null; // 默认值
        this.childNodes = null; // 默认值
    }

    /**
     * 获取被删除的节点
     * @return node
     */
    public GUIDDistributedScopeNode getNode() {
        return node;
    }

    /**
     * 获取被删除节点的子节点列表
     * @return 子节点列表
     */
    public List<GUIDDistributedScopeNode> getChildNodes() {
        return childNodes;
    }

    @Override
    public String toString() {
        return "NodeDeleteEvent{" +
                "node=" + node +
                ", childNodes=" + childNodes +
                '}';
    }
}
