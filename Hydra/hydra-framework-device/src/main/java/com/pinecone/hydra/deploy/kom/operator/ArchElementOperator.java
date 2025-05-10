package com.pinecone.hydra.deploy.kom.operator;

import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.CommonMeta;
import com.pinecone.hydra.deploy.kom.entity.ElementNode;
import com.pinecone.hydra.deploy.kom.source.NodeMetaManipulator;
import com.pinecone.hydra.deploy.kom.source.TaskMasterManipulator;
import com.pinecone.hydra.unit.imperium.ImperialTree;

public abstract class ArchElementOperator implements ElementOperator {
    protected DeployInstrument deployInstrument;
    protected ImperialTree                  imperialTree;
    protected NodeMetaManipulator           nodeMetaManipulator;
    protected TaskMasterManipulator         taskMasterManipulator;
    protected ElementOperatorFactory        factory;

    public ArchElementOperator( ElementOperatorFactory factory ){
        this( factory.getTaskMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }
    public ArchElementOperator(TaskMasterManipulator masterManipulator, DeployInstrument deployInstrument){
        this.imperialTree = deployInstrument.getMasterTrieTree();
        this.deployInstrument = deployInstrument;
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
            ele.setExtraInformation         ( commonMeta.getExtraInformation()         );
            ele.setDescription              ( commonMeta.getDescription()              );
        }
    }
}
