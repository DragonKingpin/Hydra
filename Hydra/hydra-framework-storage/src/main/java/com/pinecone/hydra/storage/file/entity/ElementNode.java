package com.pinecone.hydra.storage.file.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.system.ko.meta.ElementObject;

import java.time.LocalDateTime;

public interface ElementNode extends FileTreeNode, ElementObject {

    long getEnumId();

    GUID getGuid();
    void setGuid(GUID guid);

    LocalDateTime getCreateTime();

    LocalDateTime getUpdateTime();

    String getName();
    void setName(String name);

    FileSystemAttributes getAttributes();
    void setAttributes( FileSystemAttributes attributes );

    KOMFileSystem parentFileSystem();

    @Override
    default String objectCategoryName() {
        return "Storage";
    }

}
