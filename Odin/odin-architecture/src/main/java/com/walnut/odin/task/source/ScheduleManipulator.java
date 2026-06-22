package com.walnut.odin.task.source;

import com.walnut.odin.task.mapper.InstanceLineageAdjacentMapper;
import com.walnut.odin.task.mapper.InstanceLineageNodeMapper;
import com.walnut.odin.task.mapper.InstanceEventMapper;
import com.walnut.odin.task.mapper.InstanceExecMapper;

public interface ScheduleManipulator {

    InstanceEventMapper getInstanceEventMapper();

    InstanceLineageAdjacentMapper getInstanceLineageAdjacentMapper();

    InstanceLineageNodeMapper getInstanceLineageNodeMapper();

    InstanceExecMapper getInstanceExecMapper();

}
