package com.pinecone.hydra.task.kom.operator;

import com.pinecone.hydra.task.kom.ServiceInstrument;
import com.pinecone.hydra.task.kom.entity.CommonMeta;
import com.pinecone.hydra.task.kom.entity.ElementNode;
import com.pinecone.hydra.task.kom.source.CommonDataManipulator;
import com.pinecone.hydra.task.kom.source.TaskMasterManipulator;
import com.pinecone.hydra.unit.imperium.ImperialTree;

public abstract class ArchElementOperator implements ElementOperator {
    protected ServiceInstrument serviceInstrument;
    protected ImperialTree                  imperialTree;
    protected CommonDataManipulator         commonDataManipulator;
    protected TaskMasterManipulator taskMasterManipulator;
    protected ElementOperatorFactory        factory;

    public ArchElementOperator( ElementOperatorFactory factory ){
        this( factory.getTaskMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }
    public ArchElementOperator(TaskMasterManipulator masterManipulator, ServiceInstrument serviceInstrument){
        this.imperialTree = serviceInstrument.getMasterTrieTree();
        this.serviceInstrument = serviceInstrument;
        this.commonDataManipulator    = masterManipulator.getCommonDataManipulator();
        this.taskMasterManipulator = masterManipulator;
        //this.factory = new GenericServiceOperatorFactory(servicesTree,masterManipulator);
    }

    public ElementOperatorFactory getOperatorFactory() {
        return this.factory;
    }

    protected void applyCommonMeta( ElementNode ele, CommonMeta commonMeta ){
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
