package com.pinecone.hydra.storage.file.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.source.FolderManipulator;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;
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
        return this.fileSystem;
    }

    @Override
    public long TotalFolderSize() {
        long size = 0;
        List<TreeNode> children = this.fileSystem.getChildren(this.guid);
        for( TreeNode node : children ){
            if ( node instanceof Folder ){
                Folder folder = (Folder) node;
                size += folder.TotalFolderSize();
            }
            else if( node instanceof FileNode ){
                FileNode file = (FileNode) node;
                size += file.size();
            }
        }
        return size;
    }

    @Override
    public boolean containsKey(String key) {
        return false;
    }

    @Override
    public long size() {
        long size = 0;
        List<TreeNode> children = this.fileSystem.getChildren(this.guid);
        for( TreeNode node : children ){
            ElementNode elementNode = (ElementNode) node;
            size += elementNode.size();
        }
        return size;
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
