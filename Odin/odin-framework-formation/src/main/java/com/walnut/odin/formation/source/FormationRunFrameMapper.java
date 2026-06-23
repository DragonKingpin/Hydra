package com.walnut.odin.formation.source;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
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

    long countFrames(
            @Param( "runGuid" ) GUID runGuid,
            @Param( "frameStatus" ) String frameStatus );

    List<GenericFormationFrame> pageFrames0(
            @Param( "runGuid" ) GUID runGuid,
            @Param( "frameStatus" ) String frameStatus,
            @Param( "offset" ) long offset,
            @Param( "limit" ) long limit );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<FormationFrame> pageFrames( GUID runGuid, String frameStatus, long offset, long limit ) {
        return (List)this.pageFrames0( runGuid, frameStatus, offset, limit );
    }

    int markSubmitted(
            @Param( "guid" ) GUID guid,
            @Param( "instanceGuid" ) GUID instanceGuid );

    int markFailed(
            @Param( "guid" ) GUID guid,
            @Param( "errorCause" ) String errorCause );
}
