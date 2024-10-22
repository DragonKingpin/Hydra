package com.pinecone.hydra.storage.volume.entity;

import com.pinecone.hydra.storage.volume.VolumeTree;
import com.pinecone.hydra.storage.volume.entity.local.LocalPhysicalVolume;
import com.pinecone.hydra.storage.volume.entity.local.LocalSimpleVolume;
import com.pinecone.hydra.storage.volume.entity.local.LocalSpannedVolume;
import com.pinecone.hydra.storage.volume.entity.local.LocalStripedVolume;
import com.pinecone.hydra.storage.volume.entity.local.TitanLocalPhysicalVolume;
import com.pinecone.hydra.storage.volume.entity.local.TitanLocalSimpleVolume;
import com.pinecone.hydra.storage.volume.entity.local.TitanLocalSpannedVolume;
import com.pinecone.hydra.storage.volume.entity.local.TitanLocalStripedVolume;
import com.pinecone.hydra.storage.volume.source.PhysicalVolumeManipulator;
import com.pinecone.hydra.storage.volume.source.SimpleVolumeManipulator;
import com.pinecone.hydra.storage.volume.source.SpannedVolumeManipulator;
import com.pinecone.hydra.storage.volume.source.StripedVolumeManipulator;
import com.pinecone.hydra.storage.volume.source.VolumeCapacityManipulator;
import com.pinecone.hydra.storage.volume.source.VolumeMasterManipulator;

public class TitanVolumeAllotment implements VolumeAllotment{
    private VolumeTree              volumeTree;
    private VolumeMasterManipulator masterManipulator;

    public TitanVolumeAllotment( VolumeTree volumeTree, VolumeMasterManipulator volumeMasterManipulator ){
        this.volumeTree = volumeTree;
        this.masterManipulator= volumeMasterManipulator;
    }
    @Override
    public VolumeCapacity newVolumeCapacity() {
        return new TitanVolumeCapacity( this.volumeTree,this.masterManipulator.getVolumeCapacityManipulator() );
    }

    @Override
    public LocalStripedVolume newLocalStripedVolume() {
        return new TitanLocalStripedVolume( this.volumeTree, this.masterManipulator.getStripedVolumeManipulator() );
    }

    @Override
    public LocalSpannedVolume newLocalSpannedVolume() {
        return new TitanLocalSpannedVolume( this.volumeTree, this.masterManipulator.getSpannedVolumeManipulator() );
    }

    @Override
    public LocalSimpleVolume newLocalSimpleVolume() {
        return new TitanLocalSimpleVolume( this.volumeTree, this.masterManipulator.getSimpleVolumeManipulator() );
    }

    @Override
    public LocalPhysicalVolume newLocalPhysicalVolume() {
        return new TitanLocalPhysicalVolume( this.volumeTree, this.masterManipulator.getPhysicalVolumeManipulator() );
    }

    @Override
    public MountPoint newMountPoint() {
        return new TitanMountPoint( this.volumeTree, this.masterManipulator.getMountPointManipulator() );
    }
}
