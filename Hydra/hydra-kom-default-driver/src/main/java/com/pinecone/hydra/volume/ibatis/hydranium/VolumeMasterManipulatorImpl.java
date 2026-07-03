package com.pinecone.hydra.volume.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.storage.volume.source.VolumeEventManipulator;
import com.pinecone.hydra.storage.volume.source.VolumeExtentManipulator;
import com.pinecone.hydra.storage.volume.source.VolumeFreeIntentManipulator;
import com.pinecone.hydra.storage.volume.source.VolumeManipulator;
import com.pinecone.hydra.storage.volume.source.VolumeMasterManipulator;
import com.pinecone.hydra.storage.volume.source.VolumeMountManipulator;
import com.pinecone.hydra.storage.volume.source.VolumePhysicalManipulator;
import com.pinecone.hydra.storage.volume.source.VolumePhysicalSupportTraitManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.volume.ibatis.VolumeEventMapper;
import com.pinecone.hydra.volume.ibatis.VolumeExtentMapper;
import com.pinecone.hydra.volume.ibatis.VolumeFreeIntentMapper;
import com.pinecone.hydra.volume.ibatis.VolumeMapper;
import com.pinecone.hydra.volume.ibatis.VolumeMountMapper;
import com.pinecone.hydra.volume.ibatis.VolumePhysicalMapper;
import com.pinecone.hydra.volume.ibatis.VolumePhysicalSupportTraitMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class VolumeMasterManipulatorImpl implements VolumeMasterManipulator {
    @Resource
    @Structure( type = VolumeMapper.class )
    VolumeManipulator volumeManipulator;

    @Resource
    @Structure( type = VolumePhysicalMapper.class )
    VolumePhysicalManipulator physicalManipulator;

    @Resource
    @Structure( type = VolumePhysicalSupportTraitMapper.class )
    VolumePhysicalSupportTraitManipulator physicalSupportTraitManipulator;

    @Resource
    @Structure( type = VolumeExtentMapper.class )
    VolumeExtentManipulator extentManipulator;

    @Resource
    @Structure( type = VolumeMountMapper.class )
    VolumeMountManipulator mountManipulator;

    @Resource
    @Structure( type = VolumeEventMapper.class )
    VolumeEventManipulator eventManipulator;

    @Resource
    @Structure( type = VolumeFreeIntentMapper.class )
    VolumeFreeIntentManipulator freeIntentManipulator;

    public VolumeMasterManipulatorImpl() {

    }

    public VolumeMasterManipulatorImpl( KOIMappingDriver driver ) {
        driver.autoConstruct( VolumeMasterManipulatorImpl.class, Map.of(), this );
    }

    @Override
    public VolumeManipulator getVolumeManipulator() {
        return this.volumeManipulator;
    }

    @Override
    public VolumePhysicalManipulator getPhysicalManipulator() {
        return this.physicalManipulator;
    }

    @Override
    public VolumePhysicalSupportTraitManipulator getPhysicalSupportTraitManipulator() {
        return this.physicalSupportTraitManipulator;
    }

    @Override
    public VolumeExtentManipulator getExtentManipulator() {
        return this.extentManipulator;
    }

    @Override
    public VolumeMountManipulator getMountManipulator() {
        return this.mountManipulator;
    }

    @Override
    public VolumeEventManipulator getEventManipulator() {
        return this.eventManipulator;
    }

    @Override
    public VolumeFreeIntentManipulator getFreeIntentManipulator() {
        return this.freeIntentManipulator;
    }

    @Override
    public KOISkeletonMasterManipulator getSkeletonMasterManipulator() {
        return null;
    }
}
