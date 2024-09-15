package com.walnut.sparta.services.nodes;

import com.pinecone.hydra.unit.udsn.source.ScopeOwnerManipulator;
import com.pinecone.hydra.unit.udsn.source.ScopePathManipulator;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import com.pinecone.hydra.unit.udsn.source.TreeManipulatorSharer;
import com.walnut.sparta.services.mapper.ScenarioNodeOwnerMapper;
import com.walnut.sparta.services.mapper.ScenarioNodePathMapper;
import com.walnut.sparta.services.mapper.ScenarioTreeMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
@Component
public class ScenarioTreeManipulatorSharerImpl implements TreeManipulatorSharer {

    @Resource
    ScenarioTreeMapper              scenarioTreeMapper;
    @Resource
    ScenarioNodeOwnerMapper         scenarioNodeOwnerMapper;
    @Resource
    ScenarioNodePathMapper          scenarioNodePathMapper;
    @Override
    public ScopeOwnerManipulator getScopeOwnerManipulator() {
        return this.scenarioNodeOwnerMapper;
    }

    @Override
    public ScopeTreeManipulator getScopeTreeManipulator() {
        return this.scenarioTreeMapper;
    }

    @Override
    public ScopePathManipulator getScopePathManipulator() {
        return this.scenarioNodePathMapper;
    }
}
