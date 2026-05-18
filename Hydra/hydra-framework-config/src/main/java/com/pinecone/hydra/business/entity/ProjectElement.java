package com.pinecone.hydra.business.entity;

public interface ProjectElement extends ElementNode {

    String getProjectCode();

    void setProjectCode( String szProjectCode );

    String getProjectStatus();

    void setProjectStatus( String szProjectStatus );

    @Override
    default ProjectElement evinceProjectElement() {
        return this;
    }
}
