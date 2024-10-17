package com.pinecone.hydra.volume.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;
import com.pinecone.hydra.unit.udtt.source.TriePathCacheManipulator;
import com.pinecone.hydra.unit.udtt.source.TrieTreeManipulator;
import com.pinecone.hydra.volume.ibatis.VolumeTreeMapper;

import javax.annotation.Resource;
import java.util.Map;

public class VolumeMasterTreeManipulatorImpl implements TreeMasterManipulator {
    @Resource
    @Structure( type = VolumeTreeMapper.class )
    TrieTreeManipulator trieTreeManipulator;


    public VolumeMasterTreeManipulatorImpl() {

    }

    public VolumeMasterTreeManipulatorImpl( KOIMappingDriver driver ) {
        driver.autoConstruct( VolumeMasterTreeManipulatorImpl.class, Map.of(), this );
    }
    @Override
    public TireOwnerManipulator getTireOwnerManipulator() {
        return null;
    }

    @Override
    public TrieTreeManipulator getTrieTreeManipulator() {
        return this.trieTreeManipulator;
    }

    @Override
    public TriePathCacheManipulator getTriePathCacheManipulator() {
        return null;
    }
}
