package com.pinecone.hydra.proc.image.kom;

import com.pinecone.hydra.system.ko.meta.ElementObject;

public interface ElementNode extends ElementObject {

    @Override
    default String objectCategoryName() {
        return "Process";
    }

}