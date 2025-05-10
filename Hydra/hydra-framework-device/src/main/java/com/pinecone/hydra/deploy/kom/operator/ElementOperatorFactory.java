package com.pinecone.hydra.deploy.kom.operator;

import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.JobElement;
import com.pinecone.hydra.deploy.kom.entity.Namespace;
import com.pinecone.hydra.deploy.kom.entity.DeployElement;
import com.pinecone.hydra.deploy.kom.source.TaskMasterManipulator;
import com.pinecone.hydra.unit.imperium.operator.OperatorFactory;
import com.pinecone.hydra.unit.imperium.operator.TreeNodeOperator;

public interface ElementOperatorFactory extends OperatorFactory {
    String DefaultServiceNode     =  DeployElement.class.getSimpleName();
    String DefaultNamespace       =  Namespace.class.getSimpleName();
    String DefaultApplicationNode =  JobElement.class.getSimpleName();

    void register(String typeName, TreeNodeOperator functionalNodeOperation);

    void registerMetaType(Class<?> clazz, String metaType);

    void registerMetaType(String classFullName, String metaType);

    String getMetaType(String classFullName);

    ElementOperator getOperator(String typeName);

    DeployInstrument getServicesTree();

    TaskMasterManipulator getTaskMasterManipulator();

}
