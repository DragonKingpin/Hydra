package com.pinecone.hydra.layer.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.layer.ibatis.LayerCachePathMapper;
import com.pinecone.hydra.layer.ibatis.LayerOwnerMapper;
import com.pinecone.hydra.layer.ibatis.LayerTreeMapper;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.unit.imperium.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.imperium.source.TreeMasterManipulator;
import com.pinecone.hydra.unit.imperium.source.TriePathCacheManipulator;
import com.pinecone.hydra.unit.imperium.source.TrieTreeManipulator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class LayerMasterTreeManipulatorImpl implements TreeMasterManipulator {
    @Resource
    @Structure( type = LayerTreeMapper.class )
    TrieTreeManipulator trieTreeManipulator;

    @Resource
    @Structure( type = LayerCachePathMapper.class )
    TriePathCacheManipulator triePathCacheManipulator;

    @Resource
    @Structure( type = LayerOwnerMapper.class )
    TireOwnerManipulator tireOwnerManipulator;

    public LayerMasterTreeManipulatorImpl() {

    }

    public LayerMasterTreeManipulatorImpl(KOIMappingDriver driver ) {
        driver.autoConstruct( LayerMasterTreeManipulatorImpl.class, Map.of(), this );
    }

    @Override
    public TireOwnerManipulator getTireOwnerManipulator() {
        return this.tireOwnerManipulator;
    }

    @Override
    public TrieTreeManipulator getTrieTreeManipulator() {
        return this.trieTreeManipulator;
    }

    @Override
    public TriePathCacheManipulator getTriePathCacheManipulator() {
        return this.triePathCacheManipulator;
    }
}
