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

    long countEnabledByFormationGuid( @Param( "formationGuid" ) GUID formationGuid );

    List<GenericGroupTask> fetchEnabledByFormationGuid0( @Param( "formationGuid" ) GUID formationGuid );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<GroupTaskEntry> fetchEnabledByFormationGuid( GUID formationGuid ) {
        return (List) this.fetchEnabledByFormationGuid0( formationGuid );
    }
}
