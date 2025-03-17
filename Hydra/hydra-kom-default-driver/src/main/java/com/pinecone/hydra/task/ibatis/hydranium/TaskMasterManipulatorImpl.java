package com.pinecone.hydra.task.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.conduct.ibatis.*;
import com.pinecone.hydra.task.ibatis.*;
import com.pinecone.hydra.task.kom.source.*;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.task.kom.source.CommonDataManipulator;
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
    @Structure(type = TaskMetaMapper.class)
    TaskMetaManipulator taskMetaManipulator;
    @Resource
    @Structure(type =TaskTreeMapper.class )
    TrieTreeManipulator             trieTreeManipulator;
    @Resource
    @Structure(type = TaskNodeMapper.class)
    TaskNodeManipulator taskNodeManipulator;
    @Resource
    @Structure(type = TaskCommonDataMapper.class )
    CommonDataManipulator commonDataManipulator;
    @Resource
    @Structure( type = TaskNamespaceMapper.class )
    TaskNamespaceManipulator taskNamespaceManipulator;
    @Resource
    @Structure( type = NamespaceRulesMapper.class )
    NamespaceRulesManipulator namespaceRulesManipulator;
    @Resource
    @Structure(type = TaskMasterTreeManipulatorImpl.class)
    KOISkeletonMasterManipulator skeletonMasterManipulator;
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
    public CommonDataManipulator getCommonDataManipulator() {
        return this.commonDataManipulator;
    }
/*
    @Override
    public ApplicationNodeManipulator getApplicationNodeManipulator() {
        return this.applicationNodeManipulator;
    }

    @Override
    public ApplicationMetaManipulator getApplicationElementManipulator() {
        return this.applicationMetaManipulator;
    }
*/

    @Override
    public TaskNamespaceManipulator getNamespaceManipulator() {
        return this.taskNamespaceManipulator;
    }

    @Override
    public TaskMetaManipulator getTaskMetaManipulator() {
        return null;
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
    public KOISkeletonMasterManipulator getSkeletonMasterManipulator() {
        return this.skeletonMasterManipulator;
    }


}
