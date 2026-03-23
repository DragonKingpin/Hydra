package com.walnut.odin.conduct.schedule;


import com.walnut.odin.task.mapper.InstanceAtlasAdjacentMapper;
import com.walnut.odin.task.mapper.InstanceAtlasNodeMapper;
import com.walnut.odin.task.mapper.InstanceEventMapper;
import com.walnut.odin.task.mapper.InstanceExecMapper;

public interface InstanceManipulator {

    InstanceEventMapper getInstanceEventMapper();


    InstanceAtlasAdjacentMapper getInstanceAtlasAdjacentMapper();

    InstanceAtlasNodeMapper getInstanceAtlasNodeMapper();

    InstanceExecMapper getInstanceExecMapper();

}
