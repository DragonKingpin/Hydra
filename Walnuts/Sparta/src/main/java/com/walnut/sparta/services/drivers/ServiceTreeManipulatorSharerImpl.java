package com.walnut.sparta.services.drivers;

import com.pinecone.hydra.unit.udsn.source.ScopeOwnerManipulator;
import com.pinecone.hydra.unit.udsn.source.ScopePathManipulator;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import com.pinecone.hydra.unit.udsn.source.TreeMasterManipulator;
import com.pinecone.hydra.service.ibatis.ScopeTreeMapper;
import com.pinecone.hydra.service.ibatis.ServiceNodeOwnerMapper;
import com.pinecone.hydra.service.ibatis.ServicePathMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class ServiceTreeManipulatorSharerImpl implements TreeMasterManipulator {

    @Resource
    ServicePathMapper           scopePathManipulator;

    @Resource
    ScopeTreeMapper             scopeTreeManipulator;

    @Resource
    ServiceNodeOwnerMapper      scopeOwnerManipulator;

    @Override
    public ScopeOwnerManipulator getScopeOwnerManipulator() {
        return this.scopeOwnerManipulator;
    }

    @Override
    public ScopeTreeManipulator getScopeTreeManipulator() {
        return this.scopeTreeManipulator;
    }

    @Override
    public ScopePathManipulator getScopePathManipulator() {
        return this.scopePathManipulator;
    }
}
