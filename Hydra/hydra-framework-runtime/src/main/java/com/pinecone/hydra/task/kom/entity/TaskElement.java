package com.pinecone.hydra.task.kom.entity;

import com.pinecone.hydra.task.TaskExtraMeta;
import com.pinecone.hydra.task.marshal.KernelTaskScheduleCycle;
import com.pinecone.hydra.task.marshal.KernelTaskScheduleType;

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


    KernelTaskScheduleCycle getScheduleCycle();
    void setScheduleCycle ( KernelTaskScheduleCycle kernelScheduleCycle ) ;

    KernelTaskScheduleType getScheduleType();
    void setScheduleType ( KernelTaskScheduleType kernelScheduleType ) ;


    boolean isDryRun() ;
    void setDryRun( boolean dryRun ) ;

    boolean isManual() ;
    void setManual( boolean manual ) ;

    String getScheduleCron();
    void setScheduleCron( String scheduleCron ) ;

    int getScheduleCycleCode();
    void setScheduleCycleCode ( int code ) ;

    int getScheduleTypeCode() ;
    void setScheduleTypeCode( int scheduleTypeCode ) ;

    boolean isEnable() ;
    void setEnable( boolean enable ) ;



    TaskExtraMeta getExtraMeta();

}