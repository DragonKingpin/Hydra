package com.pinecone.hydra.service;

import com.pinecone.framework.util.name.Namespace;

import java.util.Map;

public interface Service extends ServiceFamilyMeta {
    String getName();        // Service Name, e.g. WpnService

    String getType();

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

    Map<String, Object> getMetaDataScope();
}
