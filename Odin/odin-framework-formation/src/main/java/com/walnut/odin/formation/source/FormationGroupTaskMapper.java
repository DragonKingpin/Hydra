package com.walnut.odin.formation.source;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.formation.entity.GroupTaskEntry;
import com.walnut.odin.formation.entity.GenericGroupTask;

@Mapper
@IbatisDataAccessObject
public interface FormationGroupTaskMapper extends GroupTaskManipulator {
    int insert( @Param( "task" ) GroupTaskEntry task );

    GenericGroupTask selectByGuid( @Param( "guid" ) GUID guid );

    GenericGroupTask selectByFormationGuidAndTaskGuid(
            @Param( "formationGuid" ) GUID formationGuid,
            @Param( "taskGuid" ) GUID taskGuid );

    long countEnabledByFormationGuid( @Param( "formationGuid" ) GUID formationGuid );

    List<GenericGroupTask> fetchEnabledByFormationGuid0( @Param( "formationGuid" ) GUID formationGuid );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<GroupTaskEntry> fetchEnabledByFormationGuid( GUID formationGuid ) {
        return (List) this.fetchEnabledByFormationGuid0( formationGuid );
    }

    long countByFormationGuid(
            @Param( "formationGuid" ) GUID formationGuid,
            @Param( "enable" ) Boolean enable );

    long countByFormationGuidWithFilters(
            @Param( "formationGuid" ) GUID formationGuid,
            @Param( "enable" ) Boolean enable,
            @Param( "taskKeyword" ) String taskKeyword,
            @Param( "scheduleType" ) String scheduleType );

    @Override
    default long countByFormationGuid( GUID formationGuid, Boolean enable, String taskKeyword, String scheduleType ) {
        return this.countByFormationGuidWithFilters( formationGuid, enable, taskKeyword, scheduleType );
    }

    List<GenericGroupTask> listByFormationGuid0(
            @Param( "formationGuid" ) GUID formationGuid,
            @Param( "enable" ) Boolean enable );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<GroupTaskEntry> listByFormationGuid( GUID formationGuid, Boolean enable ) {
        return (List)this.listByFormationGuid0( formationGuid, enable );
    }

    List<GenericGroupTask> pageByFormationGuid0(
            @Param( "formationGuid" ) GUID formationGuid,
            @Param( "enable" ) Boolean enable,
            @Param( "offset" ) long offset,
            @Param( "limit" ) long limit );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<GroupTaskEntry> pageByFormationGuid( GUID formationGuid, Boolean enable, long offset, long limit ) {
        return (List)this.pageByFormationGuid0( formationGuid, enable, offset, limit );
    }

    List<GenericGroupTask> pageByFormationGuidWithFilters0(
            @Param( "formationGuid" ) GUID formationGuid,
            @Param( "enable" ) Boolean enable,
            @Param( "taskKeyword" ) String taskKeyword,
            @Param( "scheduleType" ) String scheduleType,
            @Param( "offset" ) long offset,
            @Param( "limit" ) long limit );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<GroupTaskEntry> pageByFormationGuid(
            GUID formationGuid,
            Boolean enable,
            String taskKeyword,
            String scheduleType,
            long offset,
            long limit ) {
        return (List)this.pageByFormationGuidWithFilters0(
                formationGuid,
                enable,
                taskKeyword,
                scheduleType,
                offset,
                limit
        );
    }

    int updateEnable( @Param( "guid" ) GUID guid, @Param( "enable" ) boolean enable );

    int removeByGuid( @Param( "guid" ) GUID guid );

    int removeByFormationGuids( @Param( "formationGuids" ) List<GUID> formationGuids );
}
