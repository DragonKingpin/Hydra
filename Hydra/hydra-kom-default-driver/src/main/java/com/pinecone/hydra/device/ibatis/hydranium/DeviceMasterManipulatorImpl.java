package com.pinecone.hydra.device.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.device.ibatis.ContainerElementMapper;
import com.pinecone.hydra.device.ibatis.DeviceNamespaceMapper;
import com.pinecone.hydra.device.ibatis.DeviceInstanceMapper;
import com.pinecone.hydra.device.ibatis.DeviceNodeOwnerMapper;
import com.pinecone.hydra.device.ibatis.DeviceTreeMapper;
import com.pinecone.hydra.device.ibatis.GenericDeviceMapper;
import com.pinecone.hydra.device.ibatis.GenericDeviceSchemaMapper;
import com.pinecone.hydra.device.ibatis.GenericDeviceTypeMapper;
import com.pinecone.hydra.device.ibatis.ClusterNodeMapper;
import com.pinecone.hydra.device.ibatis.PhysicalHostMapper;
import com.pinecone.hydra.device.ibatis.QuickElementMapper;
import com.pinecone.hydra.device.ibatis.VirtualMachineMapper;
import com.pinecone.hydra.device.kom.source.ContainerElementManipulator;
import com.pinecone.hydra.device.kom.source.DeviceMasterManipulator;
import com.pinecone.hydra.device.kom.source.DeviceNamespaceManipulator;
import com.pinecone.hydra.device.kom.source.DeviceInstanceManipulator;
import com.pinecone.hydra.device.kom.source.GenericDeviceManipulator;
import com.pinecone.hydra.device.kom.source.GenericDeviceSchemaManipulator;
import com.pinecone.hydra.device.kom.source.GenericDeviceTypeManipulator;
import com.pinecone.hydra.device.kom.source.PhysicalHostManipulator;
import com.pinecone.hydra.device.kom.source.QuickElementManipulator;
import com.pinecone.hydra.device.kom.source.VirtualMachineManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.device.kom.source.ClusterNodeManipulator;
import com.pinecone.hydra.unit.imperium.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.imperium.source.TrieTreeManipulator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class DeviceMasterManipulatorImpl implements DeviceMasterManipulator {
    @Resource
    @Structure( type = DeviceNodeOwnerMapper.class )
    TireOwnerManipulator tireOwnerManipulator;

    @Resource
    @Structure(type = DeviceTreeMapper.class )
    TrieTreeManipulator trieTreeManipulator;

    @Resource
    @Structure(type = ClusterNodeMapper.class )
    ClusterNodeManipulator jobNodeManipulator;

    @Resource
    @Structure( type = DeviceNamespaceMapper.class )
    DeviceNamespaceManipulator deviceNamespaceManipulator;

    @Resource
    @Structure( type = PhysicalHostMapper.class )
    PhysicalHostManipulator physicalHostManipulator;

    @Resource
    @Structure( type = VirtualMachineMapper.class )
    VirtualMachineManipulator virtualMachineManipulator;

    @Resource
    @Structure( type = QuickElementMapper.class )
    QuickElementManipulator quickElementManipulator;


    @Resource
    @Structure( type = ContainerElementMapper.class )
    ContainerElementManipulator containerElementManipulator;

    @Resource
    @Structure( type = GenericDeviceMapper.class )
    GenericDeviceManipulator genericDeviceManipulator;

    @Resource
    @Structure( type = GenericDeviceTypeMapper.class )
    GenericDeviceTypeManipulator genericDeviceTypeManipulator;

    @Resource
    @Structure( type = GenericDeviceSchemaMapper.class )
    GenericDeviceSchemaManipulator genericDeviceSchemaManipulator;

    @Resource
    @Structure( type = DeviceInstanceMapper.class )
    DeviceInstanceManipulator deviceInstanceManipulator;

    @Resource( type = DeviceMasterTreeManipulatorImpl.class )
    KOISkeletonMasterManipulator skeletonMasterManipulator;

    public DeviceMasterManipulatorImpl() {

    }

    public DeviceMasterManipulatorImpl(KOIMappingDriver driver ) {
        driver.autoConstruct( DeviceMasterManipulatorImpl.class, Map.of(), this );
        this.skeletonMasterManipulator = new DeviceMasterTreeManipulatorImpl( driver );
    }


    @Override
    public KOISkeletonMasterManipulator getSkeletonMasterManipulator() {
        return this.skeletonMasterManipulator;
    }


    @Override
    public TrieTreeManipulator getTrieTreeManipulator() {
        return this.trieTreeManipulator;
    }

    @Override
    public ClusterNodeManipulator getJobNodeManipulator() {
        return this.jobNodeManipulator;
    }

    @Override
    public DeviceNamespaceManipulator getNamespaceManipulator() {
        return this.deviceNamespaceManipulator;
    }

    @Override
    public TireOwnerManipulator getTireOwnerManipulator() {
        return this.tireOwnerManipulator;
    }

    @Override
    public PhysicalHostManipulator getPhysicalHostManipulator() {
        return this.physicalHostManipulator;
    }

    @Override
    public VirtualMachineManipulator getVirtualMachineManipulator() {
        return this.virtualMachineManipulator;
    }

    @Override
    public QuickElementManipulator getQuickElementManipulator() {
        return this.quickElementManipulator;
    }

    @Override
    public ContainerElementManipulator getContainerElementManipulator() {
        return this.containerElementManipulator;
    }

    @Override
    public GenericDeviceManipulator getGenericDeviceManipulator() {
        return this.genericDeviceManipulator;
    }

    @Override
    public GenericDeviceTypeManipulator getGenericDeviceTypeManipulator() {
        return this.genericDeviceTypeManipulator;
    }

    @Override
    public GenericDeviceSchemaManipulator getGenericDeviceSchemaManipulator() {
        return this.genericDeviceSchemaManipulator;
    }

    @Override
    public DeviceInstanceManipulator getDeviceInstanceManipulator() {
        return this.deviceInstanceManipulator;
    }
}
