package com.pinecone.hydra.account.entity;

import com.pinecone.hydra.account.source.DomainNodeManipulator;

public interface Domain extends FolderElement {
    String getDomainName();

    void setDomainName( String domainName );

    void save();

    void  delete();

    void setDomainNodeManipulator(DomainNodeManipulator domainNodeManipulator);
}
