package com.walnut.odin.task.mapper;

import java.util.Collection;
import java.util.List;
import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.instance.source.InstanceNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.conduct.entity.InstanceLineageNode;
import com.walnut.odin.conduct.schedule.entity.DependencyBlockage;

@IbatisDataAccessObject
public interface InstanceLineageNodeMapper extends InstanceNodeManipulator {

    void insert( InstanceLineageNode instanceLineageNode );

    void updateSourceByGuid( @Param( "guid" ) GUID guid, @Param( "source" ) boolean source );

    InstanceLineageNode queryByInstanceGuid( @Param( "instanceGuid" ) GUID instanceGuid );

    InstanceLineageNode queryByTaskGuidAndExpectTime(
            @Param( "taskGuid" ) GUID taskGuid,
            @Param( "expectTime" ) LocalDateTime expectTime
    );

    InstanceLineageNode queryByTaskGuidAndBusinessTime(
            @Param( "taskGuid" ) GUID taskGuid,
            @Param( "businessTime" ) LocalDateTime businessTime
    );

    List<DependencyBlockage> fetchDependencyBlockages(
            @Param( "instanceGuids" ) Collection<GUID> instanceGuids,
            @Param( "finishedStatus" ) String finishedStatus
    );

}
