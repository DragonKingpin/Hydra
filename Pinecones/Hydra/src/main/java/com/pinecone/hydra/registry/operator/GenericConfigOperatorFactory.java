package com.pinecone.hydra.registry.operator;

import com.pinecone.hydra.registry.DistributedRegistry;
import com.pinecone.hydra.registry.entity.RegistryTreeNode;
import com.pinecone.hydra.registry.source.RegistryMasterManipulator;
import com.pinecone.hydra.unit.udtt.operator.TreeNodeOperator;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;

import java.util.HashMap;
import java.util.Map;

public class GenericConfigOperatorFactory implements ConfigOperatorFactory {

    protected RegistryMasterManipulator registryMasterManipulator;

    protected DistributedRegistry       registry;

    protected Map<String, TreeNodeOperator> registerer = new HashMap<>();

    public GenericConfigOperatorFactory( DistributedRegistry registry, RegistryMasterManipulator registryMasterManipulator ){
        this.registry = registry;
        this.registryMasterManipulator = registryMasterManipulator;
        this.registerer.put(
                ConfigOperatorFactory.DefaultConfigNodeKey,
                new ConfigNodeOperator( this )
        );

        this.registerer.put(
                ConfigOperatorFactory.DefaultNamespaceNodeKey,
                new NamespaceNodeOperator( this )
        );

        this.registerer.put(ConfigOperatorFactory.DefaultPropertyConfigNodeKey,
                new PropertyConfNodeOperator(this)
        );

        this.registerer.put(ConfigOperatorFactory.DefaultTextConfigNode,
                new TextValueNodeOperator(this)
        );
    }

    @Override
    public void register( String typeName, TreeNodeOperator functionalNodeOperation ) {
        this.registerer.put( typeName, functionalNodeOperation );
    }

    @Override
    public RegistryNodeOperator getOperator( String typeName ) {
        //Debug.trace( this.registerer.toString() );
        return (RegistryNodeOperator)this.registerer.get( typeName );
    }

    @Override
    public DistributedRegistry getRegistry() {
        return this.registry;
    }

    @Override
    public RegistryMasterManipulator getMasterManipulator() {
        return this.registryMasterManipulator;
    }
}
