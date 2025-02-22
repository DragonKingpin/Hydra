package com.pinecone.hydra.account.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.account.entity.GenericDomain;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.account.entity.Domain;

import java.util.List;

public interface DomainNodeManipulator extends GUIDNameManipulator {
    void insert(Domain domain);

    void remove(GUID domainGuid);

    Domain queryDomain(GUID domainGuid );

    List<GenericDomain> queryAllDomain();

    String queryDomainNameByGuid(GUID domainGuid);

    void update(Domain domain);
}
