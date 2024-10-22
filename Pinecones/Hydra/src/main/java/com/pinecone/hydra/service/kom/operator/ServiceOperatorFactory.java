package com.pinecone.hydra.service.kom.operator;

import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.kom.nodes.ApplicationNode;
import com.pinecone.hydra.service.kom.nodes.Namespace;
import com.pinecone.hydra.service.kom.nodes.ServiceNode;
import com.pinecone.hydra.service.kom.source.ServiceMasterManipulator;
import com.pinecone.hydra.unit.udtt.operator.OperatorFactory;
import com.pinecone.hydra.unit.udtt.operator.TreeNodeOperator;

public interface ServiceOperatorFactory extends OperatorFactory {
    String DefaultServiceNode     =  ServiceNode.class.getSimpleName();
    String DefaultNamespace       =  Namespace.class.getSimpleName();
    String DefaultApplicationNode =  ApplicationNode.class.getSimpleName();

    void register( String typeName, TreeNodeOperator functionalNodeOperation );

    void registerMetaType( Class<?> clazz, String metaType );

    void registerMetaType( String classFullName, String metaType );

    String getMetaType( String classFullName );

    ServiceOperator getOperator(String typeName );

    ServicesInstrument getServicesTree();

    ServiceMasterManipulator getServiceMasterManipulator();

}
