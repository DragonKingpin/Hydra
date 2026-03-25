package com.walnut.odin.task.source;

import com.walnut.odin.task.mapper.InstanceAtlasAdjacentMapper;
import com.walnut.odin.task.mapper.InstanceAtlasNodeMapper;
import com.walnut.odin.task.mapper.InstanceEventMapper;
import com.walnut.odin.task.mapper.InstanceExecMapper;

public interface ScheduleManipulator {

    InstanceEventMapper getInstanceEventMapper();

    InstanceAtlasAdjacentMapper getInstanceAtlasAdjacentMapper();

    InstanceAtlasNodeMapper getInstanceAtlasNodeMapper();

    InstanceExecMapper getInstanceExecMapper();

}
