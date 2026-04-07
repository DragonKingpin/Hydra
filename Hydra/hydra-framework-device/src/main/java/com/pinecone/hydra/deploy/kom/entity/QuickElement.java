package com.pinecone.hydra.deploy.kom.entity;

public interface QuickElement extends DeployElement {

    String getTypeName();// e.g. Script, POD

    void setTypeName(String typeName);


    @Override
    default QuickElement evinceQuickElement() {
        return this;
    }
}
