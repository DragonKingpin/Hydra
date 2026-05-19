package com.pinecone.hydra.storage.file.operator;

import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.ExternalSymbolic;
import com.pinecone.hydra.storage.file.entity.FileTreeNode;
import com.pinecone.hydra.storage.file.entity.GenericExternalSymbolic;
import com.pinecone.hydra.storage.file.source.ExternalSymbolicManipulator;
import com.pinecone.hydra.storage.file.source.FileMasterManipulator;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.ImperialTreeNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

public class GenericExternalSymbolicOperator extends ArchFileSystemOperator {
    private ExternalSymbolicManipulator externalSymbolicManipulator;

    public GenericExternalSymbolicOperator( FileSystemOperatorFactory factory ) {
        this( factory.getMasterManipulator(), (KOMFileSystem) factory.getFileSystem() );
        this.factory = factory;
    }

    public GenericExternalSymbolicOperator(FileMasterManipulator masterManipulator, KOMFileSystem fileSystem ) {
        super( masterManipulator, fileSystem );
        this.externalSymbolicManipulator = this.fileMasterManipulator.getExternalSymbolicManipulator();
    }
    @Override
    public GUID insert(TreeNode treeNode) {
        ExternalSymbolic externalSymbolic = (ExternalSymbolic) treeNode;
        ImperialTreeNode imperialTreeNode = this.affirmPreinsertionInitialize( treeNode );
        GUID guid = externalSymbolic.getGuid();

        imperialTreeNode.setBaseDataGUID(null);
        imperialTreeNode.setNodeMetadataGUID(null);
        this.imperialTree.insert(imperialTreeNode);
        this.externalSymbolicManipulator.insert( externalSymbolic );

        return guid;
    }

    @Override
    public void purge(GUID guid) {
        GUIDImperialTrieNode node = this.imperialTree.getNode(guid);
        this.imperialTree.purge( guid );
        this.externalSymbolicManipulator.remove( guid );
        this.imperialTree.removeCachePath(guid);
    }

    @Override
    public FileTreeNode get( GUID guid ) {
        return (ExternalSymbolic) this.getFileTreeNodeWideData( guid );
    }

    @Override
    public FileTreeNode get( GUID guid, int depth ) {
        return this.get( guid );
    }

    @Override
    public void rename(GUID fileGuid, String newName) {

    }

    @Override
    public FileTreeNode getAsRootDepth(GUID guid) {
        return this.getFileTreeNodeWideData(guid);
    }

    @Override
    public void update(TreeNode treeNode) {

    }

    @Override
    public void updateName(GUID guid, String name) {

    }

    protected FileTreeNode getFileTreeNodeWideData(GUID guid ){
        GUIDImperialTrieNode node = this.imperialTree.getNode( guid );
        ExternalSymbolic cn = this.externalSymbolicManipulator.getSymbolicByGuid( guid );
        if( cn instanceof GenericExternalSymbolic) {
            ((GenericExternalSymbolic) cn).apply( this.externalSymbolicManipulator );
        }

        return cn;
    }

    protected void inherit( FileTreeNode self, FileTreeNode prototype ){
        Class<? extends FileTreeNode> clazz = self.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for ( Field field : fields ){
            field.setAccessible(true);
            try {
                Object value1 = field.get( self );
                Object value2 = field.get( prototype );
                if ( Objects.isNull(value1) || (value1 instanceof List && ((List<?>) value1).isEmpty()) ){
                    field.set(self,value2);
                }
            }
            catch ( IllegalAccessException e ) {
                throw new ProxyProvokeHandleException(e);
            }
        }
    }
}
