package com.walnut.sparta.adapter;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.service.FunctionalNodeInformation;
import com.pinecone.hydra.service.FunctionalNodeOperation;

import java.util.HashMap;
import java.util.Map;

public class NodeAdapter {
    private static Map<Class<? extends FunctionalNodeInformation>, FunctionalNodeOperation> operationMap = new HashMap<>();

    public static void registration(Class<? extends FunctionalNodeInformation> nodeInfoClass, FunctionalNodeOperation functionalNodeOperation) {
        // 将 NodeInformation 的实现类和对应的 NodeOperation 存储到 HashMap 中
        operationMap.put(nodeInfoClass, functionalNodeOperation);
    }

    public static FunctionalNodeOperation getNodeOperation(Class<?> FunctionalNodeOperation) {
        Debug.trace(operationMap.toString());
        Debug.trace(FunctionalNodeOperation);
        // 从 map 中获取对应的 NodeOperation 实例
        return operationMap.get(FunctionalNodeOperation);
    }
}
