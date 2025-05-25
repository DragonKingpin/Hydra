package com.pinecone.hydra.deploy.kom.operator;

import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.ClusterElement;
import com.pinecone.hydra.deploy.kom.entity.ContainerElement;
import com.pinecone.hydra.deploy.kom.entity.Namespace;
import com.pinecone.hydra.deploy.kom.entity.DeployElement;
import com.pinecone.hydra.deploy.kom.entity.PhysicalHostElement;
import com.pinecone.hydra.deploy.kom.entity.QuickElement;
import com.pinecone.hydra.deploy.kom.entity.VirtualMachineElement;
import com.pinecone.hydra.deploy.kom.source.DeployMasterManipulator;
import com.pinecone.hydra.unit.imperium.operator.OperatorFactory;
import com.pinecone.hydra.unit.imperium.operator.TreeNodeOperator;

public interface ElementOperatorFactory extends OperatorFactory {
    String DefaultServiceNode     =  DeployElement.class.getSimpleName();
    String DefaultNamespace       =  Namespace.class.getSimpleName();
    String DefaultApplicationNode =  ClusterElement.class.getSimpleName();
    String DefaultVirtualMachine  =  VirtualMachineElement.class.getSimpleName();
    String DefaultPhysicalHost    =  PhysicalHostElement.class.getSimpleName();
    String DefaultQuickElement    =  QuickElement.class.getSimpleName();
    String DefaultContainerElement =  ContainerElement.class.getSimpleName();


    void register(String typeName, TreeNodeOperator functionalNodeOperation);

    void registerMetaType(Class<?> clazz, String metaType);

    void registerMetaType(String classFullName, String metaType);

    String getMetaType(String classFullName);

    ElementOperator getOperator(String typeName);

    DeployInstrument getServicesTree();

    DeployMasterManipulator getTaskMasterManipulator();

}
