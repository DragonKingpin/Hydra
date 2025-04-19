package com.pinecone.hydra.dag.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.dag.ibatis.DAGCachePathMapper;
import com.pinecone.hydra.dag.ibatis.DAGOwnerMapper;
import com.pinecone.hydra.dag.ibatis.DAGTreeMapper;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.unit.imperium.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.imperium.source.TreeMasterManipulator;
import com.pinecone.hydra.unit.imperium.source.TriePathCacheManipulator;
import com.pinecone.hydra.unit.imperium.source.TrieTreeManipulator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class DAGMasterTreeManipulatorImpl implements TreeMasterManipulator {
    @Resource
    @Structure( type = DAGTreeMapper.class )
    TrieTreeManipulator trieTreeManipulator;

    @Resource
    @Structure( type = DAGCachePathMapper.class )
    TriePathCacheManipulator triePathCacheManipulator;

    @Resource
    @Structure( type = DAGOwnerMapper.class )
    TireOwnerManipulator tireOwnerManipulator;

    public DAGMasterTreeManipulatorImpl() {

    }

    public DAGMasterTreeManipulatorImpl( KOIMappingDriver driver ) {
        driver.autoConstruct( DAGMasterTreeManipulatorImpl.class, Map.of(), this );
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
