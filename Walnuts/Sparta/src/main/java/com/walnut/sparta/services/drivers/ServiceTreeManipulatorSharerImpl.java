package com.walnut.sparta.services.drivers;

import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.udtt.source.TriePathManipulator;
import com.pinecone.hydra.unit.udtt.source.TrieTreeManipulator;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;
import com.pinecone.hydra.service.ibatis.TrieTreeMapper;
import com.pinecone.hydra.service.ibatis.ServiceNodeOwnerMapper;
import com.pinecone.hydra.service.ibatis.ServicePathMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class ServiceTreeManipulatorSharerImpl implements TreeMasterManipulator {

    @Resource
    ServicePathMapper           scopePathManipulator;

    @Resource
    TrieTreeMapper trieTreeManipulator;

    @Resource
    ServiceNodeOwnerMapper      scopeOwnerManipulator;

    @Override
    public TireOwnerManipulator getTireOwnerManipulator() {
        return this.scopeOwnerManipulator;
    }

    @Override
    public TrieTreeManipulator getTrieTreeManipulator() {
        return this.trieTreeManipulator;
    }

    @Override
    public TriePathManipulator getTriePathManipulator() {
        return this.scopePathManipulator;
    }
}
