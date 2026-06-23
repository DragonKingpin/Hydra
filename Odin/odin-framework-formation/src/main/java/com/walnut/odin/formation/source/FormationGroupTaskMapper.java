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

    List<GenericGroupTask> listByFormationGuid0(
            @Param( "formationGuid" ) GUID formationGuid,
            @Param( "enable" ) Boolean enable );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<GroupTaskEntry> listByFormationGuid( GUID formationGuid, Boolean enable ) {
        return (List)this.listByFormationGuid0( formationGuid, enable );
    }

    int updateEnable( @Param( "guid" ) GUID guid, @Param( "enable" ) boolean enable );

    int removeByGuid( @Param( "guid" ) GUID guid );
}
