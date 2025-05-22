package com.pinecone.hydra.proc.entity;

import com.pinecone.hydra.system.ko.meta.ElementObject;

public interface ElementNode extends ElementObject {

    @Override
    default String getObjectCategoryName() {
        return "Process";
    }

}
