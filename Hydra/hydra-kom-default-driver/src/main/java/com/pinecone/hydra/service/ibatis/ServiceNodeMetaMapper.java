package com.pinecone.hydra.service.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.ServiceFamilyNode;
import com.pinecone.hydra.service.kom.entity.GenericCommonMeta;
import com.pinecone.hydra.service.kom.entity.Namespace;
import com.pinecone.hydra.service.kom.source.NodeMetaManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@IbatisDataAccessObject
public interface ServiceNodeMetaMapper extends NodeMetaManipulator {

    @Override
    void insert( ServiceFamilyNode node );

    @Override
    void insertNS( Namespace node );

    @Override
    void remove( @Param("guid")GUID guid );

    @Override
    GenericCommonMeta getNodeCommonMeta( @Param("guid") GUID guid );

    @Override
    void update( ServiceFamilyNode node );

    void updateScenario( @Param("scenario") String scenario, @Param("guid") GUID guid );

    void updatePrimaryImplLang( @Param("primaryImplLang") String primaryImplLang, @Param("guid") GUID guid );

    void updateExtraInformation( @Param("extraInformation") String extraInformation, @Param("guid") GUID guid );

    void updateLevel( @Param("level") String level, @Param("guid") GUID guid );

    void updateDescription( @Param("description") String description, @Param("guid") GUID guid );

}
