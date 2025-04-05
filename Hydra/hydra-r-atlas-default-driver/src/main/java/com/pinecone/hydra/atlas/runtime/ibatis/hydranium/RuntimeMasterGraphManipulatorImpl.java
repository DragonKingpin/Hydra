package com.pinecone.hydra.atlas.runtime.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.atlas.runtime.ibatis.RuntimeVectorGraphMapper;
import com.pinecone.hydra.atlas.runtime.ibatis.RuntimeVectorGraphPathCacheMapper;
import com.pinecone.hydra.unit.vgraph.source.AtlasMappingDriver;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphMasterManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphPathCacheManipulator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class RuntimeMasterGraphManipulatorImpl implements VectorGraphMasterManipulator {

    @Resource
    @Structure( type = RuntimeVectorGraphMapper.class)
    VectorGraphManipulator mVectorGraphManipulator;

    @Resource
    @Structure( type = RuntimeVectorGraphPathCacheMapper.class)
    VectorGraphPathCacheManipulator mVectorGraphPathCacheManipulator;

    public RuntimeMasterGraphManipulatorImpl(){}

    public RuntimeMasterGraphManipulatorImpl(AtlasMappingDriver driver){
        driver.autoConstruct( RuntimeMasterGraphManipulatorImpl.class, Map.of(), this);
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
