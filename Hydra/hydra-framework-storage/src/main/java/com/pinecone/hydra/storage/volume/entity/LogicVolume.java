package com.pinecone.hydra.storage.volume.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.ulf.rdb.sqlite.SQLiteExecutor;
import com.pinecone.hydra.storage.StorageIOResponse;
import com.pinecone.hydra.storage.io.UIOException;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.hydra.storage.volume.entity.local.striped.CacheBlock;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface LogicVolume extends Volume, TreeNode {
    String getName();

    void setName( String name );

    List<LogicVolume> queryChildren();

    void setChildren( List<LogicVolume> children );

    VolumeCapacity64 getVolumeCapacity();

    void setVolumeCapacity( VolumeCapacity64 volumeCapacity );

    void extendLogicalVolume( GUID physicalGuid );
    List< GUID > listPhysicalVolume();

    default MirroredVolume evinceMirroredVolume(){
        return null;
    }
    default SimpleVolume   evinceSimpleVolume(){
        return null;
    }
    default SpannedVolume  evinceSpannedVolume(){
        return null;
    }
    default StripedVolume  evinceStripeVolume(){
        return null;
    }
    void setVolumeTree( VolumeManager volumeManager);


    StorageIOResponse receive( ReceiveEntity entity ) throws IOException;
    StorageIOResponse receive( ReceiveEntity entity, Number offset, Number endSize ) throws IOException;
    StorageIOResponse randomReceive( ReceiveEntity entity, Number offset, Number endSize ) throws IOException;
    StorageIOResponse receive( ReceiveEntity entity, CacheBlock cacheBlock, byte[] buffer ) throws IOException;

    StorageIOResponse export( ExporterEntity entity ) throws IOException;
    //敬请期待
    StorageIOResponse export( ExporterEntity entity, Number offset, Number endSize ) throws IOException;
    StorageIOResponse export( ExporterEntity entity, CacheBlock cacheBlock, Number offset, Number endSize, byte[] buffer ) throws UIOException;

    StorageIOResponse export( ExporterEntity entity, boolean accessRandom ) throws UIOException;
    //敬请期待
    StorageIOResponse export( ExporterEntity entity, Number offset, Number endSize, boolean accessRandom );
    StorageIOResponse export( ExporterEntity entity, CacheBlock cacheBlock, Number offset, Number endSize, byte[] buffer, boolean accessRandom ) throws UIOException;


    boolean existStorageObject( GUID storageObject ) throws SQLException;

    void build() throws SQLException;

    void storageExpansion( GUID volumeGuid );

    SQLiteExecutor getSQLiteExecutor() throws SQLException;
}
