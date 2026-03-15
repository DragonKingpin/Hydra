package com.pinecone.hydra.task.kom.entity;

import java.util.Map;

import com.pinecone.framework.util.json.JSON;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.task.TaskExtraMeta;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.marshal.KernelTaskScheduleCycle;
import com.pinecone.hydra.task.marshal.KernelTaskScheduleType;

public class GenericTaskElement extends ArchElementNode implements TaskElement {
    protected String                   taskType;
    protected String                   imagePath;
    protected String                   resourceType;
    protected String                   deploymentMethod;

    protected short                    priority;
    protected short                    actuallyPriority;
    protected boolean                  dryRun;
    protected boolean                  manual;

    protected String                   scheduleCron;
    protected KernelTaskScheduleCycle  kernelScheduleCycle;
    protected KernelTaskScheduleType   kernelScheduleType;
    protected boolean                  enable;


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
    public boolean isManual() {
        return this.manual;
    }

    @Override
    public void setManual( boolean manual ) {
        this.manual = manual;
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
    public void setScheduleCycleCode ( int code ) {
        this.kernelScheduleCycle = KernelTaskScheduleCycle.getByCode( code );
    }

    @Override
    public int getScheduleCycleCode() {
        if ( this.kernelScheduleCycle == null ) {
            return KernelTaskScheduleCycle.Undefined.getCode();
        }

        return this.kernelScheduleCycle.getCode();
    }

    @Override
    public void setScheduleTypeCode ( int code ) {
        this.kernelScheduleType = KernelTaskScheduleType.getByCode( code );
    }

    @Override
    public int getScheduleTypeCode() {
        if ( this.kernelScheduleType == null ) {
            return KernelTaskScheduleType.Undefined.getCode();
        }

        return this.kernelScheduleType.getCode();
    }



    @Override
    public void setScheduleCycle ( KernelTaskScheduleCycle kernelScheduleCycle ) {
        this.kernelScheduleCycle = kernelScheduleCycle;
    }

    @Override
    public KernelTaskScheduleCycle getScheduleCycle() {
        return this.kernelScheduleCycle;
    }

    @Override
    public void setScheduleType ( KernelTaskScheduleType kernelScheduleType ) {
        this.kernelScheduleType = kernelScheduleType;
    }

    @Override
    public KernelTaskScheduleType getScheduleType() {
        return this.kernelScheduleType;
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
    public TaskExtraMeta getExtraMeta() {
        return null;
    }
}