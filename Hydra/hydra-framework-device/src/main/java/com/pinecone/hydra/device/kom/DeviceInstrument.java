package com.pinecone.hydra.device.kom;

import java.util.Collection;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.generic.GenericDeviceSchema;
import com.pinecone.hydra.device.generic.GenericDeviceSchemaDesigner;
import com.pinecone.hydra.device.generic.GenericDeviceType;
import com.pinecone.hydra.device.kom.entity.ContainerElement;
import com.pinecone.hydra.device.kom.entity.GenericDeviceElement;
import com.pinecone.hydra.device.kom.entity.PhysicalHostElement;
import com.pinecone.hydra.device.kom.entity.QuickElement;
import com.pinecone.hydra.device.kom.entity.VirtualMachineElement;
import com.pinecone.hydra.device.kom.instance.DeviceInstanceEntry;
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

    GenericDeviceElement    affirmGenericDevice( String path, String genericDevTypeCode, String schemaDataJson );

    GenericDeviceSchema     affirmGenericDeviceSchema( GenericDeviceSchema schema );

    GenericDeviceSchema     affirmGenericDeviceSchema( GenericDeviceSchema schema, GenericDeviceSchemaDesigner designer );

    GenericDeviceType       affirmGenericDeviceType( GenericDeviceType type );

    GenericDeviceElement    queryGenericDevice( GUID guid );

    Collection<GenericDeviceSchema> fetchGenericDeviceSchemas();

    Collection<GenericDeviceType> fetchGenericDeviceTypes();

    GenericDeviceType       queryGenericDeviceType( String code );

    GenericDeviceSchema     queryGenericDeviceSchema( GUID guid );

    GenericDeviceSchema     queryGenericDeviceSchemaByCode( String code );

    ElementNode             queryElement( String path );

    void                    createDeviceInstance( DeviceInstanceEntry deviceInstanceEntry );

    DeviceInstanceEntry     queryDeviceInstance( GUID instanceGuid );

    Collection<DeviceInstanceEntry> fetchDeviceInstances( DeviceInstanceQuery query );

    long                    countDeviceInstances( DeviceInstanceQuery query );

    Collection<DeviceInstanceEntry> fetchDeviceInstancesByDeviceGuid( GUID deviceGuid );

    DeviceInstancePage      fetchDeviceInstancePage( DeviceInstanceQuery query );

    void                    updateDeviceInstance( DeviceInstanceEntry deviceInstanceEntry );

    boolean                 containsChild( GUID parentGuid, String childName );

    void                    update( TreeNode treeNode );
}
