package com.pinecone.hydra.task;

import com.pinecone.framework.util.name.Namespace;

public interface Task extends TaskFamilyMeta{
    String getName();        // Service Name, e.g. WpnService

    String getDisplayName(); // Service Long Name, e.g. Windows Push Notification System Service

    String getDescription();

    String getFullName();

    Namespace getGroupNamespace();

    String getGroupName();

    String getScenario() ;

    String getPrimaryImplLang() ;

    String getExtraInformation() ;

    String getLevel() ;

    Object getProcessImageObject();
}
