package com.pinecone.hydra.task.kom.entity;

import java.time.LocalDateTime;
import java.util.Map;

import com.pinecone.framework.util.json.JSON;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.task.TaskExtraMeta;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.marshal.TaskScheduleCycle;
import com.pinecone.hydra.task.marshal.TaskScheduleType;

public class GenericTaskElement extends ArchElementNode implements TaskElement {
    protected String                   taskType;
    protected String                   imagePath;
    protected String                   execArch;
    protected String                   resourceType;
    protected String                   deploymentMethod;

    protected short                    priority;
    protected short                    actuallyPriority;
    protected boolean                  dryRun;

    protected String                   scheduleCron;
    protected TaskScheduleCycle        scheduleCycle;
    protected TaskScheduleType         scheduleType;
    protected boolean                  enable;

    protected LocalDateTime            scheduleStartTime;
    protected LocalDateTime            scheduleEndTime;
    protected LocalDateTime            nextScheduleTime;

    protected String                   processorName;

    private void initSelf( Map<String, Object > joEntity ) {
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
        if ( this.szElementaryConfig != null ) {
            this.elementaryConfig = (JSONObject)JSON.parse( this.szElementaryConfig );
        }
    }

    public GenericTaskElement() {
        super();
    }

    public GenericTaskElement( Map<String, Object > joEntity ) {
        super( joEntity );
        this.initSelf( joEntity );
    }

    public GenericTaskElement( Map<String, Object > joEntity, TaskInstrument taskInstrument ) {
        super( joEntity, taskInstrument);
        this.initSelf( joEntity );
    }

    public GenericTaskElement( TaskInstrument taskInstrument ) {
        super(taskInstrument);
    }

    @Override
    public String getType() {
        return this.taskType;
    }

    @Override
    public void setType( String taskType ) {
        this.taskType = taskType;
    }

    @Override
    public String getImagePath() {
        return this.imagePath;
    }

    @Override
    public void setImagePath( String imagePath ) {
        this.imagePath = imagePath;
    }

    @Override
    public String getExecArch() {
        return this.execArch;
    }

    @Override
    public void setExecArch( String execArch ) {
        this.execArch = execArch;
    }

    @Override
    public String getResourceType() {
        return this.resourceType;
    }

    @Override
    public void setResourceType( String resourceType ) {
        this.resourceType = resourceType;
    }

    @Override
    public short getPriority() {
        return this.priority;
    }

    @Override
    public void setPriority( int priority ) {
        this.priority = (short) priority;
    }

    @Override
    public short getActuallyPriority() {
        return this.actuallyPriority;
    }

    @Override
    public void setActuallyPriority( int actuallyPriority ) {
        this.actuallyPriority = (short) actuallyPriority;
    }

    @Override
    public String getDeploymentMethod() {
        return this.deploymentMethod;
    }

    @Override
    public void setDeploymentMethod( String deploymentMethod ) {
        this.deploymentMethod = deploymentMethod;
    }


    @Override
    public boolean isDryRun() {
        return this.dryRun;
    }

    @Override
    public void setDryRun( boolean dryRun ) {
        this.dryRun = dryRun;
    }


    @Override
    public String getScheduleCron() {
        return this.scheduleCron;
    }

    @Override
    public void setScheduleCron( String scheduleCron ) {
        this.scheduleCron = scheduleCron;
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
    public void setScheduleCycle ( TaskScheduleCycle kernelScheduleCycle ) {
        this.scheduleCycle = kernelScheduleCycle;
    }

    @Override
    public TaskScheduleCycle getScheduleCycle() {
        return this.scheduleCycle;
    }

    @Override
    public void setScheduleType ( TaskScheduleType kernelScheduleType ) {
        this.scheduleType = kernelScheduleType;
    }

    @Override
    public TaskScheduleType getScheduleType() {
        return this.scheduleType;
    }



    @Override
    public boolean isEnable() {
        return this.enable;
    }

    @Override
    public void setEnable( boolean enable ) {
        this.enable = enable;
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
        return  this.scheduleEndTime;
    }

    @Override
    public void setScheduleEndTime( LocalDateTime scheduleEndTime ) {
        this.scheduleEndTime = scheduleEndTime;
    }

    @Override
    public String getProcessorName() {
        return this.processorName;
    }

    @Override
    public void setProcessorName( String processorName ) {
        this.processorName = processorName;
    }

    @Override
    public TaskExtraMeta getExtraMeta() {
        return null;
    }
}
