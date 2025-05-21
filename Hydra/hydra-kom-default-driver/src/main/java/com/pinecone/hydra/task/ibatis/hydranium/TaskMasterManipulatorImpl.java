package com.pinecone.hydra.task.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;

import com.pinecone.hydra.task.kom.instance.source.InstanceNodeManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.task.ibatis.JobNodeMapper;
import com.pinecone.hydra.task.ibatis.NamespaceRulesMapper;
import com.pinecone.hydra.task.ibatis.TaskNodeMetaMapper;
import com.pinecone.hydra.task.ibatis.TaskNamespaceMapper;
import com.pinecone.hydra.task.ibatis.TaskNodeMapper;
import com.pinecone.hydra.task.ibatis.TaskNodeOwnerMapper;
import com.pinecone.hydra.task.ibatis.TaskTreeMapper;
import com.pinecone.hydra.task.ibatis.InstanceNodeMapper;
import com.pinecone.hydra.task.kom.source.JobNodeManipulator;
import com.pinecone.hydra.task.kom.source.NodeMetaManipulator;
import com.pinecone.hydra.task.kom.source.NamespaceRulesManipulator;
import com.pinecone.hydra.task.kom.source.TaskMasterManipulator;
import com.pinecone.hydra.task.kom.source.TaskNamespaceManipulator;
import com.pinecone.hydra.task.kom.source.TaskNodeManipulator;
import com.pinecone.hydra.unit.imperium.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.imperium.source.TrieTreeManipulator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class TaskMasterManipulatorImpl implements TaskMasterManipulator {
    @Resource
    @Structure( type = TaskNodeOwnerMapper.class )
    TireOwnerManipulator            tireOwnerManipulator;

    @Resource
    @Structure(type = TaskTreeMapper.class )
    TrieTreeManipulator             trieTreeManipulator;

    @Resource
    @Structure(type = TaskNodeMapper.class)
    TaskNodeManipulator taskNodeManipulator;

    @Resource
    @Structure(type = TaskNodeMetaMapper.class )
    NodeMetaManipulator nodeMetaManipulator;

    @Resource
    @Structure(type = JobNodeMapper.class )
    JobNodeManipulator jobNodeManipulator;

    @Resource
    @Structure( type = TaskNamespaceMapper.class )
    TaskNamespaceManipulator taskNamespaceManipulator;

    @Resource
    @Structure( type = NamespaceRulesMapper.class )
    NamespaceRulesManipulator namespaceRulesManipulator;

    @Resource
    @Structure(type = TaskMasterTreeManipulatorImpl.class)
    KOISkeletonMasterManipulator skeletonMasterManipulator;


    @Resource
    @Structure(type = InstanceNodeMapper.class)
    InstanceNodeManipulator instanceNodeManipulator;

    public TaskMasterManipulatorImpl() {

    }

    public TaskMasterManipulatorImpl( KOIMappingDriver driver ) {
        driver.autoConstruct( TaskMasterManipulatorImpl.class, Map.of(), this );
        this.skeletonMasterManipulator = new TaskMasterTreeManipulatorImpl( driver );
    }


    @Override
    public TrieTreeManipulator getTrieTreeManipulator() {
        return this.trieTreeManipulator;
    }

    @Override
    public TaskNodeManipulator getTaskNodeManipulator() {
        return this.taskNodeManipulator;
    }

    @Override
    public NodeMetaManipulator getNodeMetaManipulator() {
        return this.nodeMetaManipulator;
    }

    @Override
    public JobNodeManipulator getJobNodeManipulator() {
        return this.jobNodeManipulator;
    }

    @Override
    public TaskNamespaceManipulator getNamespaceManipulator() {
        return this.taskNamespaceManipulator;
    }

    @Override
    public NamespaceRulesManipulator getNamespaceRulesManipulator() {
        return this.namespaceRulesManipulator;
    }

    @Override
    public KOISkeletonMasterManipulator getSkeletonMasterManipulator() {
        return this.skeletonMasterManipulator;
    }

    @Override
    public TireOwnerManipulator getTireOwnerManipulator() {
        return this.tireOwnerManipulator;
    }

    @Override
    public InstanceNodeManipulator getInstanceNodeManipulator() {
        return this.instanceNodeManipulator;
    }


}
