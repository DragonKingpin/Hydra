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
import com.walnut.odin.formation.source.KernelMasterManipulator;
import com.walnut.odin.formation.source.MasterManipulator;
import com.walnut.odin.formation.mapper.transaction.IbatisOdinMappingTransaction;
import com.walnut.odin.mapper.transaction.OdinMappingTransaction;

public class OdinUniformFormationMappingDriver extends ArchMappingDriver implements OdinFormationMappingDriver {

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

    protected MasterManipulator      mMasterManipulator;
    protected OdinMappingTransaction mTransaction;

    public OdinUniformFormationMappingDriver( Processum superiorProcess ) {
        super( superiorProcess );
    }

    public OdinUniformFormationMappingDriver(
            Processum superiorProcess,
            IbatisClient ibatisClient,
            ResourceDispenserCenter dispenserCenter
    ) {
        super( superiorProcess, ibatisClient, dispenserCenter, FormationGroupMapper.class.getPackageName() );
        ibatisClient.addXMLObjectScope( "mapper.kernel.formation" );
        this.autoConstruct( OdinUniformFormationMappingDriver.class, Map.of(), this );
        this.mTransaction = new IbatisOdinMappingTransaction( ibatisClient );
        this.mMasterManipulator = new KernelMasterManipulator(
                this,
                this.mGroupMapper,
                this.mGroupTaskMapper,
                this.mRunMapper,
                this.mPageMapper,
                this.mFrameMapper
        );
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
    public MasterManipulator getFormationMasterManipulator() {
        return this.mMasterManipulator;
    }

    @Override
    public OdinMappingTransaction transaction() {
        return this.mTransaction;
    }

    @Override
    public KOIMasterManipulator getMasterManipulator() {
        return null;
    }
}
