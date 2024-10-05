package com.walnut.sparta.services.drivers;

import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.udtt.source.TriePathCacheManipulator;
import com.pinecone.hydra.unit.udtt.source.TrieTreeManipulator;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;
import com.pinecone.hydra.task.ibatis.TaskNodeOwnerMapper;
import com.pinecone.hydra.task.ibatis.TaskNodePathCacheMapper;
import com.pinecone.hydra.task.ibatis.TaskTreeMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class TaskTreeManipulatorSharerImpl implements TreeMasterManipulator {
    @Resource
    private TaskTreeMapper          taskTreeMapper;
    @Resource
    private TaskNodePathCacheMapper taskNodePathMapper;
    @Resource
    TaskNodeOwnerMapper             taskNodeOwnerMapper;
    @Override
    public TireOwnerManipulator getTireOwnerManipulator() {
        return this.taskNodeOwnerMapper;
    }

    @Override
    public TrieTreeManipulator getTrieTreeManipulator() {
        return this.taskTreeMapper;
    }

    @Override
    public TriePathCacheManipulator getTriePathCacheManipulator() {
        return this.taskNodePathMapper;
    }
}
