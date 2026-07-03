package com.walnut.odin.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.project.GenericTaskProject;
import com.walnut.odin.project.TaskProject;
import com.walnut.odin.project.source.TaskProjectManipulator;

@Mapper
@IbatisDataAccessObject
public interface TaskProjectMapper extends TaskProjectManipulator {

    @Override
    void insert( TaskProject project );

    @Override
    void update( TaskProject project );

    @Override
    GenericTaskProject get( @Param( "guid" ) GUID guid );

    @Override
    GenericTaskProject queryByName( @Param( "name" ) String szName );

    List<GenericTaskProject> fetchByBizTreeGuid0( @Param( "bizTreeGuid" ) GUID bizTreeGuid );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<TaskProject> fetchByBizTreeGuid( GUID bizTreeGuid ) {
        return (List) this.fetchByBizTreeGuid0( bizTreeGuid );
    }

    @Override
    long count();

    List<GenericTaskProject> fetch0( @Param( "offset" ) long nOffset, @Param( "pageSize" ) long nPageSize );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<TaskProject> fetch( long nOffset, long nPageSize ) {
        return (List) this.fetch0( nOffset, nPageSize );
    }

    long countFiltered( @Param( "keyword" ) String szKeyword, @Param( "enable" ) Boolean enable );

    @Override
    default long count( String szKeyword, Boolean enable ) {
        return this.countFiltered( szKeyword, enable );
    }

    List<GenericTaskProject> fetchFiltered0(
            @Param( "offset" ) long nOffset,
            @Param( "pageSize" ) long nPageSize,
            @Param( "keyword" ) String szKeyword,
            @Param( "enable" ) Boolean enable );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<TaskProject> fetch( long nOffset, long nPageSize, String szKeyword, Boolean enable ) {
        return (List) this.fetchFiltered0( nOffset, nPageSize, szKeyword, enable );
    }

    @Override
    void updateEnable( @Param( "guid" ) GUID guid, @Param( "enable" ) boolean bEnable );

    @Override
    void remove( @Param( "guid" ) GUID guid );

}
