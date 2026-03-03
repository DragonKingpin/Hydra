package com.pinecone.hydra.atlas.runtime.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.atlas.graph.source.QueueStratumManipulator;
import com.pinecone.hydra.atlas.graph.source.RuntimeMasterManipulator;
import com.pinecone.hydra.atlas.runtime.ibatis.QueueStratumMapper;
import com.pinecone.hydra.unit.vgraph.source.AtlasMappingDriver;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphMasterManipulator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class RuntimeMasterManipulatorImpl implements RuntimeMasterManipulator {
    @Resource
    @Structure( type = RuntimeMasterGraphManipulatorImpl.class )
    VectorGraphMasterManipulator mVectorGraphMasterManipulator;

    @Resource
    @Structure( type = QueueStratumMapper.class )
    QueueStratumManipulator mQueueStratumManipulator;

    public RuntimeMasterManipulatorImpl() {}

    public RuntimeMasterManipulatorImpl( AtlasMappingDriver driver ) {
        driver.autoConstruct(RuntimeMasterManipulatorImpl.class, Map.of(),this);
        this.mVectorGraphMasterManipulator = new RuntimeMasterGraphManipulatorImpl(driver);
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
