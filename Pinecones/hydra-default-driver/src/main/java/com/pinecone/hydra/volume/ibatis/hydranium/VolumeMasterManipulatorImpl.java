package com.pinecone.hydra.volume.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.storage.volume.source.MirroredVolumeManipulator;
import com.pinecone.hydra.storage.volume.source.MountPointManipulator;
import com.pinecone.hydra.storage.volume.source.SimpleVolumeManipulator;
import com.pinecone.hydra.storage.volume.source.SpannedVolumeManipulator;
import com.pinecone.hydra.storage.volume.source.StripedVolumeManipulator;
import com.pinecone.hydra.storage.volume.source.VolumeCapacityManipulator;
import com.pinecone.hydra.storage.volume.source.VolumeMasterManipulator;

import javax.annotation.Resource;
import java.util.Map;

public class VolumeMasterManipulatorImpl implements VolumeMasterManipulator {
    @Resource
    @Structure( type = VolumeMasterTreeManipulatorImpl.class )
    KOISkeletonMasterManipulator skeletonMasterManipulator;

    @Resource
    @Structure( type = MirroredVolumeManipulator.class )
    MirroredVolumeManipulator mirroredVolumeManipulator;

    @Resource
    @Structure( type = MountPointManipulator.class )
    MountPointManipulator     mountPointManipulator;

    @Resource
    @Structure( type = SimpleVolumeManipulator.class )
    SimpleVolumeManipulator   simpleVolumeManipulator;

    @Resource
    @Structure( type = SpannedVolumeManipulator.class )
    SpannedVolumeManipulator  spannedVolumeManipulator;

    @Resource
    @Structure( type = StripedVolumeManipulator.class )
    StripedVolumeManipulator  stripedVolumeManipulator;

    @Resource
    @Structure( type = VolumeCapacityManipulator.class )
    VolumeCapacityManipulator volumeCapacityManipulator;

    public VolumeMasterManipulatorImpl() {

    }

    public VolumeMasterManipulatorImpl( KOIMappingDriver driver ) {
        driver.autoConstruct( VolumeMasterManipulatorImpl.class, Map.of(), this );
        this.skeletonMasterManipulator = new VolumeMasterTreeManipulatorImpl( driver );
    }
    @Override
    public KOISkeletonMasterManipulator getSkeletonMasterManipulator() {
        return this.skeletonMasterManipulator;
    }

    @Override
    public MirroredVolumeManipulator getMirroredVolumeManipulator() {
        return this.mirroredVolumeManipulator;
    }

    @Override
    public MountPointManipulator getMountPointManipulator() {
        return this.mountPointManipulator;
    }

    @Override
    public SimpleVolumeManipulator getSimpleVolumeManipulator() {
        return this.simpleVolumeManipulator;
    }

    @Override
    public SpannedVolumeManipulator getSpannedVolumeManipulator() {
        return this.spannedVolumeManipulator;
    }

    @Override
    public StripedVolumeManipulator getStripedVolumeManipulator() {
        return this.stripedVolumeManipulator;
    }

    @Override
    public VolumeCapacityManipulator getVolumeCapacityManipulator() {
        return this.volumeCapacityManipulator;
    }
}
