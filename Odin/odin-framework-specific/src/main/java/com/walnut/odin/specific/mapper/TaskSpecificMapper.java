package com.walnut.odin.specific.mapper;

import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.specific.digest.GenericTaskInstanceSpecificDigest;
import com.walnut.odin.specific.digest.GenericTaskSpecificDigest;
import com.walnut.odin.specific.digest.TaskInstanceSpecificDigest;
import com.walnut.odin.specific.digest.TaskInstanceSpecificDigestQuery;
import com.walnut.odin.specific.digest.TaskSpecificDigest;
import com.walnut.odin.specific.digest.TaskSpecificDigestQuery;
import com.walnut.odin.specific.source.TaskSpecificManipulator;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface TaskSpecificMapper extends TaskSpecificManipulator {

    @Override
    long countTaskSpecificDigests( @Param( "query" ) TaskSpecificDigestQuery query );

    List<GenericTaskSpecificDigest> fetchTaskSpecificDigests0( @Param( "query" ) TaskSpecificDigestQuery query );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<TaskSpecificDigest> fetchTaskSpecificDigests( TaskSpecificDigestQuery query ) {
        return (List) this.fetchTaskSpecificDigests0( query );
    }

    List<GenericTaskSpecificDigest> fetchTaskSpecificDigestsByGuids0( @Param( "taskGuids" ) Collection<GUID> taskGuids );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<TaskSpecificDigest> fetchTaskSpecificDigestsByGuids( Collection<GUID> taskGuids ) {
        return (List) this.fetchTaskSpecificDigestsByGuids0( taskGuids );
    }

    @Override
    long countTaskInstanceSpecificDigests( @Param( "query" ) TaskInstanceSpecificDigestQuery query );

    List<GenericTaskInstanceSpecificDigest> fetchTaskInstanceSpecificDigests0(
            @Param( "query" ) TaskInstanceSpecificDigestQuery query
    );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<TaskInstanceSpecificDigest> fetchTaskInstanceSpecificDigests( TaskInstanceSpecificDigestQuery query ) {
        return (List) this.fetchTaskInstanceSpecificDigests0( query );
    }

    @Override
    String selectTaskProjectGuid( @Param( "taskGuid" ) GUID taskGuid );

    @Override
    int updateTaskProjectGuid( @Param( "taskGuid" ) GUID taskGuid, @Param( "projectGuid" ) GUID projectGuid );
}
