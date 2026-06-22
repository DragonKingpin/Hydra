package com.walnut.odin.formation.source;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.formation.GenericFormationGroup;

@Mapper
@IbatisDataAccessObject
public interface FormationGroupMapper {
    GenericFormationGroup selectByGuid( @Param( "guid" ) GUID guid );

    GenericFormationGroup selectByIdentifier( @Param( "identifier" ) String identifier );

    int insert( @Param( "group" ) GenericFormationGroup group );
}
