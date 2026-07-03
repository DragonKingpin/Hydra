package com.pinecone.hydra.storage.file.delete;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class UofsDeletePlanItem implements Pinenut {
    protected final UofsDeleteTargetType targetType;
    protected final GUID guid;
    protected final GUID parentGuid;
    protected final String path;
    protected final FileNode fileNode;
    protected final TreeNode treeNode;
    protected final int depth;
    protected final boolean dangerous;

    protected UofsDeletePlanItem(
            UofsDeleteTargetType targetType,
            GUID guid,
            GUID parentGuid,
            String path,
            FileNode fileNode,
            TreeNode treeNode,
            int depth,
            boolean dangerous
    ) {
        this.targetType = targetType;
        this.guid = guid;
        this.parentGuid = parentGuid;
        this.path = path;
        this.fileNode = fileNode;
        this.treeNode = treeNode;
        this.depth = depth;
        this.dangerous = dangerous;
    }

    public static UofsDeletePlanItem pathCache( String path ) {
        return new UofsDeletePlanItem(
                UofsDeleteTargetType.PATH_CACHE,
                null,
                null,
                path,
                null,
                null,
                0,
                false
        );
    }

    public static UofsDeletePlanItem fileData( FileNode fileNode, int depth ) {
        return new UofsDeletePlanItem(
                UofsDeleteTargetType.FILE_DATA,
                fileNode == null ? null : fileNode.getGuid(),
                null,
                null,
                fileNode,
                fileNode,
                depth,
                false
        );
    }

    public static UofsDeletePlanItem fileMetadata( FileNode fileNode, int depth ) {
        return metadata( UofsDeleteTargetType.FILE_METADATA, fileNode, depth );
    }

    public static UofsDeletePlanItem folderMetadata( TreeNode node, int depth ) {
        return metadata( UofsDeleteTargetType.FOLDER_METADATA, node, depth );
    }

    public static UofsDeletePlanItem internalSymbolicMetadata( TreeNode node, int depth ) {
        return metadata( UofsDeleteTargetType.INTERNAL_SYMBOLIC_METADATA, node, depth );
    }

    public static UofsDeletePlanItem externalSymbolicMetadata( TreeNode node, int depth ) {
        return metadata( UofsDeleteTargetType.EXTERNAL_SYMBOLIC_METADATA, node, depth );
    }

    public static UofsDeletePlanItem nativeExternalTarget( String path, TreeNode node ) {
        return new UofsDeletePlanItem(
                UofsDeleteTargetType.NATIVE_EXTERNAL_TARGET,
                node == null ? null : node.getGuid(),
                null,
                path,
                null,
                node,
                0,
                true
        );
    }

    public static UofsDeletePlanItem hardlinkEdge( GUID parentGuid, GUID childGuid, int depth ) {
        return new UofsDeletePlanItem(
                UofsDeleteTargetType.HARDLINK_EDGE,
                childGuid,
                parentGuid,
                null,
                null,
                null,
                depth,
                false
        );
    }

    protected static UofsDeletePlanItem metadata( UofsDeleteTargetType targetType, TreeNode node, int depth ) {
        return new UofsDeletePlanItem(
                targetType,
                node == null ? null : node.getGuid(),
                null,
                null,
                null,
                node,
                depth,
                false
        );
    }

    public UofsDeleteTargetType getTargetType() {
        return this.targetType;
    }

    public GUID getGuid() {
        return this.guid;
    }

    public GUID getParentGuid() {
        return this.parentGuid;
    }

    public String getPath() {
        return this.path;
    }

    public FileNode getFileNode() {
        return this.fileNode;
    }

    public TreeNode getTreeNode() {
        return this.treeNode;
    }

    public int getDepth() {
        return this.depth;
    }

    public boolean getDangerous() {
        return this.dangerous;
    }
}
