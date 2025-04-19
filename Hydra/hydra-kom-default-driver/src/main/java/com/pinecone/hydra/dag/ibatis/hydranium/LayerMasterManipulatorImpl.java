package com.pinecone.hydra.dag.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerMasterManipulator;
import com.pinecone.hydra.volume.ibatis.hydranium.VolumeMasterManipulatorImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class LayerMasterManipulatorImpl implements LayerMasterManipulator {
    @Resource
    @Structure( type = DAGMasterTreeManipulatorImpl.class )
    KOISkeletonMasterManipulator skeletonMasterManipulator;

    public LayerMasterManipulatorImpl() {

    }

    public LayerMasterManipulatorImpl(KOIMappingDriver driver ) {
        driver.autoConstruct( VolumeMasterManipulatorImpl.class, Map.of(), this );
        this.skeletonMasterManipulator = new DAGMasterTreeManipulatorImpl( driver );
    }

    @Override
    public KOISkeletonMasterManipulator getSkeletonMasterManipulator() {
        return this.skeletonMasterManipulator;
    }
}
