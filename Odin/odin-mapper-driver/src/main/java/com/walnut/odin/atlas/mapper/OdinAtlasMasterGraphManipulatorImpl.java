package com.walnut.odin.atlas.mapper;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.unit.vgraph.source.AtlasMappingDriver;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphMasterManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphPathCacheManipulator;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class OdinAtlasMasterGraphManipulatorImpl implements VectorGraphMasterManipulator {

    @Resource
    @Structure( type = RuntimeVGraphMapper.class)
    VectorGraphManipulator mVectorGraphManipulator;

    @Resource
    @Structure( type = RuntimeVectorGraphPathCacheMapper.class)
    VectorGraphPathCacheManipulator mVectorGraphPathCacheManipulator;

    public OdinAtlasMasterGraphManipulatorImpl(){}

    public OdinAtlasMasterGraphManipulatorImpl(AtlasMappingDriver driver){
        driver.autoConstruct( OdinAtlasMasterGraphManipulatorImpl.class, Map.of(), this);
    }

    @Override
    public VectorGraphManipulator getVectorGraphManipulator() {
        return this.mVectorGraphManipulator;
    }

    @Override
    public VectorGraphPathCacheManipulator getVectorGraphPathCacheManipulator() {
        return this.mVectorGraphPathCacheManipulator;
    }
}
