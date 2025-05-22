package com.pinecone.hydra.system.ko.entity;

import com.pinecone.hydra.system.ko.handle.HandleObject;

public interface ObjectHandle extends HandleObject {
    String FunctionName = HandleObject.class.getSimpleName();

    @Override
    default String getObjectFunctionName() {
        return FunctionName;
    }
}
