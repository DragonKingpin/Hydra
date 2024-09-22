package com.pinecone.hydra.scenario.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.scenario.ibatis.ScenarioNodeOwnerMapper;
import com.pinecone.hydra.scenario.ibatis.ScenarioNodePathMapper;
import com.pinecone.hydra.scenario.ibatis.ScenarioTreeMapper;
import com.pinecone.hydra.service.ibatis.ServiceNodeOwnerMapper;
import com.pinecone.hydra.service.ibatis.ServicePathMapper;
import com.pinecone.hydra.service.ibatis.ServiceTrieTreeMapper;
import com.pinecone.hydra.service.ibatis.hydranium.ServiceMasterTreeManipulatorImpl;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;
import com.pinecone.hydra.unit.udtt.source.TriePathManipulator;
import com.pinecone.hydra.unit.udtt.source.TrieTreeManipulator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
@Component
public class ScenarioMasterTreeManipulatorImpl implements TreeMasterManipulator {

    @Resource
    @Structure( type = ScenarioNodePathMapper.class )
    TriePathManipulator  triePathManipulator;

    @Resource
    @Structure( type = ScenarioNodeOwnerMapper.class )
    TireOwnerManipulator tireOwnerManipulator;

    @Resource
    @Structure( type = ScenarioTreeMapper.class )
    TrieTreeManipulator  trieTreeManipulator;

    public ScenarioMasterTreeManipulatorImpl() {

    }

    public ScenarioMasterTreeManipulatorImpl( KOIMappingDriver driver ) {
        driver.autoConstruct( ScenarioMasterTreeManipulatorImpl.class, Map.of(), this );
    }
    @Override
    public TireOwnerManipulator getTireOwnerManipulator() {
        return this.tireOwnerManipulator;
    }

    @Override
    public TrieTreeManipulator getTrieTreeManipulator() {
        return this.trieTreeManipulator;
    }

    @Override
    public TriePathManipulator getTriePathManipulator() {
        return this.triePathManipulator;
    }
}
