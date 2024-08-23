package com.pinecone.hydra.service.tree;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.lang.DynamicFactory;
import com.pinecone.framework.util.lang.GenericDynamicFactory;
import com.pinecone.hydra.service.tree.FunctionalNodeInformation;
import com.pinecone.hydra.service.tree.FunctionalNodeOperation;

import java.util.HashMap;
import java.util.Map;

public class FunctionalNodeFactory {
    protected DynamicFactory genericDynamicFactory = new GenericDynamicFactory();
    protected Map<Class<? extends FunctionalNodeInformation>, FunctionalNodeOperation > operationMap = new HashMap<>();

    public void registration(Class<? extends FunctionalNodeInformation> nodeInfoClass, FunctionalNodeOperation functionalNodeOperation) {
        // 将 NodeInformation 的实现类和对应的 NodeOperation 存储到 HashMap 中
        this.operationMap.put(nodeInfoClass, functionalNodeOperation);
    }

    public FunctionalNodeOperation getNodeOperation(Class<?> FunctionalNodeOperation) {
        Debug.trace(this.operationMap.toString());
        Debug.trace(FunctionalNodeOperation);
        // 从 map 中获取对应的 NodeOperation 实例
        return this.operationMap.get(FunctionalNodeOperation);
    }
}
