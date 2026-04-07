package com.pinecone.hydra.task.kom.operator;

import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.source.TaskMasterManipulator;
import com.pinecone.hydra.unit.imperium.ImperialTree;

public abstract class ArchElementOperator implements ElementOperator {
    protected TaskInstrument                taskInstrument;
    protected ImperialTree                  imperialTree;
    protected TaskMasterManipulator         taskMasterManipulator;
    protected ElementOperatorFactory        factory;

    public ArchElementOperator( ElementOperatorFactory factory ){
        this( factory.getTaskMasterManipulator(), factory.taskInstrument() );
        this.factory = factory;
    }
    public ArchElementOperator( TaskMasterManipulator masterManipulator, TaskInstrument taskInstrument){
        this.imperialTree = taskInstrument.getMasterTrieTree();
        this.taskInstrument = taskInstrument;
        this.taskMasterManipulator = masterManipulator;
        //this.factory = new GenericServiceOperatorFactory(servicesTree,masterManipulator);
    }

    public ElementOperatorFactory getOperatorFactory() {
        return this.factory;
    }


}
