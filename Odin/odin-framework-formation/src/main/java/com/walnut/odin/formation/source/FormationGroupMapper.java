package com.walnut.odin.formation.source;

import java.util.List;

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

    int update( @Param( "group" ) GroupEntry group );

    int updateEnable( @Param( "guid" ) GUID guid, @Param( "enable" ) boolean enable );

    long countGroups(
            @Param( "keyword" ) String keyword,
            @Param( "strategyType" ) String strategyType,
            @Param( "enable" ) Boolean enable );

    List<GenericGroup> pageGroups0(
            @Param( "keyword" ) String keyword,
            @Param( "strategyType" ) String strategyType,
            @Param( "enable" ) Boolean enable,
            @Param( "offset" ) long offset,
            @Param( "limit" ) long limit );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<GroupEntry> pageGroups( String keyword, String strategyType, Boolean enable, long offset, long limit ) {
        return (List)this.pageGroups0( keyword, strategyType, enable, offset, limit );
    }
}
