package com.walnut.odin.atlas.mapper;

import javax.annotation.Resource;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.unit.vgraph.source.AtlasMappingDriver;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphMasterManipulator;


@Component
public class OdinAtlasMasterManipulatorImpl implements RuntimeMasterManipulator {
    @Resource
    @Structure( type = OdinAtlasMasterGraphManipulatorImpl.class )
    VectorGraphMasterManipulator mVectorGraphMasterManipulator;

    @Resource
    @Structure( type = QueueStratumMapper.class )
    QueueStratumManipulator mQueueStratumManipulator;

    public OdinAtlasMasterManipulatorImpl() {}

    public OdinAtlasMasterManipulatorImpl(AtlasMappingDriver driver ) {
        driver.autoConstruct(OdinAtlasMasterManipulatorImpl.class, Map.of(),this);
        this.mVectorGraphMasterManipulator = new OdinAtlasMasterGraphManipulatorImpl(driver);
    }

    @Override
    public VectorGraphMasterManipulator getVectorGraphMasterManipulator() {
        return this.mVectorGraphMasterManipulator;
    }

    @Override
    public QueueStratumManipulator getQueueStratumManipulator() {
        return this.mQueueStratumManipulator;
    }
}
