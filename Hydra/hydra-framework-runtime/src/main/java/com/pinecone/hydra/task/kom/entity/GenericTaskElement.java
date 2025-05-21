package com.pinecone.hydra.task.kom.entity;

import java.util.Map;

import com.pinecone.framework.util.json.JSON;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.task.TaskExtraMeta;
import com.pinecone.hydra.task.kom.TaskInstrument;

public class GenericTaskElement extends ArchElementNode implements TaskElement {
    protected String        taskType;

    protected String        imagePath;

    protected String        resourceType;

    protected String        deploymentMethod;

    protected short         priority;

    protected short         actuallyPriority;

    protected boolean       dryRun;

    protected int           scheduleTypeCode;

    protected boolean       enable;


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
    public int getScheduleTypeCode() {
        return this.scheduleTypeCode;
    }

    @Override
    public void setScheduleTypeCode( int scheduleTypeCode ) {
        this.scheduleTypeCode = scheduleTypeCode;
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