package com.pinecone.hydra.task.kom.entity;

public interface TaskElement extends ServoElement {
    @Override
    default TaskElement evinceTaskElement() {
        return this;
    }

    String getServiceType();

    void setServiceType(String serviceType);
}