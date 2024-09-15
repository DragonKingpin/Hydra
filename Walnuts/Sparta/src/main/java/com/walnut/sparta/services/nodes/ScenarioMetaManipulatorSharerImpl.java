package com.walnut.sparta.services.nodes;

import com.pinecone.hydra.scenario.source.NamespaceNodeManipulator;
import com.pinecone.hydra.scenario.source.NamespaceNodeMetaManipulator;
import com.pinecone.hydra.scenario.source.ScenarioCommonDataManipulator;
import com.pinecone.hydra.scenario.source.ScenarioMetaManipulatorSharer;
import com.pinecone.hydra.unit.udsn.source.ScopePathManipulator;
import com.walnut.sparta.services.mapper.ScenarioNodePathMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
@Component
public class ScenarioMetaManipulatorSharerImpl implements ScenarioMetaManipulatorSharer {
    @Resource
    NamespaceNodeManipulator        namespaceNodeManipulator;
    @Resource
    NamespaceNodeMetaManipulator    namespaceNodeMetaManipulator;
    @Resource
    ScenarioCommonDataManipulator   scenarioCommonDataManipulator;
    @Override
    public NamespaceNodeManipulator getNamespaceNodeManipulator() {
        return this.namespaceNodeManipulator;
    }

    @Override
    public NamespaceNodeMetaManipulator getNamespaceNodeMetaManipulator() {
        return this.namespaceNodeMetaManipulator;
    }

    @Override
    public ScenarioCommonDataManipulator getScenarioCommonDataManipulator() {
        return this.scenarioCommonDataManipulator;
    }

}
