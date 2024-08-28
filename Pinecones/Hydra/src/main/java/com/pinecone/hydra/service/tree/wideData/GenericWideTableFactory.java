package com.pinecone.hydra.service.tree.wideData;

import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulator;

import java.util.HashMap;
import java.util.Map;

public class GenericWideTableFactory implements WideTableFactory{
    private DefaultMetaNodeManipulator  defaultMetaNodeManipulator;
    private Map<String,NodeWideTable>   wideTableRegister = new HashMap<>();

    public GenericWideTableFactory(DefaultMetaNodeManipulator defaultMetaNodeManipulator){
        this.defaultMetaNodeManipulator = defaultMetaNodeManipulator;
        wideTableRegister.put(DefaultApplicationNode,new GenericApplicationWideTable(this.defaultMetaNodeManipulator));
        wideTableRegister.put(DefaultServiceNode,new GenericServiceWideTable(this.defaultMetaNodeManipulator));
    }

    @Override
    public NodeWideTable getUniformObjectWideTable(String loaderName) {
        return this.wideTableRegister.get(loaderName);
    }

    @Override
    public void register(String loaderName, NodeWideTable loader) {
        wideTableRegister.put(loaderName,loader);
    }

    @Override
    public void deregister(String loaderName) {
        wideTableRegister.remove(loaderName);
    }

    @Override
    public int size() {
        return wideTableRegister.size();
    }

    @Override
    public boolean isEmpty() {
        return wideTableRegister.isEmpty();
    }
}
