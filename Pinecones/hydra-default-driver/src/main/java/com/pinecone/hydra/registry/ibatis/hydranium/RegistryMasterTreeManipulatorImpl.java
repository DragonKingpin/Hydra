package com.pinecone.hydra.registry.ibatis.hydranium;

import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.registry.ibatis.RegistryNodeOwnerMapper;
import com.pinecone.hydra.registry.ibatis.RegistryNodePathMapper;
import com.pinecone.hydra.registry.ibatis.RegistryTreeMapper;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.udtt.source.TriePathManipulator;
import com.pinecone.hydra.unit.udtt.source.TrieTreeManipulator;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;

@Component
public class RegistryMasterTreeManipulatorImpl implements TreeMasterManipulator {

    @Resource
    @Structure( type = RegistryNodePathMapper.class )
    TriePathManipulator  triePathManipulator;

    @Resource
    @Structure( type = RegistryNodeOwnerMapper.class )
    TireOwnerManipulator tireOwnerManipulator;

    @Resource
    @Structure( type = RegistryTreeMapper.class )
    TrieTreeManipulator  trieTreeManipulator;

    public RegistryMasterTreeManipulatorImpl() {

    }

    public RegistryMasterTreeManipulatorImpl( KOIMappingDriver driver ) {
        driver.autoConstruct( RegistryMasterTreeManipulatorImpl.class, Map.of(), this );
    }

    @Override
    public TriePathManipulator getTriePathManipulator() {
        return this.triePathManipulator;
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
