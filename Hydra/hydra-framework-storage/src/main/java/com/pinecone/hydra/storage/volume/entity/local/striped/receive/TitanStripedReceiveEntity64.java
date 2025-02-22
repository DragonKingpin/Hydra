package com.pinecone.hydra.storage.volume.entity.local.striped.receive;

import com.pinecone.hydra.storage.io.Chanface;
import com.pinecone.hydra.storage.StorageIOResponse;
import com.pinecone.hydra.storage.StorageReceiveIORequest;
import com.pinecone.hydra.storage.io.UIOException;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.hydra.storage.volume.entity.ArchReceiveEntity;
import com.pinecone.hydra.storage.volume.entity.StripedVolume;
import com.pinecone.hydra.storage.volume.entity.local.striped.CacheBlock;

import java.io.IOException;

public class TitanStripedReceiveEntity64 extends ArchReceiveEntity implements StripedReceiveEntity64{
    protected StripedVolume         stripedVolume;

    protected StripedReceive64      stripedReceive;

    protected Chanface              chanface;

    public TitanStripedReceiveEntity64(VolumeManager volumeManager, StorageReceiveIORequest storageReceiveIORequest, Chanface channel, StripedVolume stripedVolume) {
        super(volumeManager, storageReceiveIORequest, channel);
        this.stripedVolume  = stripedVolume;
        this.stripedReceive = new TitanStripedReceive64( this );
        this.chanface = channel;
    }

    @Override
    public StorageIOResponse receive() throws IOException {
        return this.stripedReceive.receive(this.chanface);
    }

    @Override
    public StorageIOResponse receive(Number offset, Number endSize) throws IOException {
        return this.stripedReceive.receive( this.chanface, offset, endSize );
    }

    @Override
    public StorageIOResponse randomReceive(Number offset, Number endSize) throws IOException {
        return null;
    }

    @Override
    public StorageIOResponse receive(CacheBlock cacheBlock, byte[] buffer) throws UIOException {
        return null;
    }

    @Override
    public StripedVolume getStripedVolume() {
        return this.stripedVolume;
    }
}
