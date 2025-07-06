package com.pinecone.hydra.task;

import java.util.Map;

import com.pinecone.hydra.task.kom.entity.TaskElement;

public interface Task extends TaskFamilyMeta {

    TaskElement getTaskElement();

    String getName();        // Service Name, e.g. WpnService

    String getDisplayName(); // Service Long Name, e.g. Windows Push Notification System Service

    String getDescription();

    String getFullName();

    String getScenario() ;

    String getMarshallingArchitecture() ;

    String getExtraInformation() ;

    short getPriority();

    short getActuallyPriority();


    boolean isDryRun() ;

    int getScheduleTypeCode() ;

    boolean isEnable() ;


    Map<String, Object> getMetaDataScope();

}
