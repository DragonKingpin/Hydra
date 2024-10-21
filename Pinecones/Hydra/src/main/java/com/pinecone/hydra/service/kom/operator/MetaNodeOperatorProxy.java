package com.pinecone.hydra.service.kom.operator;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.kom.source.ServiceMasterManipulator;

import java.util.HashMap;
import java.util.Map;

public class MetaNodeOperatorProxy implements Pinenut {
    protected ServiceMasterManipulator serviceMasterManipulator;
    protected Map<String, MetaNodeOperator>  registerer = new HashMap<>();

    public MetaNodeOperatorProxy( ServiceMasterManipulator manipulators ){
        this.serviceMasterManipulator = manipulators;

        this.register( ServicesInstrument.DefaultMetaNodeApplication, new ApplicationNodeOperator( this.serviceMasterManipulator) );
        this.register( ServicesInstrument.DefaultMetaNodeClassification, new ClassificationNodeOperator( this.serviceMasterManipulator) );
        this.register( ServicesInstrument.DefaultMetaNodeService, new ServiceNodeOperator( this.serviceMasterManipulator) );
    }

    public void register( String typeName, MetaNodeOperator functionalNodeOperation ) {
        this.registerer.put( typeName, functionalNodeOperation );
    }

    public MetaNodeOperator getOperator(String typeName) {
        return this.registerer.get( typeName );
    }
}
