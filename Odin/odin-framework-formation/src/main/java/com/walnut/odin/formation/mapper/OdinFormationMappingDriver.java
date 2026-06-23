package com.walnut.odin.formation.mapper;

import java.util.Map;

import javax.annotation.Resource;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.entity.ibatis.hydranium.ArchMappingDriver;
import com.pinecone.hydra.system.component.ResourceDispenserCenter;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.walnut.odin.formation.source.FormationGroupMapper;
import com.walnut.odin.formation.source.FormationGroupTaskMapper;
import com.walnut.odin.formation.source.FormationRunFrameMapper;
import com.walnut.odin.formation.source.FormationRunMapper;
import com.walnut.odin.formation.source.FormationRunPageMapper;

public class OdinFormationMappingDriver extends ArchMappingDriver {

    @Resource
    @Structure( type = FormationGroupMapper.class )
    protected FormationGroupMapper mGroupMapper;

    @Resource
    @Structure( type = FormationGroupTaskMapper.class )
    protected FormationGroupTaskMapper mGroupTaskMapper;

    @Resource
    @Structure( type = FormationRunMapper.class )
    protected FormationRunMapper mRunMapper;

    @Resource
    @Structure( type = FormationRunPageMapper.class )
    protected FormationRunPageMapper mPageMapper;

    @Resource
    @Structure( type = FormationRunFrameMapper.class )
    protected FormationRunFrameMapper mFrameMapper;

    public OdinFormationMappingDriver( Processum superiorProcess ) {
        super( superiorProcess );
    }

    public OdinFormationMappingDriver(
            Processum superiorProcess,
            IbatisClient ibatisClient,
            ResourceDispenserCenter dispenserCenter
    ) {
        super( superiorProcess, ibatisClient, dispenserCenter, FormationGroupMapper.class.getPackageName() );
        ibatisClient.addXMLObjectScope( "mapper.kernel.formation" );
        this.autoConstruct( OdinFormationMappingDriver.class, Map.of(), this );
    }

    public FormationGroupMapper groupMapper() {
        return this.mGroupMapper;
    }

    public FormationGroupTaskMapper groupTaskMapper() {
        return this.mGroupTaskMapper;
    }

    public FormationRunMapper runMapper() {
        return this.mRunMapper;
    }

    public FormationRunPageMapper pageMapper() {
        return this.mPageMapper;
    }

    public FormationRunFrameMapper frameMapper() {
        return this.mFrameMapper;
    }

    @Override
    public KOIMasterManipulator getMasterManipulator() {
        return null;
    }
}
