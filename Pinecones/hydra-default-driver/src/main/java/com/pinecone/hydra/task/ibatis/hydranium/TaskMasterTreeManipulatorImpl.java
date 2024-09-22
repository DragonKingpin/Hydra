package com.pinecone.hydra.task.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.task.ibatis.TaskNodeOwnerMapper;
import com.pinecone.hydra.task.ibatis.TaskNodePathMapper;
import com.pinecone.hydra.task.ibatis.TaskTreeMapper;
import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;
import com.pinecone.hydra.unit.udtt.source.TriePathManipulator;
import com.pinecone.hydra.unit.udtt.source.TrieTreeManipulator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
@Component
public class TaskMasterTreeManipulatorImpl implements TreeMasterManipulator {
    @Resource
    @Structure( type = TaskNodePathMapper.class )
    TriePathManipulator  triePathManipulator;

    @Resource
    @Structure( type = TaskNodeOwnerMapper.class )
    TireOwnerManipulator tireOwnerManipulator;

    @Resource
    @Structure( type = TaskTreeMapper.class )
    TrieTreeManipulator  trieTreeManipulator;

    public TaskMasterTreeManipulatorImpl() {

    }

    public TaskMasterTreeManipulatorImpl( KOIMappingDriver driver ) {
        driver.autoConstruct( TaskMasterTreeManipulatorImpl.class, Map.of(), this );
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
