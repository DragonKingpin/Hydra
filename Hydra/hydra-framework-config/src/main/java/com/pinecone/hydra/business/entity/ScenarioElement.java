package com.pinecone.hydra.business.entity;

public interface ScenarioElement extends FolderElement {

    String getScenarioCode();

    void setScenarioCode( String szScenarioCode );

    String getScenarioType();

    void setScenarioType( String szScenarioType );

    Short getPriority();

    void setPriority( Short nPriority );

    @Override
    default ScenarioElement evinceScenarioElement() {
        return this;
    }
}
