package com.pinecone.hydra.storage.volume.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.ulf.rdb.sqlite.SQLiteExecutor;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.hydra.storage.volume.entity.LogicVolume;
import com.pinecone.hydra.storage.volume.entity.StripedVolume;
import com.pinecone.hydra.storage.volume.entity.VolumeCapacity64;
import com.pinecone.hydra.storage.volume.entity.local.LocalStripedVolume;
import com.pinecone.hydra.storage.volume.source.StripedVolumeManipulator;
import com.pinecone.hydra.storage.volume.source.VolumeMasterManipulator;
import com.pinecone.hydra.unit.imperium.ImperialTreeNode;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

import java.sql.SQLException;
import java.util.List;

public class StripedVolumeOperator extends ArchVolumeOperator  implements VolumeOperator{
    protected StripedVolumeManipulator          stripedVolumeManipulator;

    public StripedVolumeOperator( VolumeOperatorFactory  factory ){
        this( factory.getMasterManipulator(), factory.getVolumeManager() );
        this.factory = factory;
    }

    public StripedVolumeOperator(VolumeMasterManipulator masterManipulator, VolumeManager volumeManager) {
        super(masterManipulator, volumeManager);
        this.stripedVolumeManipulator = masterManipulator.getStripedVolumeManipulator();
    }

    @Override
    public GUID insert(TreeNode treeNode) {
        LocalStripedVolume stripedVolume = ( LocalStripedVolume ) treeNode;
        ImperialTreeNode imperialTreeNode = this.affirmPreinsertionInitialize(stripedVolume);
        GUID guid = stripedVolume.getGuid();
        VolumeCapacity64 volumeCapacity = stripedVolume.getVolumeCapacity();
        if ( volumeCapacity.getVolumeGuid() == null ){
            volumeCapacity.setVolumeGuid( guid );
        }

        this.imperialTree.insert(imperialTreeNode);
        this.stripedVolumeManipulator.insert( stripedVolume );
        this.volumeCapacityManipulator.insert( volumeCapacity );
        return guid;
    }

    @Override
    public void purge(GUID guid) {
        List<GUIDImperialTrieNode> children = this.imperialTree.getChildren(guid);
        for( GUIDImperialTrieNode node : children ){
            TreeNode newInstance = (TreeNode)node.getType().newInstance( new Class<? >[]{this.getClass()}, this );
            VolumeOperator operator = this.factory.getOperator(this.getVolumeMetaType(newInstance));
            operator.purge( node.getGuid() );
        }
        this.removeNode( guid );
    }

    @Override
    public TreeNode get(GUID guid) {
        StripedVolume stripedVolume = this.stripedVolumeManipulator.getStripedVolume(guid);
        VolumeCapacity64 volumeCapacity = this.volumeCapacityManipulator.getVolumeCapacity(guid);
        stripedVolume.setVolumeCapacity( volumeCapacity );
        stripedVolume.setVolumeTree( this.volumeManager);
        stripedVolume.setKenVolumeFileSystem();
        return stripedVolume;
    }

    @Override
    public TreeNode get(GUID guid, int depth) {
        return null;
    }

    @Override
    public TreeNode getSelf(GUID guid) {
        return null;
    }

    @Override
    public void update(TreeNode treeNode) {

    }

    @Override
    public void updateName(GUID guid, String name) {

    }

    @Override
    public void removeStorageObject(GUID volumeGuid,GUID storageObjectGuid,long size) {
        LogicVolume logicVolume = this.volumeManager.get(volumeGuid);
        try {
            SQLiteExecutor sqLiteExecutor = logicVolume.getSQLiteExecutor();
            this.kenVolumeFileSystem.removeStripMetaTable( storageObjectGuid, sqLiteExecutor );
            List<TreeNode> children = this.volumeManager.getChildren(volumeGuid);
            for( TreeNode treeNode : children ){
                this.volumeManager.removeStorageObject( treeNode.getGuid(), storageObjectGuid, size );
            }
            logicVolume.increaseCapacity( size );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeNode(GUID guid ){
        this.imperialTree.purge( guid );
        this.imperialTree.removeCachePath( guid );
        this.stripedVolumeManipulator.remove( guid );
    }
}
