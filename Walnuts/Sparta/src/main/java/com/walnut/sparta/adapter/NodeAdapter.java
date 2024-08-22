package com.walnut.sparta.adapter;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.service.NodeInformation;
import com.pinecone.hydra.service.NodeOperation;

import java.util.HashMap;
import java.util.Map;

public class NodeAdapter {
    private static final Map<Class<? extends NodeInformation>, NodeOperation> operationMap = new HashMap<>();

    public static void registration(Class<? extends NodeInformation> nodeInfoClass, NodeOperation nodeOperation) {
        // 将 NodeInformation 的实现类和对应的 NodeOperation 存储到 HashMap 中
        operationMap.put(nodeInfoClass, nodeOperation);
    }

    public static NodeOperation getNodeOperation(NodeInformation nodeInformation) {
        // 获取 NodeInformation 的具体实现类
        Class<? extends NodeInformation> nodeInfoClass = nodeInformation.getClass();

        // 从 map 中获取对应的 NodeOperation 实例
        return operationMap.get(nodeInfoClass);
    }
}
