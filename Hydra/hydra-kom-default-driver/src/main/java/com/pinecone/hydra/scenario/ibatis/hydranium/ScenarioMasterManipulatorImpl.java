package com.pinecone.hydra.scenario.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.scenario.ibatis.ScenarioCommonDataMapper;
import com.pinecone.hydra.scenario.ibatis.ScenarioNamespaceNodeMapper;
import com.pinecone.hydra.scenario.ibatis.ScenarioNamespaceNodeMetaMapper;
import com.pinecone.hydra.scenario.source.NamespaceNodeManipulator;
import com.pinecone.hydra.scenario.source.NamespaceNodeMetaManipulator;
import com.pinecone.hydra.scenario.source.ScenarioCommonDataManipulator;
import com.pinecone.hydra.scenario.source.ScenarioMasterManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
@Component
public class ScenarioMasterManipulatorImpl implements ScenarioMasterManipulator {
    @Resource
    @Structure(type = ScenarioNamespaceNodeMapper.class)
    NamespaceNodeManipulator namespaceNodeManipulator;

    @Resource
    @Structure(type = ScenarioNamespaceNodeMetaMapper.class)
    NamespaceNodeMetaManipulator namespaceNodeMetaManipulator;

    @Resource
    @Structure(type = ScenarioCommonDataMapper.class)
    ScenarioCommonDataManipulator scenarioCommonDataManipulator;

    @Resource
    @Structure(type = ScenarioMasterTreeManipulatorImpl.class)
    KOISkeletonMasterManipulator skeletonMasterManipulator;
    public ScenarioMasterManipulatorImpl() {

    }

    public ScenarioMasterManipulatorImpl( KOIMappingDriver driver ) {
        driver.autoConstruct( ScenarioMasterManipulatorImpl.class, Map.of(), this );
        this.skeletonMasterManipulator = new ScenarioMasterTreeManipulatorImpl( driver );
    }
    @Override
    public NamespaceNodeManipulator getNamespaceNodeManipulator() {
        return this.namespaceNodeManipulator;
    }

    @Override
    public NamespaceNodeMetaManipulator getNSNodeMetaManipulator() {
        return this.namespaceNodeMetaManipulator;
    }

    @Override
    public ScenarioCommonDataManipulator getScenarioCommonDataManipulator() {
        return this.scenarioCommonDataManipulator;
    }

    @Override
    public KOISkeletonMasterManipulator getSkeletonMasterManipulator() {
        return this.skeletonMasterManipulator;
    }
}
