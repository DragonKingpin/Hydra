package com.walnut.odin.formation.source;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.formation.plan.GenericFormationPage;

@Mapper
@IbatisDataAccessObject
public interface FormationRunPageMapper {
    int insert( @Param( "page" ) GenericFormationPage page );

    List<GenericFormationPage> fetchPendingPages( @Param( "runGuid" ) GUID runGuid, @Param( "limit" ) long limit );

    int claimPage(
            @Param( "id" ) long id,
            @Param( "claimOwner" ) String claimOwner,
            @Param( "claimToken" ) GUID claimToken,
            @Param( "leaseSeconds" ) long leaseSeconds );

    int markRunning( @Param( "id" ) long id );

    int markCompleted( @Param( "id" ) long id );
}
