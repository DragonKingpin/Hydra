package com.pinecone.hydra.file.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.file.DistributedFile;
import com.pinecone.hydra.registry.entity.Attributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Folder extends FileTreeNode{
    long getEnumId();

    void setEnumId(long enumId);

    GUID getGuid();

    void setGuid(GUID guid);

    String getName();

    void setName( String name );

    LocalDateTime getCreateTime();

    void setCreateTime( LocalDateTime createTime );

    LocalDateTime getUpdateTime();

    void setUpdateTime( LocalDateTime updateTime );

    Attributes getNodeAttribute(GUID guid);
    void setNodeAttribute(Attributes attributes);

    FolderMeta getFolderMeta(GUID guid);
    void setFolderMeta(FolderMeta folderMeta);

    Map<String, FileTreeNode> getChildren();

    List<GUID > getChildrenGuids();

    void setChildrenGuids( List<GUID> contentGuids, int depth );

    List<FileTreeNode > listItem();


    void put (String key, FileTreeNode val);

    void remove ( String key );

    DistributedFile getFileTree();

    boolean containsKey  ( String key );

    int size();

    boolean isEmpty();

    @Override
    default Folder evinceFolder() {
        return this;
    }

    Set<String > keySet();

    Set<Map.Entry<String,FileTreeNode>> entrySet();

    void copyTo(GUID destinationGuid);
    void copyNamespaceMetaTo(GUID destinationGuid);
}
