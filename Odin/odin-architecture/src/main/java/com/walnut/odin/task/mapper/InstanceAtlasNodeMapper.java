package com.walnut.odin.task.mapper;

import java.util.Collection;
import java.util.List;
import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.instance.source.InstanceNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.conduct.entity.InstanceAtlasNode;
import com.walnut.odin.conduct.schedule.entity.DependencyBlockage;

@IbatisDataAccessObject
public interface InstanceAtlasNodeMapper extends InstanceNodeManipulator {

    void insert( InstanceAtlasNode instanceAtlasNode );

    void updateSourceByGuid( @Param( "guid" ) GUID guid, @Param( "source" ) boolean source );

    InstanceAtlasNode queryByInstanceGuid( @Param( "instanceGuid" ) GUID instanceGuid );

    InstanceAtlasNode queryByTaskGuidAndExpectTime(
            @Param( "taskGuid" ) GUID taskGuid,
            @Param( "expectTime" ) LocalDateTime expectTime
    );

    InstanceAtlasNode queryByTaskGuidAndBusinessTime(
            @Param( "taskGuid" ) GUID taskGuid,
            @Param( "businessTime" ) LocalDateTime businessTime
    );

    List<DependencyBlockage> fetchDependencyBlockages(
            @Param( "instanceGuids" ) Collection<GUID> instanceGuids,
            @Param( "finishedStatus" ) String finishedStatus
    );

}
