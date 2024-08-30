package com.pinecone.hydra.service.tree.entity;

import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulator;

import java.util.HashMap;
import java.util.Map;

public class GenericMetaNodeInstanceFactory implements MetaNodeInstanceFactory {
    private DefaultMetaNodeManipulator      defaultMetaNodeManipulator;
    private Map<String, MetaNodeInstance>   nodeInstanceRegister = new HashMap<>();

    public GenericMetaNodeInstanceFactory(DefaultMetaNodeManipulator defaultMetaNodeManipulator){
        this.defaultMetaNodeManipulator = defaultMetaNodeManipulator;
        this.nodeInstanceRegister.put(DefaultApplicationNode,new GenericApplicationInstance(this.defaultMetaNodeManipulator));
        this.nodeInstanceRegister.put(DefaultServiceNode,new GenericServiceInstance(this.defaultMetaNodeManipulator));
    }

    @Override
    public MetaNodeInstance getUniformObjectWideTable(String loaderName) {
        return this.nodeInstanceRegister.get(loaderName);
    }

    @Override
    public void register(String loaderName, MetaNodeInstance loader) {
        this.nodeInstanceRegister.put(loaderName,loader);
    }

    @Override
    public void deregister(String loaderName) {
        this.nodeInstanceRegister.remove(loaderName);
    }

    @Override
    public int size() {
        return this.nodeInstanceRegister.size();
    }

    @Override
    public boolean isEmpty() {
        return this.nodeInstanceRegister.isEmpty();
    }
}
