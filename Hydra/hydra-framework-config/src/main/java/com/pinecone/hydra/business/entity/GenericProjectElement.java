package com.pinecone.hydra.business.entity;

public class GenericProjectElement extends ArchElementNode implements ProjectElement {

    protected String mszProjectCode;
    protected String mszProjectStatus = "Active";

    public GenericProjectElement() {
        this.setType( "Project" );
    }

    @Override
    public String getProjectCode() {
        return this.mszProjectCode;
    }

    @Override
    public void setProjectCode( String szProjectCode ) {
        this.mszProjectCode = szProjectCode;
    }

    @Override
    public String getProjectStatus() {
        return this.mszProjectStatus;
    }

    @Override
    public void setProjectStatus( String szProjectStatus ) {
        this.mszProjectStatus = szProjectStatus;
    }
}
