package com.walnut.sparta.listener;


import com.walnut.sparta.entity.Node;
import com.walnut.sparta.entity.ServiceTree;
import com.walnut.sparta.mapper.SystemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 在服务启动时监听并加载所有的Node信息将其导入到服务树中
 */
@Component
public class NodeLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final SystemMapper systemMapper;
    private final ServiceTree serviceTree;

    @Autowired
    public NodeLoader(SystemMapper systemMapper, ServiceTree serviceTree) {
        this.systemMapper = systemMapper;
        this.serviceTree = serviceTree;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        loadNodesFromDatabase();
    }

    private void loadNodesFromDatabase() {
       //获取所有节点
        List<Node> nodes = systemMapper.selectAllNode();
        for (Node node:nodes){
            serviceTree.addNode(node);
        }
        System.out.println(serviceTree.toString());
    }
}
