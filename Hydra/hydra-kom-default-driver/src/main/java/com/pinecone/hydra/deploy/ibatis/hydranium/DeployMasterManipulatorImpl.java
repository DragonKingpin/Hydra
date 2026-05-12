package com.pinecone.hydra.deploy.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.deploy.ibatis.ContainerElementMapper;
import com.pinecone.hydra.deploy.ibatis.DeployNamespaceMapper;
import com.pinecone.hydra.deploy.ibatis.DeployNodeOwnerMapper;
import com.pinecone.hydra.deploy.ibatis.DeployTreeMapper;
import com.pinecone.hydra.deploy.ibatis.ClusterNodeMapper;
import com.pinecone.hydra.deploy.ibatis.PhysicalHostMapper;
import com.pinecone.hydra.deploy.ibatis.QuickElementMapper;
import com.pinecone.hydra.deploy.ibatis.VirtualMachineMapper;
import com.pinecone.hydra.deploy.kom.source.ContainerElementManipulator;
import com.pinecone.hydra.deploy.kom.source.DeployMasterManipulator;
import com.pinecone.hydra.deploy.kom.source.DeployNamespaceManipulator;
import com.pinecone.hydra.deploy.kom.source.PhysicalHostManipulator;
import com.pinecone.hydra.deploy.kom.source.QuickElementManipulator;
import com.pinecone.hydra.deploy.kom.source.VirtualMachineManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.deploy.kom.source.ClusterNodeManipulator;
import com.pinecone.hydra.unit.imperium.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.imperium.source.TrieTreeManipulator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class DeployMasterManipulatorImpl implements DeployMasterManipulator {
    @Resource
    @Structure( type = DeployNodeOwnerMapper.class )
    TireOwnerManipulator tireOwnerManipulator;

    @Resource
    @Structure(type = DeployTreeMapper.class )
    TrieTreeManipulator trieTreeManipulator;

    @Resource
    @Structure(type = ClusterNodeMapper.class )
    ClusterNodeManipulator jobNodeManipulator;

    @Resource
    @Structure( type = DeployNamespaceMapper.class )
    DeployNamespaceManipulator deployNamespaceManipulator;

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

    @Resource( type = DeployMasterTreeManipulatorImpl.class )
    KOISkeletonMasterManipulator skeletonMasterManipulator;

    public DeployMasterManipulatorImpl() {

    }

    public DeployMasterManipulatorImpl(KOIMappingDriver driver ) {
        driver.autoConstruct( DeployMasterManipulatorImpl.class, Map.of(), this );
        this.skeletonMasterManipulator = new DeployMasterTreeManipulatorImpl( driver );
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
    public DeployNamespaceManipulator getNamespaceManipulator() {
        return this.deployNamespaceManipulator;
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
}
