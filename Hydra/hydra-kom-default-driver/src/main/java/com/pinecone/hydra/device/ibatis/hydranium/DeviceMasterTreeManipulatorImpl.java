package com.pinecone.hydra.device.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.device.ibatis.DeviceNodeOwnerMapper;
import com.pinecone.hydra.device.ibatis.DeviceNodePathCacheMapper;
import com.pinecone.hydra.device.ibatis.DeviceTreeMapper;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.unit.imperium.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.imperium.source.TreeMasterManipulator;
import com.pinecone.hydra.unit.imperium.source.TriePathCacheManipulator;
import com.pinecone.hydra.unit.imperium.source.TrieTreeManipulator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class DeviceMasterTreeManipulatorImpl implements TreeMasterManipulator {

    @Resource
    @Structure( type = DeviceNodePathCacheMapper.class )
    TriePathCacheManipulator triePathCacheManipulator;

    @Resource
    @Structure( type = DeviceNodeOwnerMapper.class )
    TireOwnerManipulator tireOwnerManipulator;

    @Resource
    @Structure( type = DeviceTreeMapper.class )
    TrieTreeManipulator  trieTreeManipulator;

    public DeviceMasterTreeManipulatorImpl() {

    }

    public DeviceMasterTreeManipulatorImpl(KOIMappingDriver driver ) {
        driver.autoConstruct( DeviceMasterTreeManipulatorImpl.class, Map.of(), this );
    }

    @Override
    public TriePathCacheManipulator getTriePathCacheManipulator() {
        return this.triePathCacheManipulator;
    }

    @Override
    public TireOwnerManipulator getTireOwnerManipulator() {
        return this.tireOwnerManipulator;
    }

    @Override
    public TrieTreeManipulator getTrieTreeManipulator() {
        return this.trieTreeManipulator;
    }


}
