package com.walnut.odin.task.mapper;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.conduct.entity.InstanceLineageAdjacent;
import com.walnut.odin.conduct.schedule.entity.DependencyBlockage;

@IbatisDataAccessObject
public interface InstanceLineageAdjacentMapper {

    void insertIgnore( InstanceLineageAdjacent instanceLineageAdjacent );

    int deleteByInstanceGuid( @Param( "instanceGuid" ) GUID instanceGuid );

    int deleteByInstanceGuids( @Param( "instanceGuids" ) Collection<GUID> instanceGuids );

    long countByInstanceGuidAndParentInstanceGuid(
            @Param( "instanceGuid" ) GUID instanceGuid,
            @Param( "parentInstanceGuid" ) GUID parentInstanceGuid
    );

    List<InstanceLineageAdjacent> fetchParentsByInstanceGuid( @Param( "instanceGuid" ) GUID instanceGuid );

    List<InstanceLineageAdjacent> fetchChildrenByInstanceGuid( @Param( "instanceGuid" ) GUID instanceGuid );

    List<DependencyBlockage> fetchDependencyBlockages(
            @Param( "instanceGuids" ) Collection<GUID> instanceGuids,
            @Param( "finishedStatus" ) String finishedStatus
    );

}
