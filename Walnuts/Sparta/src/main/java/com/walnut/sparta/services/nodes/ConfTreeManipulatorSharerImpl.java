package com.walnut.sparta.services.nodes;

import com.pinecone.hydra.config.distribute.source.ConfNodeOwnerManipulator;
import com.pinecone.hydra.config.distribute.source.ConfNodePathManipulator;
import com.pinecone.hydra.unit.udsn.source.ScopeOwnerManipulator;
import com.pinecone.hydra.unit.udsn.source.ScopePathManipulator;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import com.pinecone.hydra.unit.udsn.source.TreeManipulatorSharer;
import com.walnut.sparta.services.mapper.ConfNodeOwnerMapper;
import com.walnut.sparta.services.mapper.ConfNodePathMapper;
import com.walnut.sparta.services.mapper.ConfTreeMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
@Component
public class ConfTreeManipulatorSharerImpl implements TreeManipulatorSharer {

    @Resource
    ConfNodePathMapper              confNodePathMapper;

    @Resource
    ConfNodeOwnerMapper             confNodeOwnerManipulator;

    @Resource
    ConfTreeMapper                  scopeTreeManipulator;

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
