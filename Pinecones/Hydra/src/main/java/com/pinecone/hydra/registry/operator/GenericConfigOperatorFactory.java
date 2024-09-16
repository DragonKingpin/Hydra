package com.pinecone.hydra.registry.operator;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.registry.source.RegistryMasterManipulator;
import com.pinecone.hydra.unit.udtt.operator.TreeNodeOperator;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;

import java.util.HashMap;
import java.util.Map;

public class GenericConfigOperatorFactory implements ConfigOperatorFactory {

    protected RegistryMasterManipulator registryMasterManipulator;

    protected Map<String, TreeNodeOperator> registerer = new HashMap<>();

    public GenericConfigOperatorFactory( RegistryMasterManipulator registryMasterManipulator, TreeMasterManipulator masterManipulator ){
        this.registryMasterManipulator = registryMasterManipulator;
        this.registerer.put(
                ConfigOperatorFactory.DefaultConfigNodeKey,
                new ConfigNodeOperator( this.registryMasterManipulator, masterManipulator )
        );

        this.registerer.put(
                ConfigOperatorFactory.DefaultNamespaceNodeKey,
                new NamespaceNodeOperator( this.registryMasterManipulator, masterManipulator )
        );
    }

    @Override
    public void register( String typeName, TreeNodeOperator functionalNodeOperation ) {
        this.registerer.put( typeName, functionalNodeOperation );
    }

    @Override
    public TreeNodeOperator getOperator( String typeName ) {
        //Debug.trace( this.registerer.toString() );
        return this.registerer.get( typeName );
    }
}
