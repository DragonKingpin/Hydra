package com.pinecone.hydra.device.kom;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.entity.ContainerElement;
import com.pinecone.hydra.device.kom.entity.PhysicalHostElement;
import com.pinecone.hydra.device.kom.entity.QuickElement;
import com.pinecone.hydra.device.kom.entity.VirtualMachineElement;
import com.pinecone.hydra.system.ko.kom.ReparseKOMTree;
import com.pinecone.hydra.device.kom.entity.ElementNode;
import com.pinecone.hydra.device.kom.entity.ClusterElement;
import com.pinecone.hydra.device.kom.entity.Namespace;
import com.pinecone.hydra.device.kom.entity.DeviceElement;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public interface DeviceInstrument extends ReparseKOMTree {

    DeviceConfig KERNEL_DEVICE_CONFIG = new KernelDeviceConfig();

    ClusterElement          affirmCluster(String path );

    Namespace               affirmNamespace( String path );

    QuickElement            affirmQuick( String path );

    VirtualMachineElement   affirmVirtualMachine( String path );

    ContainerElement        affirmContainerElement(String path);

    PhysicalHostElement     affirmPhysicalHost(String path );

    ElementNode             queryElement( String path );

    boolean                 containsChild( GUID parentGuid, String childName );

    void                    update( TreeNode treeNode );
}
