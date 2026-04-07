package com.walnut.odin.task.mapper;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.walnut.odin.task.source.ScheduleManipulator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class ScheduleManipulatorImpl implements ScheduleManipulator {

    @Resource
    @Structure(type = InstanceEventMapper.class)
    private InstanceEventMapper minstanceEventMapper;

    @Resource
    @Structure(type = InstanceAtlasAdjacentMapper.class)
    private InstanceAtlasAdjacentMapper minstanceAtlasAdjacentMapper;

    @Resource
    @Structure(type = InstanceAtlasNodeMapper.class)
    private InstanceAtlasNodeMapper minstanceAtlasNodeMapper;

    @Resource
    @Structure(type = InstanceExecMapper.class)
    private InstanceExecMapper minstanceExecMapper;

    public ScheduleManipulatorImpl() {
    }

    public ScheduleManipulatorImpl(KOIMappingDriver driver) {
        driver.autoConstruct(ScheduleManipulatorImpl.class, Map.of(), this);
    }

    public ScheduleManipulatorImpl(
            InstanceEventMapper instanceEventMapper,
            InstanceAtlasAdjacentMapper instanceAtlasAdjacentMapper,
            InstanceAtlasNodeMapper instanceAtlasNodeMapper,
            InstanceExecMapper instanceExecMapper
    ) {
        this.minstanceEventMapper = instanceEventMapper;
        this.minstanceAtlasAdjacentMapper = instanceAtlasAdjacentMapper;
        this.minstanceAtlasNodeMapper = instanceAtlasNodeMapper;
        this.minstanceExecMapper = instanceExecMapper;
    }

    @Override
    public InstanceEventMapper getInstanceEventMapper() {
        return this.minstanceEventMapper;
    }

    @Override
    public InstanceAtlasAdjacentMapper getInstanceAtlasAdjacentMapper() {
        return this.minstanceAtlasAdjacentMapper;
    }

    @Override
    public InstanceAtlasNodeMapper getInstanceAtlasNodeMapper() {
        return this.minstanceAtlasNodeMapper;
    }

    @Override
    public InstanceExecMapper getInstanceExecMapper() {
        return this.minstanceExecMapper;
    }
}
