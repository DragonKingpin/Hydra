package com.pinecone.hydra.deploy.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.deploy.ibatis.DeployNamespaceMapper;
import com.pinecone.hydra.deploy.ibatis.DeployNodeMapper;
import com.pinecone.hydra.deploy.ibatis.DeployNodeMetaMapper;
import com.pinecone.hydra.deploy.ibatis.DeployNodeOwnerMapper;
import com.pinecone.hydra.deploy.ibatis.DeployTreeMapper;
import com.pinecone.hydra.deploy.ibatis.ClusterNodeMapper;
import com.pinecone.hydra.deploy.ibatis.NamespaceRulesMapper;
import com.pinecone.hydra.deploy.ibatis.PhysicalHostMapper;
import com.pinecone.hydra.deploy.ibatis.QuickElementMapper;
import com.pinecone.hydra.deploy.ibatis.VirtualMachineMapper;
import com.pinecone.hydra.deploy.kom.source.DeployMasterManipulator;
import com.pinecone.hydra.deploy.kom.source.DeployNamespaceManipulator;
import com.pinecone.hydra.deploy.kom.source.DeployNodeManipulator;
import com.pinecone.hydra.deploy.kom.source.PhysicalHostManipulator;
import com.pinecone.hydra.deploy.kom.source.QuickElementManipulator;
import com.pinecone.hydra.deploy.kom.source.VirtualMachineManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.deploy.kom.source.ClusterNodeManipulator;
import com.pinecone.hydra.deploy.kom.source.NamespaceRulesManipulator;
import com.pinecone.hydra.deploy.kom.source.NodeMetaManipulator;
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
    @Structure(type = DeployNodeMetaMapper.class )
    NodeMetaManipulator nodeMetaManipulator;

    @Resource
    @Structure(type = DeployNodeMapper.class)
    DeployNodeManipulator deployNodeManipulator;

    @Resource
    @Structure( type = DeployNamespaceMapper.class )
    DeployNamespaceManipulator deployNamespaceManipulator;

    @Resource
    @Structure( type = NamespaceRulesMapper.class )
    NamespaceRulesManipulator namespaceRulesManipulator;

    @Resource
    @Structure( type = PhysicalHostMapper.class )
    PhysicalHostManipulator physicalHostManipulator;

    @Resource
    @Structure( type = VirtualMachineMapper.class )
    VirtualMachineManipulator virtualMachineManipulator;

    @Resource
    @Structure( type = QuickElementMapper.class )
    QuickElementManipulator quickElementManipulator;


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
    public NodeMetaManipulator getNodeMetaManipulator() {
        return this.nodeMetaManipulator;
    }

    @Override
    public ClusterNodeManipulator getJobNodeManipulator() {
        return this.jobNodeManipulator;
    }

    @Override
    public DeployNodeManipulator getDeployNodeManipulator() {
        return this.deployNodeManipulator;
    }

    @Override
    public DeployNamespaceManipulator getNamespaceManipulator() {
        return this.deployNamespaceManipulator;
    }

    @Override
    public NamespaceRulesManipulator getNamespaceRulesManipulator() {
        return this.namespaceRulesManipulator;
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
}
