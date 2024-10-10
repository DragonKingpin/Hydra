package com.pinecone.hydra.file.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.file.KOMFileSystem;
import com.pinecone.hydra.file.source.FolderManipulator;
import com.pinecone.hydra.registry.KOMRegistry;
import com.pinecone.hydra.registry.entity.Attributes;
import com.pinecone.ulf.util.id.GuidAllocator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GenericFolder extends ArchElementNode implements Folder{
    private FileSystemAttributes        attributes;
    private FolderMeta                  folderMeta;
    private KOMFileSystem               fileSystem;
    private FolderManipulator           folderManipulator;

    public GenericFolder() {
    }
    public GenericFolder(KOMFileSystem fileSystem ) {
        this.fileSystem = fileSystem;
        GuidAllocator guidAllocator = this.fileSystem.getGuidAllocator();
        this.setGuid( guidAllocator.nextGUID72() );
        this.setCreateTime( LocalDateTime.now() );
    }

    public GenericFolder( KOMFileSystem fileSystem, FolderManipulator folderManipulator ) {
        this(fileSystem);
        this.folderManipulator = folderManipulator;
    }


    public void apply(KOMFileSystem fileSystem ) {
        this.fileSystem = fileSystem;
    }

    @Override
    public KOMFileSystem parentFileSystem() {
        return this.fileSystem;
    }


    @Override
    public void setNodeAttribute(FileSystemAttributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public FolderMeta getFolderMeta() {
        return this.folderMeta;
    }

    @Override
    public void setFolderMeta(FolderMeta folderMeta) {
        this.folderMeta = folderMeta;
    }

    @Override
    public Map<String, FileTreeNode> getChildren() {
        return null;
    }

    @Override
    public List<GUID> getChildrenGuids() {
        return null;
    }

    @Override
    public void setChildrenGuids(List<GUID> contentGuids, int depth) {

    }

    @Override
    public List<FileTreeNode> listItem() {
        return null;
    }

    @Override
    public void put(String key, FileTreeNode val) {

    }

    @Override
    public void remove(String key) {

    }

    @Override
    public KOMFileSystem getFileTree() {
        return null;
    }

    @Override
    public boolean containsKey(String key) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Set<Map.Entry<String, FileTreeNode>> entrySet() {
        return null;
    }

    @Override
    public void copyTo(GUID destinationGuid) {

    }

    @Override
    public void copyNamespaceMetaTo(GUID destinationGuid) {

    }
}
