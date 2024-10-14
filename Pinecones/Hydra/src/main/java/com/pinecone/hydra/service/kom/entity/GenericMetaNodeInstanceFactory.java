package com.pinecone.hydra.service.kom.entity;

import com.pinecone.hydra.service.kom.source.ServiceMasterManipulator;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;

import java.util.HashMap;
import java.util.Map;

public class GenericMetaNodeInstanceFactory implements MetaNodeInstanceFactory {
    private ServiceMasterManipulator serviceMasterManipulator;
    private Map<String, MetaNodeInstance>   nodeInstanceRegister = new HashMap<>();

    public GenericMetaNodeInstanceFactory(ServiceMasterManipulator serviceMasterManipulator, TreeMasterManipulator treeManipulatorSharer){
        this.serviceMasterManipulator = serviceMasterManipulator;
        this.nodeInstanceRegister.put(DefaultApplicationNode,new GenericApplicationInstance(this.serviceMasterManipulator,treeManipulatorSharer));
        this.nodeInstanceRegister.put(DefaultServiceNode,new GenericServiceInstance(this.serviceMasterManipulator,treeManipulatorSharer));
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
