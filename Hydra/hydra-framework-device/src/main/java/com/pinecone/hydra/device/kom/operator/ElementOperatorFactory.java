package com.pinecone.hydra.device.kom.operator;

import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.ClusterElement;
import com.pinecone.hydra.device.kom.entity.ContainerElement;
import com.pinecone.hydra.device.kom.entity.Namespace;
import com.pinecone.hydra.device.kom.entity.DeviceElement;
import com.pinecone.hydra.device.kom.entity.GenericDeviceElement;
import com.pinecone.hydra.device.kom.entity.PhysicalHostElement;
import com.pinecone.hydra.device.kom.entity.QuickElement;
import com.pinecone.hydra.device.kom.entity.VirtualMachineElement;
import com.pinecone.hydra.device.kom.source.DeviceMasterManipulator;
import com.pinecone.hydra.unit.imperium.operator.OperatorFactory;
import com.pinecone.hydra.unit.imperium.operator.TreeNodeOperator;

public interface ElementOperatorFactory extends OperatorFactory {
    String DefaultServiceNode     =  DeviceElement.class.getSimpleName();
    String DefaultNamespace       =  Namespace.class.getSimpleName();
    String DefaultApplicationNode =  ClusterElement.class.getSimpleName();
    String DefaultVirtualMachine  =  VirtualMachineElement.class.getSimpleName();
    String DefaultPhysicalHost    =  PhysicalHostElement.class.getSimpleName();
    String DefaultQuickElement    =  QuickElement.class.getSimpleName();
    String DefaultContainerElement =  ContainerElement.class.getSimpleName();
    String DefaultGenericDeviceElement = GenericDeviceElement.class.getSimpleName().replace( "Generic", "" );


    void register(String typeName, TreeNodeOperator functionalNodeOperation);

    void registerMetaType(Class<?> clazz, String metaType);

    void registerMetaType(String classFullName, String metaType);

    String getMetaType(String classFullName);

    ElementOperator getOperator(String typeName);

    DeviceInstrument getServicesTree();

    DeviceMasterManipulator getTaskMasterManipulator();

}
