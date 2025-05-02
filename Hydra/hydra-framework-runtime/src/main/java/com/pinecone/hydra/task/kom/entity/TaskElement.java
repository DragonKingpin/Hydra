package com.pinecone.hydra.task.kom.entity;

import com.pinecone.hydra.task.TaskExtraMeta;

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
    void setPriority( short priority );

    short getActuallyPriority();
    void setActuallyPriority( short priority );



    boolean isDryRun() ;
    void setDryRun( boolean dryRun ) ;

    int getScheduleTypeCode() ;
    void setScheduleTypeCode( int scheduleTypeCode ) ;

    boolean isEnable() ;
    void setEnable( boolean enable ) ;



    TaskExtraMeta getExtraMeta();

}