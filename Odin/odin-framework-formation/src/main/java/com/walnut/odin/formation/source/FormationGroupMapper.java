package com.walnut.odin.formation.source;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.formation.entity.GroupEntry;
import com.walnut.odin.formation.entity.GenericGroup;

@Mapper
@IbatisDataAccessObject
public interface FormationGroupMapper extends GroupManipulator {
    GenericGroup selectByGuid( @Param( "guid" ) GUID guid );

    GenericGroup selectByIdentifier( @Param( "identifier" ) String identifier );

    int insert( @Param( "group" ) GroupEntry group );
}
