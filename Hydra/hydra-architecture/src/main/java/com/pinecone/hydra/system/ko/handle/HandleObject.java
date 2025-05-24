package com.pinecone.hydra.system.ko.handle;

import com.pinecone.hydra.system.ko.KernelObject;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public interface HandleObject extends TreeNode, KernelObject {

    String FunctionName = HandleObject.class.getSimpleName().replace( "Object", "" );

    @Override
    default String objectFunctionName() {
        return FunctionName;
    }

    @Override
    default String objectCategoryName() {
        return "Handle";
    }

}
