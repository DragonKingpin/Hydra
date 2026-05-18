package com.walnut.odin.task.mapper;

import java.util.List;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.entity.ibatis.hydranium.ArchMappingDriver;
import com.pinecone.hydra.system.component.ResourceDispenserCenter;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.task.ibatis.hydranium.TaskMappingDriver;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.slime.jelly.source.ibatis.ProxySessionMapperPool;
import com.walnut.odin.project.mapper.TaskProjectMapper;

public class OdinUniformTaskMappingDriver extends ArchMappingDriver implements OdinTaskMappingDriver {
    protected KOIMasterManipulator mKOIMasterManipulator;

    protected KOIMappingDriver     mParentDriver;

    public OdinUniformTaskMappingDriver( Processum superiorProcess ) {
        super( superiorProcess );
    }

    public OdinUniformTaskMappingDriver( Processum superiorProcess, IbatisClient ibatisClient, ResourceDispenserCenter dispenserCenter ) {
        super( superiorProcess, ibatisClient, dispenserCenter, OdinUniformTaskMappingDriver.class.getPackageName().replace( "hydranium", "" ) );

        this.prepare_project_mapper( ibatisClient, dispenserCenter );

        this.mParentDriver = new TaskMappingDriver(
                superiorProcess, ibatisClient, dispenserCenter
        );

        this.mKOIMasterManipulator = new RavenTaskMasterManipulatorImpl( this, (TaskMappingDriver)this.getParentDriver() );
    }

    protected void prepare_project_mapper( IbatisClient ibatisClient, ResourceDispenserCenter dispenserCenter ) {
        ibatisClient.addXMLObjectScope( "mapper.kernel.project" );
        List<Class<? > > mapperCandidates = ibatisClient.addDataAccessObjectScope( TaskProjectMapper.class.getPackageName() );
        for ( Class<? > mapperClass : mapperCandidates ) {
            dispenserCenter.getInstanceDispenser().register( mapperClass, new ProxySessionMapperPool( ibatisClient, mapperClass ) );
        }
    }

    @Override
    public KOIMasterManipulator getMasterManipulator() {
        return this.mKOIMasterManipulator;
    }

    @Override
    public KOIMappingDriver getParentDriver() {
        return this.mParentDriver;
    }

}
