package com.pinecone.hydra.task.kom.entity;

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

}