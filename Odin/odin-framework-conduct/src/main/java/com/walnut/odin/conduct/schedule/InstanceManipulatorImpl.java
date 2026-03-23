package com.walnut.odin.conduct.schedule;

import com.walnut.odin.task.mapper.InstanceAtlasAdjacentMapper;
import com.walnut.odin.task.mapper.InstanceAtlasNodeMapper;
import com.walnut.odin.task.mapper.InstanceEventMapper;
import com.walnut.odin.task.mapper.InstanceExecMapper;

import javax.annotation.Resource;

public class InstanceManipulatorImpl implements InstanceManipulator{

    @Resource
    private InstanceEventMapper              minstanceEventMapper;
    @Resource
    private InstanceAtlasAdjacentMapper      minstanceAtlasAdjacentMapper;
    @Resource
    private InstanceAtlasNodeMapper          minstanceAtlasNodeMapper;
    @Resource
    private InstanceExecMapper               minstanceExecMapper;




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
