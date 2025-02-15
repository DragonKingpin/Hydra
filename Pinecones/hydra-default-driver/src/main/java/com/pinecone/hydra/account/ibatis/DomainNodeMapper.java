package com.pinecone.hydra.account.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.account.entity.Domain;
import com.pinecone.hydra.account.entity.Account;
import com.pinecone.hydra.account.entity.GenericDomain;
import com.pinecone.hydra.account.source.DomainNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@IbatisDataAccessObject
public interface DomainNodeMapper extends DomainNodeManipulator {

    @Insert("INSERT INTO `hydra_account_domain_node` (`domain_name`, `domin_guid`, `name`) VALUES (#{domainName}, #{guid}, #{name})")
    void insert(Domain domain);

    @Delete("DELETE FROM `hydra_account_domain_node` WHERE `domin_guid` = #{domainGuid}")
    void remove(GUID domainGuid);

    @Select("SELECT `id`, `domain_name` AS domainName, `domin_guid` AS guid, `name` FROM `hydra_account_domain_node` WHERE `domin_guid` = #{domainGuid}")
    GenericDomain queryDomain0(GUID domainGuid );

    default GenericDomain queryDomain(GUID domainGuid ){
        GenericDomain domain = this.queryDomain0(domainGuid);
        domain.setDomainNodeManipulator( this );
        return domain;
    }

    @Select("SELECT `domin_guid` FROM hydra_account_domain_node WHERE `name` = #{name}")
    List<GUID > getGuidsByName(String name );
    @Select("SELECT `domin_guid` FROM hydra_account_domain_node WHERE `name` = #{name} AND domin_guid = #{guid}")
    List<GUID > getGuidsByNameID(@Param("name") String name, @Param("guid") GUID guid );

    @Select("SELECT `id`, `domain_name` AS domainName, `domin_guid` AS guid, `name` FROM `hydra_account_domain_node`")
    List<GenericDomain> queryAllDomain();
    @Select("SELECT `name` AS domainName FROM `hydra_account_domain_node` WHERE `domin_guid` = #{domainGuid}")
    String queryDomainNameByGuid(GUID domainGuid);

    @Insert("UPDATE `hydra_account_domain_node` SET `domain_name` = #{domainName}, `name` = #{name} WHERE `domin_guid` = #{guid} ")
    void update(Domain domain);
}
