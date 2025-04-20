package com.pinecone.hydra.layer.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerManipulator;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerMasterManipulator;
import com.pinecone.hydra.volume.ibatis.hydranium.VolumeMasterManipulatorImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class LayerMasterManipulatorImpl implements LayerMasterManipulator {
    @Resource
    @Structure( type = LayerMasterTreeManipulatorImpl.class )
    KOISkeletonMasterManipulator skeletonMasterManipulator;

    @Resource
    @Structure( type = LayerMasterTreeManipulatorImpl.class )
    LayerManipulator layerManipulator;

    public LayerMasterManipulatorImpl() {

    }

    public LayerMasterManipulatorImpl(KOIMappingDriver driver ) {
        driver.autoConstruct( VolumeMasterManipulatorImpl.class, Map.of(), this );
        this.skeletonMasterManipulator = new LayerMasterTreeManipulatorImpl( driver );
    }

    @Override
    public KOISkeletonMasterManipulator getSkeletonMasterManipulator() {
        return this.skeletonMasterManipulator;
    }

    @Override
    public LayerManipulator getLayerManipulator() {
        return null;
    }
}
