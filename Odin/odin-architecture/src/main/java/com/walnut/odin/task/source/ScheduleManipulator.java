package com.walnut.odin.task.source;

import com.walnut.odin.task.mapper.InstanceLineageAdjacentMapper;
import com.walnut.odin.task.mapper.InstanceExecAuditMapper;
import com.walnut.odin.task.mapper.InstanceEventMapper;
import com.walnut.odin.task.mapper.InstanceExecMapper;
import com.walnut.odin.task.mapper.PatrolWatchdogLogMapper;
import com.walnut.odin.task.mapper.TaskInstanceOperationLogMapper;

public interface ScheduleManipulator {

    InstanceEventMapper getInstanceEventMapper();

    InstanceLineageAdjacentMapper getInstanceLineageAdjacentMapper();

    InstanceExecMapper getInstanceExecMapper();

    InstanceExecAuditMapper getInstanceExecAuditMapper();

    PatrolWatchdogLogMapper getPatrolWatchdogLogMapper();

    TaskInstanceOperationLogMapper getTaskInstanceOperationLogMapper();

}
