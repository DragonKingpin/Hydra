package com.walnut.odin.formation.source;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.formation.plan.GenericFormationFrame;

@Mapper
@IbatisDataAccessObject
public interface FormationRunFrameMapper {
    int insert( @Param( "frame" ) GenericFormationFrame frame );

    List<GenericFormationFrame> fetchPendingFramesByPage(
            @Param( "runGuid" ) GUID runGuid,
            @Param( "pageNo" ) long pageNo );

    int markSubmitted(
            @Param( "guid" ) GUID guid,
            @Param( "instanceGuid" ) GUID instanceGuid );

    int markFailed(
            @Param( "guid" ) GUID guid,
            @Param( "errorCause" ) String errorCause );
}
