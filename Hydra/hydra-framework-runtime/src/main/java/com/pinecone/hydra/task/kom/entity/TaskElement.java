package com.pinecone.hydra.task.kom.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.TaskExtraMeta;
import com.pinecone.hydra.task.marshal.TaskScheduleCycle;
import com.pinecone.hydra.task.marshal.TaskScheduleType;

import java.time.LocalDateTime;

public interface TaskElement extends ElementNode {

    @Override
    default TaskElement evinceTaskElement() {
        return this;
    }

    String getImagePath();
    void setImagePath( String path );

    String getType();
    void setType( String type );

    String getDeploymentMethod();
    void setDeploymentMethod( String deploymentMethod );

    String getResourceType();
    void setResourceType( String resourceType );

    short getPriority();
    void setPriority( int priority );

    short getActuallyPriority();
    void setActuallyPriority( int priority );


    TaskScheduleCycle getScheduleCycle();
    void setScheduleCycle ( TaskScheduleCycle kernelScheduleCycle ) ;

    TaskScheduleType getScheduleType();
    void setScheduleType ( TaskScheduleType kernelScheduleType ) ;


    boolean isDryRun() ;
    void setDryRun( boolean dryRun ) ;

    String getScheduleCron();
    void setScheduleCron( String scheduleCron ) ;

    boolean isEnable() ;
    void setEnable( boolean enable ) ;

    LocalDateTime getScheduleStartTime();
    void setScheduleStartTime( LocalDateTime scheduleStartTime );

    LocalDateTime getScheduleEndTime();
    void setScheduleEndTime( LocalDateTime scheduleEndTime );

    LocalDateTime getNextScheduleTime();
    void setNextScheduleTime( LocalDateTime nextScheduleTime );


    String getProcessorName();
    void setProcessorName( String processorName );


    TaskExtraMeta getExtraMeta();

}