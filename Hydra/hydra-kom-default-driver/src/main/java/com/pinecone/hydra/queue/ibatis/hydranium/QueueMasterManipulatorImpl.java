package com.pinecone.hydra.queue.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.queue.ibatis.AtlasExecuteQueueMapper;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.unit.iqueue.DPQueueManipulator;
import com.pinecone.hydra.unit.iqueue.QueueMasterManipulator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class QueueMasterManipulatorImpl implements QueueMasterManipulator {
    @Resource
    @Structure( type = AtlasExecuteQueueMapper.class )
    protected DPQueueManipulator mDPQueueManipulator;

    @Override
    public KOISkeletonMasterManipulator getSkeletonMasterManipulator() {
        return null;
    }

    public QueueMasterManipulatorImpl() {}

    public QueueMasterManipulatorImpl( KOIMappingDriver driver ) {
        driver.autoConstruct( QueueMasterManipulatorImpl.class, Map.of(), this );
    }

    @Override
    public DPQueueManipulator getDPQueueManipulator() {
        return this.mDPQueueManipulator;
    }
}
