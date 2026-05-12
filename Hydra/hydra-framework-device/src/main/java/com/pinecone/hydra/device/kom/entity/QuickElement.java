package com.pinecone.hydra.device.kom.entity;

public interface QuickElement extends DeviceElement {

    String getTypeName();// e.g. Script, POD

    void setTypeName(String typeName);


    @Override
    default QuickElement evinceQuickElement() {
        return this;
    }
}
