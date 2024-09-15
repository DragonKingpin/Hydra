package com.walnut.sparta.services.drivers;

import com.pinecone.hydra.unit.udsn.source.ScopeOwnerManipulator;
import com.pinecone.hydra.unit.udsn.source.ScopePathManipulator;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import com.pinecone.hydra.unit.udsn.source.TreeMasterManipulator;
import com.pinecone.hydra.config.ibatis.ConfigNodeOwnerMapper;
import com.pinecone.hydra.config.ibatis.ConfigNodePathMapper;
import com.pinecone.hydra.config.ibatis.ConfigTreeMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ConfigMasterTreeManipulatorImpl implements TreeMasterManipulator {

    @Resource
    ConfigNodePathMapper confNodePathMapper;

    @Resource
    ConfigNodeOwnerMapper confNodeOwnerManipulator;

    @Resource
    ConfigTreeMapper scopeTreeManipulator;

    @Override
    public ScopeOwnerManipulator getScopeOwnerManipulator() {
        return this.confNodeOwnerManipulator;
    }

    @Override
    public ScopeTreeManipulator getScopeTreeManipulator() {
        return this.scopeTreeManipulator;
    }

    @Override
    public ScopePathManipulator getScopePathManipulator() {
        return this.confNodePathMapper;
    }
}
