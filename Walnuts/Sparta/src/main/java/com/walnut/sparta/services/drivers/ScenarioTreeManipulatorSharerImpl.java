package com.walnut.sparta.services.drivers;

import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.udtt.source.TriePathManipulator;
import com.pinecone.hydra.unit.udtt.source.TrieTreeManipulator;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;
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
    public TireOwnerManipulator getTireOwnerManipulator() {
        return this.scenarioNodeOwnerMapper;
    }

    @Override
    public TrieTreeManipulator getTrieTreeManipulator() {
        return this.scenarioTreeMapper;
    }

    @Override
    public TriePathManipulator getTriePathManipulator() {
        return this.scenarioNodePathMapper;
    }
}
