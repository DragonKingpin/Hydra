package com.pinecone.hydra.deploy.kom.operator;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.GenericClusterElement;
import com.pinecone.hydra.deploy.kom.entity.GenericNamespace;
import com.pinecone.hydra.deploy.kom.entity.GenericDeployElement;
import com.pinecone.hydra.deploy.kom.source.DeployMasterManipulator;
import com.pinecone.hydra.unit.imperium.operator.TreeNodeOperator;

public class GenericElementOperatorFactory implements ElementOperatorFactory {
    protected DeployMasterManipulator deployMasterManipulator;
    protected DeployInstrument deployInstrument;
    protected Map<String, TreeNodeOperator> registerer = new HashMap<>();

    protected Map<String, String >             metaTypeMap = new TreeMap<>();

    protected void registerDefaultMetaType( Class<?> genericType ) {
        this.metaTypeMap.put( genericType.getName(), genericType.getSimpleName().replace("Generic","") );
    }

    protected void registerDefaultMetaTypes() {
        this.registerDefaultMetaType( GenericNamespace.class );
        this.registerDefaultMetaType( GenericDeployElement.class );
        this.registerDefaultMetaType( GenericClusterElement.class );
    }

    public GenericElementOperatorFactory(DeployInstrument deployInstrument, DeployMasterManipulator deployMasterManipulator){
        this.deployInstrument = deployInstrument;
        this.deployMasterManipulator = deployMasterManipulator;

        this.registerer.put(
                ElementOperatorFactory.DefaultServiceNode,
                new DeployElementOperator( this )
        );

        this.registerer.put(
                ElementOperatorFactory.DefaultApplicationNode,
                new JobElementOperator(this)
        );

        this.registerer.put(
                ElementOperatorFactory.DefaultNamespace,
                new NamespaceOperator(this)
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
    public DeployInstrument getServicesTree() {
        return this.deployInstrument;
    }

    @Override
    public DeployMasterManipulator getTaskMasterManipulator() {
        return this.deployMasterManipulator;
    }

    @Override
    public String getMetaType( String classFullName ) {
        return this.metaTypeMap.get( classFullName );
    }

    @Override
    public ElementOperator getOperator(String typeName ) {
        //Debug.trace( this.registerer.toString() );
        return (ElementOperator) this.registerer.get( typeName );
    }

}
