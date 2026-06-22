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
    @Structure(type = InstanceLineageAdjacentMapper.class)
    private InstanceLineageAdjacentMapper mInstanceLineageAdjacentMapper;

    @Resource
    @Structure(type = InstanceLineageNodeMapper.class)
    private InstanceLineageNodeMapper mInstanceLineageNodeMapper;

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
            InstanceLineageAdjacentMapper instanceLineageAdjacentMapper,
            InstanceLineageNodeMapper instanceLineageNodeMapper,
            InstanceExecMapper instanceExecMapper
    ) {
        this.minstanceEventMapper = instanceEventMapper;
        this.mInstanceLineageAdjacentMapper = instanceLineageAdjacentMapper;
        this.mInstanceLineageNodeMapper = instanceLineageNodeMapper;
        this.minstanceExecMapper = instanceExecMapper;
    }

    @Override
    public InstanceEventMapper getInstanceEventMapper() {
        return this.minstanceEventMapper;
    }

    @Override
    public InstanceLineageAdjacentMapper getInstanceLineageAdjacentMapper() {
        return this.mInstanceLineageAdjacentMapper;
    }

    @Override
    public InstanceLineageNodeMapper getInstanceLineageNodeMapper() {
        return this.mInstanceLineageNodeMapper;
    }

    @Override
    public InstanceExecMapper getInstanceExecMapper() {
        return this.minstanceExecMapper;
    }
}
