package com.pinecone.hydra.atlas.runtime.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.atlas.source.RuntimeMasterManipulator;
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

    public RuntimeMasterManipulatorImpl() {}

    public RuntimeMasterManipulatorImpl( AtlasMappingDriver driver ) {
        driver.autoConstruct(RuntimeMasterManipulatorImpl.class, Map.of(),this);
        this.mVectorGraphMasterManipulator = new RuntimeMasterGraphManipulatorImpl(driver);
    }

    public VectorGraphMasterManipulator getVectorGraphMasterManipulator() {
        return this.mVectorGraphMasterManipulator;
    }
}
