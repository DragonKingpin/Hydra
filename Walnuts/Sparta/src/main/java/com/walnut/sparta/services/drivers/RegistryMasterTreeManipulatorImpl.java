package com.walnut.sparta.services.drivers;

import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.udtt.source.TriePathManipulator;
import com.pinecone.hydra.unit.udtt.source.TrieTreeManipulator;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;
import com.pinecone.hydra.registry.ibatis.RegistryNodeOwnerMapper;
import com.pinecone.hydra.registry.ibatis.RegistryNodePathMapper;
import com.pinecone.hydra.registry.ibatis.RegistryTreeMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RegistryMasterTreeManipulatorImpl implements TreeMasterManipulator {

    @Resource
    RegistryNodePathMapper configNodePathMapper;

    @Resource
    RegistryNodeOwnerMapper configNodeOwnerManipulator;

    @Resource
    RegistryTreeMapper trieTreeManipulator;

    @Override
    public TireOwnerManipulator getTireOwnerManipulator() {
        return this.configNodeOwnerManipulator;
    }

    @Override
    public TrieTreeManipulator getTrieTreeManipulator() {
        return this.trieTreeManipulator;
    }

    @Override
    public TriePathManipulator getTriePathManipulator() {
        return this.configNodePathMapper;
    }
}
