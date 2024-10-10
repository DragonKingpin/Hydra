package com.pinecone.hydra.file.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.file.KOMFileSystem;
import com.pinecone.hydra.registry.entity.Attributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Folder extends ElementNode{
    void setNodeAttribute(FileSystemAttributes attributes);

    FolderMeta getFolderMeta();
    void setFolderMeta(FolderMeta folderMeta);

    Map<String, FileTreeNode> getChildren();

    List<GUID > getChildrenGuids();

    void setChildrenGuids( List<GUID> contentGuids, int depth );

    List<FileTreeNode > listItem();


    void put (String key, FileTreeNode val);

    void remove ( String key );

    KOMFileSystem getFileTree();

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
