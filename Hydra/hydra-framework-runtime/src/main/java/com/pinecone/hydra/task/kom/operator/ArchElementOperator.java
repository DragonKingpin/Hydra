package com.pinecone.hydra.task.kom.operator;

import com.pinecone.hydra.task.kom.TasksInstrument;
import com.pinecone.hydra.task.kom.entity.CommonMeta;
import com.pinecone.hydra.task.kom.entity.ElementNode;
import com.pinecone.hydra.task.kom.source.CommonDataManipulator;

import com.pinecone.hydra.task.kom.source.TaskMasterManipulator;
import com.pinecone.hydra.unit.imperium.ImperialTree;

public abstract class ArchElementOperator implements ElementOperator {
    protected TasksInstrument tasksInstrument;
    protected ImperialTree                  imperialTree;
    protected CommonDataManipulator commonDataManipulator;
    protected TaskMasterManipulator serviceMasterManipulator;
    protected ElementOperatorFactory        factory;

    public ArchElementOperator(ElementOperatorFactory factory ){
        this( factory.getTaskMasterManipulator(),factory.getTasksTree() );
        this.factory = factory;
    }
    public ArchElementOperator(TaskMasterManipulator masterManipulator, TasksInstrument tasksInstrument){
        this.imperialTree = tasksInstrument.getMasterTrieTree();
        this.tasksInstrument = tasksInstrument;
        this.commonDataManipulator    = masterManipulator.getCommonDataManipulator();
        this.serviceMasterManipulator = masterManipulator;
        //this.factory = new GenericServiceOperatorFactory(servicesTree,masterManipulator);
    }

    public ElementOperatorFactory getOperatorFactory() {
        return this.factory;
    }

    protected void applyCommonMeta(ElementNode ele, CommonMeta commonMeta ){
        if( commonMeta != null ) {
            ele.setGuid             ( commonMeta.getGuid()             );
            ele.setScenario         ( commonMeta.getScenario()         );
            ele.setPrimaryImplLang  ( commonMeta.getPrimaryImplLang()  );
            ele.setExtraInformation ( commonMeta.getExtraInformation() );
            ele.setLevel            ( commonMeta.getLevel()            );
            ele.setDescription      ( commonMeta.getDescription()      );
        }
    }
}
