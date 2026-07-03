package com.pinecone.hydra.lifecycle.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.lifecycle.ibatis.StorageLifecycleTaskMapper;
import com.pinecone.hydra.storage.lifecycle.source.StorageLifecycleMasterManipulator;
import com.pinecone.hydra.storage.lifecycle.source.StorageLifecycleTaskManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class StorageLifecycleMasterManipulatorImpl implements StorageLifecycleMasterManipulator {
    @Resource
    @Structure( type = StorageLifecycleTaskMapper.class )
    StorageLifecycleTaskManipulator taskManipulator;

    public StorageLifecycleMasterManipulatorImpl() {
    }

    public StorageLifecycleMasterManipulatorImpl( KOIMappingDriver driver ) {
        driver.autoConstruct( StorageLifecycleMasterManipulatorImpl.class, Map.of(), this );
    }

    @Override
    public StorageLifecycleTaskManipulator getTaskManipulator() {
        return this.taskManipulator;
    }

    @Override
    public KOISkeletonMasterManipulator getSkeletonMasterManipulator() {
        return null;
    }
}
