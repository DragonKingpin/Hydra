package com.pinecone.hydra.file.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.file.KOMFileSystem;
import com.pinecone.hydra.file.entity.FileSystemAttributes;
import com.pinecone.hydra.file.entity.FileTreeNode;
import com.pinecone.hydra.file.entity.Folder;
import com.pinecone.hydra.file.entity.FolderMeta;
import com.pinecone.hydra.file.entity.GenericFolder;
import com.pinecone.hydra.file.source.FileMasterManipulator;
import com.pinecone.hydra.file.source.FolderManipulator;
import com.pinecone.hydra.file.source.FolderMetaManipulator;
import com.pinecone.hydra.registry.KOMRegistry;
import com.pinecone.hydra.registry.entity.GenericNamespace;
import com.pinecone.hydra.unit.udtt.DistributedTreeNode;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;
import com.pinecone.ulf.util.id.GuidAllocator;

import java.util.ArrayList;
import java.util.List;

public class GenericFolderOperator extends ArchFileSystemOperator{
    private FolderManipulator       folderManipulator;
    private FolderMetaManipulator   folderMetaManipulator;

    public GenericFolderOperator(FileSystemOperatorFactory factory ) {
        this( factory.getMasterManipulator(), (KOMFileSystem) factory.getFileSystem() );
        this.factory = factory;
    }

    public GenericFolderOperator(FileMasterManipulator masterManipulator, KOMFileSystem fileSystem ) {
        super( masterManipulator, fileSystem );
        this.folderManipulator      =   masterManipulator.getFolderManipulator();
        this.folderMetaManipulator  =   masterManipulator.getFolderMetaManipulator();
    }

    @Override
    public GUID insert(TreeNode treeNode) {
        Folder folder  = (Folder) treeNode;
        DistributedTreeNode distributedTreeNode = this.affirmPreinsertionInitialize(treeNode);
        GuidAllocator guidAllocator = this.fileSystem.getGuidAllocator();
        GUID guid = folder.getGuid();

        FileSystemAttributes attributes = folder.getAttributes();
        GUID attributesGuid = guidAllocator.nextGUID72();
        if ( attributes != null ){
            attributes.setGuid(attributesGuid);
            this.fileSystemAttributeManipulator.insert(attributes);
        }
        else {
            attributesGuid = null;
        }

        FolderMeta folderMeta = folder.getFolderMeta();
        GUID folderMetaGuid = guidAllocator.nextGUID72();
        if ( folderMeta != null ){
            folderMeta.setGuid(folderMetaGuid);
            this.folderMetaManipulator.insert(folderMeta);
        }
        else {
            folderMetaGuid = null;
        }

        distributedTreeNode.setNodeMetadataGUID(folderMetaGuid);
        distributedTreeNode.setBaseDataGUID(attributesGuid);
        this.distributedTrieTree.insert(distributedTreeNode);
        this.folderManipulator.insert(folder);
        return guid;
    }

    @Override
    public void purge(GUID guid) {
        //namespace节点需要递归删除其拥有节点若其引用节点，没有其他引用则进行清理
        List<GUIDDistributedTrieNode> childNodes = this.distributedTrieTree.getChildren(guid);
        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode(guid);
        if ( !childNodes.isEmpty() ){
            List<GUID > subordinates = this.distributedTrieTree.getSubordinates(guid);
            if ( !subordinates.isEmpty() ){
                for ( GUID subordinateGuid : subordinates ){
                    this.purge( subordinateGuid );
                }
            }
            childNodes = this.distributedTrieTree.getChildren( guid );
            for( GUIDDistributedTrieNode childNode : childNodes ){
                List<GUID > parentNodes = this.distributedTrieTree.getParentGuids(childNode.getGuid());
                if ( parentNodes.size() > 1 ){
                    this.distributedTrieTree.removeInheritance(childNode.getGuid(),guid);
                }
                else {
                    this.purge( childNode.getGuid() );
                }
            }
        }

        if ( node.getType().getObjectName().equals(GenericNamespace.class.getName()) ){
            this.removeNode(guid);
        }
        else {
            UOI uoi = node.getType();
            String metaType = this.getOperatorFactory().getMetaType( uoi.getObjectName() );
            if( metaType == null ) {
                TreeNode newInstance = (TreeNode)uoi.newInstance( new Class<? >[]{ KOMRegistry.class }, this.fileSystem );
                metaType = newInstance.getMetaType();
            }

            FileSystemOperator operator = this.getOperatorFactory().getOperator( metaType );
            operator.purge( guid );
        }
    }

    @Override
    public FileTreeNode get(GUID guid) {
        return this.getFolderWideData(guid,0);
    }

    @Override
    public FileTreeNode get(GUID guid, int depth) {
        return this.getFolderWideData(guid,depth);
    }

    @Override
    public FileTreeNode getSelf(GUID guid) {
        return this.getFolderWideData(guid,0);
    }

    @Override
    public void update(TreeNode treeNode) {

    }

    @Override
    public void updateName(GUID guid, String name) {

    }

    private Folder getFolderWideData(GUID guid, int depth ){
        Folder fd = this.folderManipulator.getFolderByGuid( guid );
        if ( fd instanceof GenericFolder){
            ((GenericFolder) fd).apply( this.fileSystem );
        }
        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode(guid);

        if( depth <= 0 ) {
            List<GUIDDistributedTrieNode> childNode = this.distributedTrieTree.getChildren(guid);
            ArrayList<GUID> guids = new ArrayList<>();
            for ( GUIDDistributedTrieNode n : childNode ){
                guids.add( n.getGuid() );
            }
            ++depth;
            fd.setChildrenGuids( guids, depth );
        }

        FileSystemAttributes attributes = this.fileSystemAttributeManipulator.getAttributes( guid, fd );
        FolderMeta folderMeta = this.folderMetaManipulator.getFolderMetaByGuid( node.getNodeMetadataGUID() );
        fd.setAttributes    ( attributes );
        fd.setFolderMeta ( folderMeta );
        return fd;
    }



    private void removeNode( GUID guid ){
        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode(guid);
        this.distributedTrieTree.purge( guid );
        this.distributedTrieTree.removeCachePath(guid);
        this.folderManipulator.remove(guid);
        this.folderMetaManipulator.remove(node.getNodeMetadataGUID());
        this.fileSystemAttributeManipulator.remove(node.getAttributesGUID());
    }
}
