package com.walnut.odin.task.mapper;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.instance.source.InstanceNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.conduct.entity.InstanceAtlasNode;
import com.walnut.odin.conduct.schedule.entity.DependencyBlockage;

@IbatisDataAccessObject
public interface InstanceAtlasNodeMapper extends InstanceNodeManipulator {

    void insert( InstanceAtlasNode instanceAtlasNode );

    List<DependencyBlockage> fetchDependencyBlockages(
            @Param( "instanceGuids" ) Collection<GUID> instanceGuids,
            @Param( "finishedStatus" ) String finishedStatus
    );

}
