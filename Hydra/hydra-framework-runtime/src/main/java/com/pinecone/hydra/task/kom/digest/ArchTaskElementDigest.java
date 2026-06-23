package com.pinecone.hydra.task.kom.digest;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.ArchTaskFamilyMeta;
import com.pinecone.hydra.task.marshal.TaskScheduleCycle;
import com.pinecone.hydra.task.marshal.TaskScheduleType;

import java.time.LocalDateTime;

public abstract class ArchTaskElementDigest extends ArchTaskFamilyMeta implements TaskElementDigest {

    protected long enumId;

    protected String type;

    protected String imagePath;

    protected String execArch;

    protected String resourceType;

    protected String deploymentMethod;

    protected short priority;

    protected short actuallyPriority;

    protected boolean dryRun;

    protected Long timeoutSeconds = 86400L;

    protected int retryTimes = 2;

    protected long retryIntervalSeconds;

    protected String scheduleCron;

    protected TaskScheduleCycle scheduleCycle;

    protected TaskScheduleType scheduleType;

    protected boolean enable;

    protected LocalDateTime scheduleStartTime;

    protected LocalDateTime scheduleEndTime;

    protected LocalDateTime nextScheduleTime;

    protected String processorName;

    protected String komPath;

    protected String systemKernelObjectPath;

    protected String projectGuid;

    protected LocalDateTime createTime;

    protected LocalDateTime updateTime;

    @Override
    public void setGuid( GUID guid ) {
        this.guid = guid;
    }

    @Override
    public void setName( String szName ) {
        this.name = szName;
    }

    @Override
    public long getEnumId() {
        return this.enumId;
    }

    @Override
    public void setEnumId( long nEnumId ) {
        this.enumId = nEnumId;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public void setType( String szType ) {
        this.type = szType;
    }

    @Override
    public String getImagePath() {
        return this.imagePath;
    }

    @Override
    public void setImagePath( String szImagePath ) {
        this.imagePath = szImagePath;
    }

    @Override
    public String getExecArch() {
        return this.execArch;
    }

    @Override
    public void setExecArch( String szExecArch ) {
        this.execArch = szExecArch;
    }

    @Override
    public String getResourceType() {
        return this.resourceType;
    }

    @Override
    public void setResourceType( String szResourceType ) {
        this.resourceType = szResourceType;
    }

    @Override
    public String getDeploymentMethod() {
        return this.deploymentMethod;
    }

    @Override
    public void setDeploymentMethod( String szDeploymentMethod ) {
        this.deploymentMethod = szDeploymentMethod;
    }

    @Override
    public short getPriority() {
        return this.priority;
    }

    @Override
    public void setPriority( short nPriority ) {
        this.priority = nPriority;
    }

    @Override
    public short getActuallyPriority() {
        return this.actuallyPriority;
    }

    @Override
    public void setActuallyPriority( short nActuallyPriority ) {
        this.actuallyPriority = nActuallyPriority;
    }

    @Override
    public boolean isDryRun() {
        return this.dryRun;
    }

    @Override
    public void setDryRun( boolean bDryRun ) {
        this.dryRun = bDryRun;
    }

    @Override
    public Long getTimeoutSeconds() {
        return this.timeoutSeconds;
    }

    @Override
    public void setTimeoutSeconds( Long nTimeoutSeconds ) {
        this.timeoutSeconds = nTimeoutSeconds;
    }

    @Override
    public int getRetryTimes() {
        return this.retryTimes;
    }

    @Override
    public void setRetryTimes( int nRetryTimes ) {
        this.retryTimes = nRetryTimes;
    }

    @Override
    public long getRetryIntervalSeconds() {
        return this.retryIntervalSeconds;
    }

    @Override
    public void setRetryIntervalSeconds( long nRetryIntervalSeconds ) {
        this.retryIntervalSeconds = nRetryIntervalSeconds;
    }

    @Override
    public String getScheduleCron() {
        return this.scheduleCron;
    }

    @Override
    public void setScheduleCron( String szScheduleCron ) {
        this.scheduleCron = szScheduleCron;
    }

    @Override
    public TaskScheduleCycle getScheduleCycle() {
        return this.scheduleCycle;
    }

    @Override
    public void setScheduleCycle( TaskScheduleCycle scheduleCycle ) {
        this.scheduleCycle = scheduleCycle;
    }

    @Override
    public TaskScheduleType getScheduleType() {
        return this.scheduleType;
    }

    @Override
    public void setScheduleType( TaskScheduleType scheduleType ) {
        this.scheduleType = scheduleType;
    }

    @Override
    public boolean isEnable() {
        return this.enable;
    }

    @Override
    public void setEnable( boolean bEnable ) {
        this.enable = bEnable;
    }

    @Override
    public LocalDateTime getScheduleStartTime() {
        return this.scheduleStartTime;
    }

    @Override
    public void setScheduleStartTime( LocalDateTime scheduleStartTime ) {
        this.scheduleStartTime = scheduleStartTime;
    }

    @Override
    public LocalDateTime getScheduleEndTime() {
        return this.scheduleEndTime;
    }

    @Override
    public void setScheduleEndTime( LocalDateTime scheduleEndTime ) {
        this.scheduleEndTime = scheduleEndTime;
    }

    @Override
    public LocalDateTime getNextScheduleTime() {
        return this.nextScheduleTime;
    }

    @Override
    public void setNextScheduleTime( LocalDateTime nextScheduleTime ) {
        this.nextScheduleTime = nextScheduleTime;
    }

    @Override
    public String getProcessorName() {
        return this.processorName;
    }

    @Override
    public void setProcessorName( String szProcessorName ) {
        this.processorName = szProcessorName;
    }

    @Override
    public String getKomPath() {
        return this.komPath;
    }

    @Override
    public void setKomPath( String szKomPath ) {
        this.komPath = szKomPath;
    }

    @Override
    public String getSystemKernelObjectPath() {
        return this.systemKernelObjectPath;
    }

    @Override
    public void setSystemKernelObjectPath( String szSystemKernelObjectPath ) {
        this.systemKernelObjectPath = szSystemKernelObjectPath;
    }

    @Override
    public String getProjectGuid() {
        return this.projectGuid;
    }

    @Override
    public void setProjectGuid( String szProjectGuid ) {
        this.projectGuid = szProjectGuid;
    }

    @Override
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    @Override
    public void setCreateTime( LocalDateTime createTime ) {
        this.createTime = createTime;
    }

    @Override
    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    @Override
    public void setUpdateTime( LocalDateTime updateTime ) {
        this.updateTime = updateTime;
    }
}
