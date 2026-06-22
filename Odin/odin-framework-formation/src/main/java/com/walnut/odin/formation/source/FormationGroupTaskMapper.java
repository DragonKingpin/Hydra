package com.walnut.odin.formation.source;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.formation.GenericFormationGroupTask;

@Mapper
@IbatisDataAccessObject
public interface FormationGroupTaskMapper {
    int insert( @Param( "task" ) GenericFormationGroupTask task );

    long countEnabledByFormationGuid( @Param( "formationGuid" ) GUID formationGuid );

    List<GenericFormationGroupTask> fetchEnabledByFormationGuid( @Param( "formationGuid" ) GUID formationGuid );
}
