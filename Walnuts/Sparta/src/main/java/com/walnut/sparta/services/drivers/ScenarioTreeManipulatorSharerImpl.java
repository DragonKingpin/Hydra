package com.walnut.sparta.services.drivers;

import com.pinecone.hydra.unit.udsn.source.ScopeOwnerManipulator;
import com.pinecone.hydra.unit.udsn.source.ScopePathManipulator;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import com.pinecone.hydra.unit.udsn.source.TreeMasterManipulator;
import com.pinecone.hydra.scenario.ibatis.ScenarioNodeOwnerMapper;
import com.pinecone.hydra.scenario.ibatis.ScenarioNodePathMapper;
import com.pinecone.hydra.scenario.ibatis.ScenarioTreeMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
@Component
public class ScenarioTreeManipulatorSharerImpl implements TreeMasterManipulator {

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
