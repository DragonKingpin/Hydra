package com.pinecone.hydra.account.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.account.entity.Domain;
import com.pinecone.hydra.account.entity.GenericDomain;
import com.pinecone.hydra.account.source.DomainNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@IbatisDataAccessObject
public interface DomainNodeMapper extends DomainNodeManipulator {

    void insert(Domain domain);

    void remove(@Param("domainGuid") GUID domainGuid);

    GenericDomain queryDomain0(@Param("domainGuid") GUID domainGuid );

    default GenericDomain queryDomain(GUID domainGuid ){
        GenericDomain domain = this.queryDomain0(domainGuid);
        if( domain != null ) {
            domain.setDomainNodeManipulator( this );
        }
        return domain;
    }

    List<GUID > getGuidsByName(@Param("name") String name );

    List<GUID > getGuidsByNameID(@Param("name") String name, @Param("guid") GUID guid );

    List<GenericDomain> queryAllDomain();

    String queryDomainNameByGuid(@Param("domainGuid") GUID domainGuid);

    void update(Domain domain);
}
