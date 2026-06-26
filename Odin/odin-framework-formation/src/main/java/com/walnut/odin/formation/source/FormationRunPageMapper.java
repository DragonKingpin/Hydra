package com.walnut.odin.formation.source;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.formation.dto.FormationPageQuery;
import com.walnut.odin.formation.plan.FormationPage;
import com.walnut.odin.formation.plan.GenericFormationPage;

@Mapper
@IbatisDataAccessObject
public interface FormationRunPageMapper extends PageManipulator {
    int insert( @Param( "page" ) FormationPage page );

    List<GenericFormationPage> fetchPendingPages0( @Param( "runGuid" ) GUID runGuid, @Param( "limit" ) long limit );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<FormationPage> fetchPendingPages( GUID runGuid, long limit ) {
        return (List) this.fetchPendingPages0( runGuid, limit );
    }

    List<GenericFormationPage> listByRunGuid0( @Param( "runGuid" ) GUID runGuid );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<FormationPage> listByRunGuid( GUID runGuid ) {
        return (List)this.listByRunGuid0( runGuid );
    }

    long countPages( @Param( "query" ) FormationPageQuery query );

    List<GenericFormationPage> pagePages0(
            @Param( "query" ) FormationPageQuery query,
            @Param( "offset" ) long offset,
            @Param( "limit" ) long limit );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<FormationPage> pagePages( FormationPageQuery query, long offset, long limit ) {
        return (List)this.pagePages0( query, offset, limit );
    }

    int claimPage(
            @Param( "id" ) long id,
            @Param( "claimOwner" ) String claimOwner,
            @Param( "claimToken" ) GUID claimToken,
            @Param( "leaseSeconds" ) long leaseSeconds );

    int markRunning( @Param( "id" ) long id );

    int markCompleted( @Param( "id" ) long id );

    int removeByFormationGuids( @Param( "formationGuids" ) List<GUID> formationGuids );
}
