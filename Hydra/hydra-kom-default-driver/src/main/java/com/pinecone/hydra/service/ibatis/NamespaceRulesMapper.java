package com.pinecone.hydra.service.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.GenericNamespaceRules;
import com.pinecone.hydra.service.kom.source.NamespaceRulesManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@IbatisDataAccessObject
public interface NamespaceRulesMapper extends NamespaceRulesManipulator {
    void insert(GenericNamespaceRules classificationRules);

    void remove(@Param("guid")GUID guid);

    GenericNamespaceRules getNamespaceRules(@Param("guid")GUID guid);

    void update(GenericNamespaceRules classificationRules);

    void updateScope( @Param("scope") String scope, @Param("guid") GUID guid );

    void updateName( @Param("name") String name, @Param("guid") GUID guid );

    void updateDescription( @Param("description") String description, @Param("guid") GUID guid );
}
