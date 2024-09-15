package com.walnut.sparta.services.nodes;

import com.pinecone.hydra.unit.udsn.source.ScopeOwnerManipulator;
import com.pinecone.hydra.unit.udsn.source.ScopePathManipulator;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import com.pinecone.hydra.unit.udsn.source.TreeManipulatorSharer;
import com.walnut.sparta.services.mapper.ScopeTreeMapper;
import com.walnut.sparta.services.mapper.ServiceNodeOwnerMapper;
import com.walnut.sparta.services.mapper.ServicePathMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class ServiceTreeManipulatorSharerImpl implements TreeManipulatorSharer {

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
