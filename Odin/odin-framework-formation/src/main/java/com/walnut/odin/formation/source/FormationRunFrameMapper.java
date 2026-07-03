package com.walnut.odin.formation.source;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.formation.dto.FormationFrameQuery;
import com.walnut.odin.formation.plan.FormationFrame;
import com.walnut.odin.formation.plan.GenericFormationFrame;

@Mapper
@IbatisDataAccessObject
public interface FormationRunFrameMapper extends FrameManipulator {
    int insert( @Param( "frame" ) FormationFrame frame );

    List<GenericFormationFrame> fetchPendingFramesByPage0(
            @Param( "runGuid" ) GUID runGuid,
            @Param( "pageNo" ) long pageNo );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<FormationFrame> fetchPendingFramesByPage( GUID runGuid, long pageNo ) {
        return (List) this.fetchPendingFramesByPage0( runGuid, pageNo );
    }

    List<GenericFormationFrame> fetchPendingFrames0(
            @Param( "runGuid" ) GUID runGuid,
            @Param( "limit" ) long limit );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<FormationFrame> fetchPendingFrames( GUID runGuid, long limit ) {
        return (List)this.fetchPendingFrames0( runGuid, limit );
    }

    int claimFrame(
            @Param( "guid" ) GUID guid,
            @Param( "claimOwner" ) String claimOwner,
            @Param( "claimToken" ) GUID claimToken,
            @Param( "leaseSeconds" ) long leaseSeconds );

    long countFrames0(
            @Param( "query" ) FormationFrameQuery query );

    @Override
    default long countFrames( FormationFrameQuery query ) {
        return this.countFrames0( query );
    }

    List<GenericFormationFrame> pageFrames0(
            @Param( "query" ) FormationFrameQuery query,
            @Param( "offset" ) long offset,
            @Param( "limit" ) long limit );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<FormationFrame> pageFrames( FormationFrameQuery query, long offset, long limit ) {
        return (List)this.pageFrames0( query, offset, limit );
    }

    int markSubmitted(
            @Param( "guid" ) GUID guid,
            @Param( "instanceGuid" ) GUID instanceGuid );

    int markCompleted(
            @Param( "guid" ) GUID guid );

    int markFailed(
            @Param( "guid" ) GUID guid,
            @Param( "errorCause" ) String errorCause );

    int markCompletedByInstanceGuid(
            @Param( "instanceGuid" ) GUID instanceGuid );

    int markFailedByInstanceGuid(
            @Param( "instanceGuid" ) GUID instanceGuid,
            @Param( "errorCause" ) String errorCause );

    int removeByFormationGuids( @Param( "formationGuids" ) List<GUID> formationGuids );
}
