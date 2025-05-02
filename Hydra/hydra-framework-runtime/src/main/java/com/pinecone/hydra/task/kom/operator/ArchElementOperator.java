package com.pinecone.hydra.task.kom.operator;

import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.CommonMeta;
import com.pinecone.hydra.task.kom.entity.ElementNode;
import com.pinecone.hydra.task.kom.source.NodeMetaManipulator;
import com.pinecone.hydra.task.kom.source.TaskMasterManipulator;
import com.pinecone.hydra.unit.imperium.ImperialTree;

public abstract class ArchElementOperator implements ElementOperator {
    protected TaskInstrument                taskInstrument;
    protected ImperialTree                  imperialTree;
    protected NodeMetaManipulator           nodeMetaManipulator;
    protected TaskMasterManipulator         taskMasterManipulator;
    protected ElementOperatorFactory        factory;

    public ArchElementOperator( ElementOperatorFactory factory ){
        this( factory.getTaskMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }
    public ArchElementOperator(TaskMasterManipulator masterManipulator, TaskInstrument taskInstrument){
        this.imperialTree = taskInstrument.getMasterTrieTree();
        this.taskInstrument = taskInstrument;
        this.nodeMetaManipulator = masterManipulator.getNodeMetaManipulator();
        this.taskMasterManipulator = masterManipulator;
        //this.factory = new GenericServiceOperatorFactory(servicesTree,masterManipulator);
    }

    public ElementOperatorFactory getOperatorFactory() {
        return this.factory;
    }

    protected void applyCommonMeta( ElementNode ele, CommonMeta commonMeta ){
        if( commonMeta != null ) {
            ele.setGuid                     ( commonMeta.getGuid()                     );
            ele.setScenario                 ( commonMeta.getScenario()                 );
            ele.setMarshallingArchitecture  ( commonMeta.getMarshallingArchitecture()  );
            ele.setExtraInformation         ( commonMeta.getExtraInformation()         );
            ele.setDescription              ( commonMeta.getDescription()              );
        }
    }
}
