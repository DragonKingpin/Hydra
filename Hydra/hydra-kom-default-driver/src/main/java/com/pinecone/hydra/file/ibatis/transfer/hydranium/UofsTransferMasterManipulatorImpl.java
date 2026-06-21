package com.pinecone.hydra.file.ibatis.transfer.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.file.ibatis.transfer.UofsTransferItemMapper;
import com.pinecone.hydra.file.ibatis.transfer.UofsTransferTaskMapper;
import com.pinecone.hydra.storage.file.transfer.source.UofsTransferItemManipulator;
import com.pinecone.hydra.storage.file.transfer.source.UofsTransferMasterManipulator;
import com.pinecone.hydra.storage.file.transfer.source.UofsTransferTaskManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class UofsTransferMasterManipulatorImpl implements UofsTransferMasterManipulator {
    @Resource
    @Structure( type = UofsTransferTaskMapper.class )
    protected UofsTransferTaskManipulator mTaskManipulator;

    @Resource
    @Structure( type = UofsTransferItemMapper.class )
    protected UofsTransferItemManipulator mItemManipulator;

    public UofsTransferMasterManipulatorImpl() {
    }

    public UofsTransferMasterManipulatorImpl( KOIMappingDriver driver ) {
        driver.autoConstruct( UofsTransferMasterManipulatorImpl.class, Map.of(), this );
    }

    @Override
    public UofsTransferTaskManipulator getTaskManipulator() {
        return this.mTaskManipulator;
    }

    @Override
    public UofsTransferItemManipulator getItemManipulator() {
        return this.mItemManipulator;
    }

    @Override
    public KOISkeletonMasterManipulator getSkeletonMasterManipulator() {
        return null;
    }
}
