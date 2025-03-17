package com.pinecone.hydra.service.kom.operator;

import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.kom.source.ServiceMasterManipulator;
import com.pinecone.hydra.service.kom.entity.GenericNamespace;
import com.pinecone.hydra.service.kom.entity.GenericApplicationElement;
import com.pinecone.hydra.service.kom.entity.GenericServiceElement;
import com.pinecone.hydra.unit.imperium.operator.TreeNodeOperator;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class GenericElementOperatorFactory implements ElementOperatorFactory {
    protected ServiceMasterManipulator      serviceMasterManipulator;
    protected ServiceInstrument serviceInstrument;
    protected Map<String, TreeNodeOperator> registerer = new HashMap<>();

    protected Map<String, String >             metaTypeMap = new TreeMap<>();

    protected void registerDefaultMetaType( Class<?> genericType ) {
        this.metaTypeMap.put( genericType.getName(), genericType.getSimpleName().replace("Generic","") );
    }

    protected void registerDefaultMetaTypes() {
        this.registerDefaultMetaType( GenericNamespace.class );
        this.registerDefaultMetaType( GenericServiceElement.class );
        this.registerDefaultMetaType( GenericApplicationElement.class );
    }

    public GenericElementOperatorFactory(ServiceInstrument serviceInstrument, ServiceMasterManipulator serviceMasterManipulator ){
        this.serviceInstrument = serviceInstrument;
        this.serviceMasterManipulator = serviceMasterManipulator;

        this.registerer.put(
                ElementOperatorFactory.DefaultServiceNode,
                new ServiceElementOperator( this )
        );

        this.registerer.put(
                ElementOperatorFactory.DefaultApplicationNode,
                new ApplicationElementOperator(this)
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
    public ServiceInstrument getServicesTree() {
        return this.serviceInstrument;
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
    public ElementOperator getOperator(String typeName ) {
        //Debug.trace( this.registerer.toString() );
        return (ElementOperator) this.registerer.get( typeName );
    }

}
