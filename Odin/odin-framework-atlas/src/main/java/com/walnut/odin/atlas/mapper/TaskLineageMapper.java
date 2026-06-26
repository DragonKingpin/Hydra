package com.walnut.odin.atlas.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

@IbatisDataAccessObject
public interface TaskLineageMapper {

    void addDependency( @Param("taskGuid") GUID taskGuid, @Param("parentTaskGuid") GUID parentTaskGuid );

    void removeDependency( @Param("taskGuid") GUID taskGuid, @Param("parentTaskGuid") GUID parentTaskGuid );

    List<GUID> fetchParentTaskGuids( @Param("taskGuid") GUID taskGuid );

    List<GUID> fetchChildTaskGuids( @Param("taskGuid") GUID taskGuid );

    long countParents( @Param("taskGuid") GUID taskGuid );

    long countChildren( @Param("taskGuid") GUID taskGuid );

    int deleteByTaskGuids( @Param( "taskGuids" ) List<GUID> taskGuids );

}
