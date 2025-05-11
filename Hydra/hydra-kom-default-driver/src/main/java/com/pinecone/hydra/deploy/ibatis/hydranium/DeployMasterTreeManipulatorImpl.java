package com.pinecone.hydra.deploy.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.deploy.ibatis.DeployNodeOwnerMapper;
import com.pinecone.hydra.deploy.ibatis.DeployNodePathCacheMapper;
import com.pinecone.hydra.deploy.ibatis.DeployTreeMapper;
import com.pinecone.hydra.registry.ibatis.RegistryNodeOwnerMapper;
import com.pinecone.hydra.registry.ibatis.RegistryNodePathCacheMapper;
import com.pinecone.hydra.registry.ibatis.RegistryTreeMapper;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.unit.imperium.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.imperium.source.TreeMasterManipulator;
import com.pinecone.hydra.unit.imperium.source.TriePathCacheManipulator;
import com.pinecone.hydra.unit.imperium.source.TrieTreeManipulator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class DeployMasterTreeManipulatorImpl implements TreeMasterManipulator {

    @Resource
    @Structure( type = DeployNodePathCacheMapper.class )
    TriePathCacheManipulator triePathCacheManipulator;

    @Resource
    @Structure( type = DeployNodeOwnerMapper.class )
    TireOwnerManipulator tireOwnerManipulator;

    @Resource
    @Structure( type = DeployTreeMapper.class )
    TrieTreeManipulator  trieTreeManipulator;

    public DeployMasterTreeManipulatorImpl() {

    }

    public DeployMasterTreeManipulatorImpl(KOIMappingDriver driver ) {
        driver.autoConstruct( DeployMasterTreeManipulatorImpl.class, Map.of(), this );
    }

    @Override
    public TriePathCacheManipulator getTriePathCacheManipulator() {
        return this.triePathCacheManipulator;
    }

    @Override
    public TireOwnerManipulator getTireOwnerManipulator() {
        return this.tireOwnerManipulator;
    }

    @Override
    public TrieTreeManipulator getTrieTreeManipulator() {
        return this.trieTreeManipulator;
    }


}
