package com.pinecone.hydra.system.ko.meta;

import com.pinecone.hydra.system.ko.KernelObject;
import com.pinecone.hydra.unit.imperium.entity.ElementumNode;

public interface ElementObject extends ElementumNode, KernelObject {

    String FunctionName = ElementObject.class.getSimpleName().replace( "Object", "" );

    @Override
    default String getObjectFunctionName() {
        return FunctionName;
    }

}
