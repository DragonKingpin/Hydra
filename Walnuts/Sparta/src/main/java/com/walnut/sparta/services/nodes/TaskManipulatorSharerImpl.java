package com.walnut.sparta.services.nodes;

import com.pinecone.hydra.task.source.TaskCommonDataManipulator;
import com.pinecone.hydra.task.source.TaskManipulatorSharer;
import com.pinecone.hydra.task.source.TaskNodeManipulator;
import com.pinecone.hydra.task.source.TaskNodeMetaManipulator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
@Component
public class TaskManipulatorSharerImpl implements TaskManipulatorSharer {
    @Resource
    private TaskCommonDataManipulator   taskCommonDataManipulator;

    @Resource
    private TaskNodeMetaManipulator     taskNodeMetaManipulator;

    @Resource
    private TaskNodeManipulator         taskNodeManipulator;

    @Override
    public TaskNodeMetaManipulator getTaskNodeMetaManipulator() {
        return this.taskNodeMetaManipulator;
    }

    @Override
    public TaskNodeManipulator getTaskNodeManipulator() {
        return this.taskNodeManipulator;
    }

    @Override
    public TaskCommonDataManipulator getTaskCommonDataManipulator() {
        return this.taskCommonDataManipulator;
    }
}
