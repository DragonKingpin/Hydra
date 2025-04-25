package com.walnut.odin.mapper;

import com.pinecone.framework.system.construction.Structure;
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
public class KernelCategoryMasterTreeManipulatorImpl implements TreeMasterManipulator {
    @Resource
    @Structure( type = KernelCategoryNodePathCacheMapper.class )
    TriePathCacheManipulator triePathCacheManipulator;

    @Resource
    @Structure( type = KernelCategoryNodeOwnerMapper.class )
    TireOwnerManipulator tireOwnerManipulator;

    @Resource
    @Structure( type = KernelCategoryTreeMapper.class )
    TrieTreeManipulator  trieTreeManipulator;

    public KernelCategoryMasterTreeManipulatorImpl() {
    }
    public KernelCategoryMasterTreeManipulatorImpl( KOIMappingDriver driver ) {
        driver.autoConstruct( KernelCategoryMasterTreeManipulatorImpl.class, Map.of(), this );
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
