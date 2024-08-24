package com.pinecone.hydra.service.tree;

import com.pinecone.framework.util.lang.DynamicFactory;
import com.pinecone.framework.util.lang.GenericDynamicFactory;

import java.util.HashMap;
import java.util.Map;

public class MetaNodeOperatorProxy {
    protected DynamicFactory genericDynamicFactory;
    protected Map<String, MetaNodeOperator> operationMap = new HashMap<>();

    public MetaNodeOperatorProxy(){
        this.genericDynamicFactory = new GenericDynamicFactory();
    }

    public MetaNodeOperatorProxy(ClassLoader classLoader){
        this.genericDynamicFactory = new GenericDynamicFactory(classLoader);
    }

    public void registration(String FunctionNodePage, MetaNodeOperator functionalNodeOperation) {
        // 将 NodeInformation 的实现类和对应的 NodeOperation 存储到 HashMap 中
        this.operationMap.put(FunctionNodePage, functionalNodeOperation);
    }

    public MetaNodeOperator getNodeOperation(String FunctionalNodePage) {
        // 从 map 中获取对应的 NodeOperation 实例
        return this.operationMap.get(FunctionalNodePage);
    }
}
