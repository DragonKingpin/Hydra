package com.walnut.odin.task.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.hydra.task.TaskFamilyMeta;

public class GenericRavenTaskMeta implements RavenTaskMeta {

    protected TaskFamilyMeta kernelMeta;

    protected int taskVersion;
    protected boolean rootTask;

    protected GUID guid;

    // For bean initialization.
    public GenericRavenTaskMeta() {

    }

    public GenericRavenTaskMeta( TaskFamilyMeta kernelMeta ) {
        this.kernelMeta = kernelMeta;
    }

    @Override
    public int getTaskVersion() {
        return this.taskVersion;
    }

    @Override
    public void setTaskVersion( int taskVersion ) {
        this.taskVersion = taskVersion;
    }

    @Override
    public boolean isRootTask() {
        return this.rootTask;
    }

    @Override
    public void setRootTask( boolean rootTask ) {
        this.rootTask = rootTask;
    }

    @Override
    public TaskFamilyMeta getKernelMeta() {
        return this.kernelMeta;
    }

    @Override
    public void setKernelMeta( TaskFamilyMeta kernelMeta ) {
        this.kernelMeta = kernelMeta;
    }

    @Override
    public GUID getGuid() {
        return this.guid;
    }

    @Override
    public void setGuid( GUID guid ) {
        this.guid = guid;
    }

    @Override
    public String getTaskName() {
        return this.kernelMeta.getName();
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this, JsonifyExpectedKey );
    }

}
