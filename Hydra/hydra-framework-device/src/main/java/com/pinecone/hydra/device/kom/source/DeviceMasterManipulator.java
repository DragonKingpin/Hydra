package com.pinecone.hydra.device.kom.source;

import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.unit.imperium.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.imperium.source.TrieTreeManipulator;

public interface DeviceMasterManipulator extends KOIMasterManipulator {
    TrieTreeManipulator getTrieTreeManipulator() ;

    ClusterNodeManipulator getJobNodeManipulator();

    DeviceNamespaceManipulator getNamespaceManipulator();

    TireOwnerManipulator getTireOwnerManipulator();

    PhysicalHostManipulator getPhysicalHostManipulator();

    VirtualMachineManipulator getVirtualMachineManipulator();

    QuickElementManipulator getQuickElementManipulator();

    ContainerElementManipulator getContainerElementManipulator();

    GenericDeviceManipulator getGenericDeviceManipulator();

    GenericDeviceTypeManipulator getGenericDeviceTypeManipulator();

    GenericDeviceSchemaManipulator getGenericDeviceSchemaManipulator();

    DeviceInstanceManipulator getDeviceInstanceManipulator();
}
