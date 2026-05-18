package com.pinecone.hydra.business.entity;

public class GenericScenarioElement extends ArchFolderElement implements ScenarioElement {

    protected String mszScenarioCode;
    protected String mszScenarioType;
    protected Short  mnPriority;

    public GenericScenarioElement() {
        this.setType( "Scenario" );
    }

    @Override
    public String getScenarioCode() {
        return this.mszScenarioCode;
    }

    @Override
    public void setScenarioCode( String szScenarioCode ) {
        this.mszScenarioCode = szScenarioCode;
    }

    @Override
    public String getScenarioType() {
        return this.mszScenarioType;
    }

    @Override
    public void setScenarioType( String szScenarioType ) {
        this.mszScenarioType = szScenarioType;
    }

    @Override
    public Short getPriority() {
        return this.mnPriority;
    }

    @Override
    public void setPriority( Short nPriority ) {
        this.mnPriority = nPriority;
    }
}
