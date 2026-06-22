package com.walnut.odin.atlas.mapper;

import java.util.Map;

import javax.annotation.Resource;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.entity.ibatis.hydranium.ArchMappingDriver;
import com.pinecone.hydra.system.component.ResourceDispenserCenter;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;

public class OdinAtlasMappingDriver extends ArchMappingDriver {

    @Resource
    @Structure( type = TaskLineageMapper.class )
    protected TaskLineageMapper mTaskLineageMapper;

    public OdinAtlasMappingDriver( Processum superiorProcess ) {
        super( superiorProcess );
    }

    public OdinAtlasMappingDriver(
            Processum superiorProcess, IbatisClient ibatisClient, ResourceDispenserCenter dispenserCenter
    ) {
        super( superiorProcess, ibatisClient, dispenserCenter, OdinAtlasMappingDriver.class.getPackageName() );
        this.autoConstruct( OdinAtlasMappingDriver.class, Map.of(), this );
    }

    public TaskLineageMapper taskLineageMapper() {
        return this.mTaskLineageMapper;
    }

    @Override
    public KOIMasterManipulator getMasterManipulator() {
        return null;
    }
}
