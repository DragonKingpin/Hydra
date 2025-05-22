package com.pinecone.hydra.system.ko.control;

import com.pinecone.hydra.system.ko.KernelObject;

public interface ControlObject extends KernelObject {

    String FunctionName = ControlObject.class.getSimpleName().replace( "Object", "" );

    @Override
    default String getObjectFunctionName() {
        return FunctionName;
    }

}
