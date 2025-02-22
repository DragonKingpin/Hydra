package com.pinecone.hydra.storage.volume.entity.local.physical.receive;

import com.pinecone.hydra.storage.io.Chanface;
import com.pinecone.hydra.storage.StorageIOResponse;
import com.pinecone.hydra.storage.StorageReceiveIORequest;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.hydra.storage.volume.entity.ArchReceiveEntity;
import com.pinecone.hydra.storage.volume.entity.local.striped.CacheBlock;

import java.io.IOException;

public class TitanDirectReceiveEntity64 extends ArchReceiveEntity implements DirectReceiveEntity64{
    protected String            destDirPath;

    protected DirectReceive64   directReceive;

    protected Chanface          chanface;

    public TitanDirectReceiveEntity64(VolumeManager volumeManager, StorageReceiveIORequest storageReceiveIORequest, Chanface channel, String destDirPath) {
        super(volumeManager, storageReceiveIORequest, channel);
        this.destDirPath = destDirPath;
        this.directReceive = new TitanDirectReceive64( this );
        this.chanface      = channel;
    }

    @Override
    public StorageIOResponse receive() throws IOException {
        return this.directReceive.receive( this.chanface );
    }

    @Override
    public StorageIOResponse receive(Number offset, Number endSize) throws IOException {
        return this.directReceive.receive( this.chanface, offset, endSize );
    }

    @Override
    public StorageIOResponse receive(CacheBlock cacheBlock, byte[] buffer) throws IOException {
        return this.directReceive.receive( this.chanface,cacheBlock, buffer );
    }

    @Override
    public StorageIOResponse randomReceive(Number offset, Number endSize) throws IOException {
        return this.directReceive.randomReceive( this.chanface, offset,endSize );
    }

    @Override
    public String getDestDirPath() {
        return this.destDirPath;
    }

    @Override
    public void setDestDirPath(String destDirPath) {
        this.destDirPath = destDirPath;
    }
}
