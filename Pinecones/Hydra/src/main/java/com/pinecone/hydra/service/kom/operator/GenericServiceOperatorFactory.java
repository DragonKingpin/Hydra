package com.pinecone.hydra.service.kom.operator;

import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.kom.source.ServiceMasterManipulator;
import com.pinecone.hydra.storage.file.entity.GenericFileNode;
import com.pinecone.hydra.storage.file.entity.GenericFolder;
import com.pinecone.hydra.unit.udtt.operator.TreeNodeOperator;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class GenericServiceOperatorFactory implements ServiceOperatorFactory{
    protected ServiceMasterManipulator      serviceMasterManipulator;
    protected ServicesInstrument servicesInstrument;
    protected Map<String, TreeNodeOperator> registerer = new HashMap<>();

    protected Map<String, String >             metaTypeMap = new TreeMap<>();
    protected void registerDefaultMetaType( Class<?> genericType ) {
        this.metaTypeMap.put( genericType.getName(), genericType.getSimpleName().replace("Generic","") );
    }

    protected void registerDefaultMetaTypes() {
        this.registerDefaultMetaType( GenericFolder.class );
        this.registerDefaultMetaType( GenericFileNode.class );
    }

    public GenericServiceOperatorFactory(ServicesInstrument servicesInstrument, ServiceMasterManipulator serviceMasterManipulator ){
        this.servicesInstrument = servicesInstrument;
        this.serviceMasterManipulator = serviceMasterManipulator;

        this.registerer.put(
                ServiceOperatorFactory.DefaultServiceNode,
                new ServiceNodeOperator( this )
        );

        this.registerer.put(
                ServiceOperatorFactory.DefaultApplicationNode,
                new ApplicationNodeOperator(this)
        );

        this.registerer.put(
                ServiceOperatorFactory.DefaultNamespace,
                new ServiceNamespaceOperator(this)
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
    public ServicesInstrument getServicesTree() {
        return this.servicesInstrument;
    }

    @Override
    public ServiceMasterManipulator getServiceMasterManipulator() {
        return this.serviceMasterManipulator;
    }

    @Override
    public String getMetaType( String classFullName ) {
        return this.metaTypeMap.get( classFullName );
    }

    @Override
    public ServiceOperator getOperator(String typeName ) {
        //Debug.trace( this.registerer.toString() );
        return (ServiceOperator) this.registerer.get( typeName );
    }

}
