package com.walnut.sparta.services.nodes;

import com.pinecone.hydra.unit.udsn.source.ScopeOwnerManipulator;
import com.pinecone.hydra.unit.udsn.source.ScopePathManipulator;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import com.pinecone.hydra.unit.udsn.source.TreeManipulatorSharer;
import com.walnut.sparta.services.mapper.TaskNodeOwnerMapper;
import com.walnut.sparta.services.mapper.TaskNodePathMapper;
import com.walnut.sparta.services.mapper.TaskTreeMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class TaskTreeManipulatorSharerImpl implements TreeManipulatorSharer {
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
