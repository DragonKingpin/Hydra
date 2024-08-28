package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GenericManipulatorFactory implements ManipulatorFactory {
    protected DefaultMetaNodeManipulator    defaultMetaNodeManipulator;

    protected ServiceFamilyTreeManipulator  serviceFamilyTreeManipulator;

    protected ServiceNodeManipulator        serviceNodeManipulator;

    protected ServiceMetaManipulator        serviceMetaManipulator;

    protected Map<String, Class<? > >       ManipulatorRegister = new HashMap<>();

    public GenericManipulatorFactory(DefaultMetaNodeManipulator defaultMetaNodeManipulator){
        this.defaultMetaNodeManipulator  =  defaultMetaNodeManipulator;
        serviceFamilyTreeManipulator     =  this.defaultMetaNodeManipulator.getServiceFamilyTreeManipulator();
        serviceNodeManipulator           =  this.defaultMetaNodeManipulator.getServiceNodeManipulator();
        serviceMetaManipulator           =  this.defaultMetaNodeManipulator.getServiceMetaManipulator();

        ManipulatorRegister.put(DefaultServiceMetaManipulator,this.serviceMetaManipulator.getClass());
        ManipulatorRegister.put(DefaultServiceNodeManipulator,this.serviceNodeManipulator.getClass());
        ManipulatorRegister.put(DefaultServiceFamilyTreeManipulator,this.serviceFamilyTreeManipulator.getClass());
    }


    @Override
    public Class<?> getUniformObjectManipulator(String loaderName) {
        return this.ManipulatorRegister.get(loaderName);
    }

    @Override
    public void register(String loaderName, Class<?> loader) {
        this.ManipulatorRegister.put(loaderName,loader);
    }

    @Override
    public void deregister(String loaderName) {
        this.ManipulatorRegister.remove(loaderName);
    }

    @Override
    public int size() {
        return ManipulatorRegister.size();
    }

    @Override
    public boolean isEmpty() {
        return ManipulatorRegister.isEmpty();
    }
}
