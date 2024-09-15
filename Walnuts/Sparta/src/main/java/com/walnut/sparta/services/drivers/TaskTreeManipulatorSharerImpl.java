package com.walnut.sparta.services.drivers;

import com.pinecone.hydra.unit.udsn.source.ScopeOwnerManipulator;
import com.pinecone.hydra.unit.udsn.source.ScopePathManipulator;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import com.pinecone.hydra.unit.udsn.source.TreeMasterManipulator;
import com.pinecone.hydra.task.ibatis.TaskNodeOwnerMapper;
import com.pinecone.hydra.task.ibatis.TaskNodePathMapper;
import com.pinecone.hydra.task.ibatis.TaskTreeMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class TaskTreeManipulatorSharerImpl implements TreeMasterManipulator {
    @Resource
    private TaskTreeMapper          taskTreeMapper;
    @Resource
    private TaskNodePathMapper      taskNodePathMapper;
    @Resource
    TaskNodeOwnerMapper             taskNodeOwnerMapper;
    @Override
    public ScopeOwnerManipulator getScopeOwnerManipulator() {
        return this.taskNodeOwnerMapper;
    }

    @Override
    public ScopeTreeManipulator getScopeTreeManipulator() {
        return this.taskTreeMapper;
    }

    @Override
    public ScopePathManipulator getScopePathManipulator() {
        return this.taskNodePathMapper;
    }
}
