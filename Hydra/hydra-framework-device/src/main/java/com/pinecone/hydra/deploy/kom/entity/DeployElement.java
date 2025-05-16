package com.pinecone.hydra.deploy.kom.entity;

import com.pinecone.hydra.deploy.DeployExtraMeta;

public interface DeployElement extends ElementNode {

    @Override
    default DeployElement evinceDeployElement() {
        return this;
    }

    boolean isEnable() ;

    void setEnable( boolean enable ) ;

    DeployExtraMeta getExtraMeta();

    void setExtraMeta(DeployExtraMeta extraMeta);

    DeployElement getAffiliateDeployment();


    void addChild(ElementNode child);
}