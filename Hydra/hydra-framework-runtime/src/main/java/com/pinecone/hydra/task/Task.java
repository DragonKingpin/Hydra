package com.pinecone.hydra.task;

import java.util.Map;

import com.pinecone.framework.util.name.Namespace;

public interface Task extends TaskFamilyMeta {

    String getName();        // Service Name, e.g. WpnService

    String getDisplayName(); // Service Long Name, e.g. Windows Push Notification System Service

    String getDescription();

    String getFullName();

    String getScenario() ;

    String getMarshallingArchitecture() ;

    String getExtraInformation() ;

    short getPriority();

    Map<String, Object> getMetaDataScope();

}
