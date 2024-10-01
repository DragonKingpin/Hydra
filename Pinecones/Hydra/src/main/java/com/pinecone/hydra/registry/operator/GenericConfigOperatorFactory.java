package com.pinecone.hydra.registry.operator;

import com.pinecone.hydra.registry.DistributedRegistry;
import com.pinecone.hydra.registry.entity.GenericNamespaceNode;
import com.pinecone.hydra.registry.entity.GenericProperties;
import com.pinecone.hydra.registry.entity.GenericTextConfigNode;
import com.pinecone.hydra.registry.source.RegistryMasterManipulator;
import com.pinecone.hydra.unit.udtt.operator.TreeNodeOperator;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class GenericConfigOperatorFactory implements ConfigOperatorFactory {

    protected RegistryMasterManipulator        registryMasterManipulator;

    protected DistributedRegistry              registry;

    protected Map<String, TreeNodeOperator>    registerer = new HashMap<>();

    protected Map<String, String >             metaTypeMap = new TreeMap<>();

    protected void registerDefaultMetaType( Class<?> genericType ) {
        this.metaTypeMap.put( genericType.getName(), genericType.getSimpleName().replace("Generic","") );
    }

    protected void registerDefaultMetaTypes() {
        this.registerDefaultMetaType( GenericNamespaceNode.class );
        this.registerDefaultMetaType( GenericProperties.class );
        this.registerDefaultMetaType( GenericTextConfigNode.class );
    }

    public GenericConfigOperatorFactory( DistributedRegistry registry, RegistryMasterManipulator registryMasterManipulator ){
        this.registry = registry;
        this.registryMasterManipulator = registryMasterManipulator;

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

        this.registerDefaultMetaTypes();
    }

    @Override
    public void register( String typeName, TreeNodeOperator functionalNodeOperation ) {
        this.registerer.put( typeName, functionalNodeOperation );
    }

    @Override
    public void registerMetaType( Class<?> clazz, String metaType ){
        this.registerMetaType( clazz.getName(), metaType );
    }

    @Override
    public void registerMetaType( String classFullName, String metaType ){
        this.metaTypeMap.put( classFullName, metaType );
    }

    @Override
    public String getMetaType( String classFullName ) {
        return this.metaTypeMap.get( classFullName );
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
