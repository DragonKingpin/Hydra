package com.walnut.odin.formation.source;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.formation.GenericFormationRun;

@Mapper
@IbatisDataAccessObject
public interface FormationRunMapper {
    int insert( @Param( "run" ) GenericFormationRun run );

    GenericFormationRun selectByGuid( @Param( "guid" ) GUID guid );

    List<GenericFormationRun> fetchRunnableRuns( @Param( "limit" ) int limit );

    int updateStatus( @Param( "guid" ) GUID guid, @Param( "status" ) String status );

    int markRunning( @Param( "guid" ) GUID guid );

    int increaseSubmitted( @Param( "guid" ) GUID guid );

    int increaseCompleted( @Param( "guid" ) GUID guid );

    int increaseFailed( @Param( "guid" ) GUID guid );
}
