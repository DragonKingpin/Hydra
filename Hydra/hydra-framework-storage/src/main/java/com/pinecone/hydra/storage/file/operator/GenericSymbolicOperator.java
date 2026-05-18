package com.pinecone.hydra.storage.file.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FileTreeNode;
import com.pinecone.hydra.storage.file.entity.GenericSymbolic;
import com.pinecone.hydra.storage.file.entity.Symbolic;
import com.pinecone.hydra.storage.file.source.FileMasterManipulator;
import com.pinecone.hydra.storage.file.source.SymbolicManipulator;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.ImperialTreeNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class GenericSymbolicOperator extends ArchFileSystemOperator {
    private SymbolicManipulator symbolicManipulator;

    public GenericSymbolicOperator( FileSystemOperatorFactory factory ) {
        this( factory.getMasterManipulator(), (KOMFileSystem) factory.getFileSystem() );
        this.factory = factory;
    }

    public GenericSymbolicOperator( FileMasterManipulator masterManipulator, KOMFileSystem fileSystem ) {
        super( masterManipulator, fileSystem );
        this.symbolicManipulator = this.fileMasterManipulator.getSymbolicManipulator();
    }

    @Override
    public GUID insert( TreeNode treeNode ) {
        Symbolic symbolic = (Symbolic) treeNode;
        ImperialTreeNode imperialTreeNode = this.affirmPreinsertionInitialize( treeNode );
        GUID guid = symbolic.getGuid();

        imperialTreeNode.setBaseDataGUID(null);
        imperialTreeNode.setNodeMetadataGUID(null);
        this.imperialTree.insert(imperialTreeNode);
        this.symbolicManipulator.insert(symbolic);

        return guid;
    }

    @Override
    public void purge( GUID guid ) {
        this.imperialTree.purge(guid);
        this.symbolicManipulator.remove(guid);
        this.imperialTree.removeCachePath(guid);
    }

    @Override
    public FileTreeNode get( GUID guid ) {
        return (FileTreeNode) this.getFileTreeNodeWideData(guid);
    }

    @Override
    public FileTreeNode get( GUID guid, int depth ) {
        return this.get(guid);
    }

    @Override
    public void rename( GUID fileGuid, String newName ) {
    }

    @Override
    public FileTreeNode getAsRootDepth( GUID guid ) {
        return this.getFileTreeNodeWideData(guid);
    }

    @Override
    public void update( TreeNode treeNode ) {
    }

    @Override
    public void updateName( GUID guid, String name ) {
    }

    protected FileTreeNode getFileTreeNodeWideData( GUID guid ) {
        GUIDImperialTrieNode node = this.imperialTree.getNode(guid);
        Symbolic symbolic = this.symbolicManipulator.getSymbolicByGuid(guid);
        if ( symbolic instanceof GenericSymbolic ) {
            ((GenericSymbolic) symbolic).apply(this.symbolicManipulator);
        }
        return (FileTreeNode) symbolic;
    }
}
