package com.pinecone.hydra.system.ko.action;

import com.pinecone.hydra.system.ko.KernelObject;

public interface ActionObject extends KernelObject {

    String FunctionName = ActionObject.class.getSimpleName().replace( "Object", "" );

    @Override
    default String objectFunctionName() {
        return FunctionName;
    }


}
