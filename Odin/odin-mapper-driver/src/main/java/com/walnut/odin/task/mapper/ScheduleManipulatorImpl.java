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
    @Structure(type = InstanceExecMapper.class)
    private InstanceExecMapper minstanceExecMapper;

    @Resource
    @Structure(type = InstanceExecAuditMapper.class)
    private InstanceExecAuditMapper mInstanceExecAuditMapper;

    @Resource
    @Structure(type = PatrolWatchdogLogMapper.class)
    private PatrolWatchdogLogMapper mPatrolWatchdogLogMapper;

    @Resource
    @Structure(type = TaskInstanceOperationLogMapper.class)
    private TaskInstanceOperationLogMapper mTaskInstanceOperationLogMapper;

    public ScheduleManipulatorImpl() {
    }

    public ScheduleManipulatorImpl(KOIMappingDriver driver) {
        driver.autoConstruct(ScheduleManipulatorImpl.class, Map.of(), this);
    }

    public ScheduleManipulatorImpl(
            InstanceEventMapper instanceEventMapper,
            InstanceLineageAdjacentMapper instanceLineageAdjacentMapper,
            InstanceExecMapper instanceExecMapper
    ) {
        this.minstanceEventMapper = instanceEventMapper;
        this.mInstanceLineageAdjacentMapper = instanceLineageAdjacentMapper;
        this.minstanceExecMapper = instanceExecMapper;
    }

    public ScheduleManipulatorImpl(
            InstanceEventMapper instanceEventMapper,
            InstanceLineageAdjacentMapper instanceLineageAdjacentMapper,
            InstanceExecMapper instanceExecMapper,
            InstanceExecAuditMapper instanceExecAuditMapper
    ) {
        this.minstanceEventMapper = instanceEventMapper;
        this.mInstanceLineageAdjacentMapper = instanceLineageAdjacentMapper;
        this.minstanceExecMapper = instanceExecMapper;
        this.mInstanceExecAuditMapper = instanceExecAuditMapper;
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
    public InstanceExecMapper getInstanceExecMapper() {
        return this.minstanceExecMapper;
    }

    @Override
    public InstanceExecAuditMapper getInstanceExecAuditMapper() {
        return this.mInstanceExecAuditMapper;
    }

    @Override
    public PatrolWatchdogLogMapper getPatrolWatchdogLogMapper() {
        return this.mPatrolWatchdogLogMapper;
    }

    @Override
    public TaskInstanceOperationLogMapper getTaskInstanceOperationLogMapper() {
        return this.mTaskInstanceOperationLogMapper;
    }
}
