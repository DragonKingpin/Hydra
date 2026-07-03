package com.walnut.odin.task.mapper;

import java.util.Collection;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.patrol.PatrolWatchdogLog;
import org.apache.ibatis.annotations.Param;

@IbatisDataAccessObject
public interface PatrolWatchdogLogMapper extends Pinenut {

    void insert( PatrolWatchdogLog log );

    long count(
            @Param( "ruleCode" ) String ruleCode,
            @Param( "patrolState" ) String patrolState,
            @Param( "severity" ) String severity,
            @Param( "actionState" ) String actionState
    );

    List<PatrolWatchdogLog> page(
            @Param( "ruleCode" ) String ruleCode,
            @Param( "patrolState" ) String patrolState,
            @Param( "severity" ) String severity,
            @Param( "actionState" ) String actionState,
            @Param( "offset" ) long offset,
            @Param( "limit" ) long limit
    );

    PatrolWatchdogLog latest();

    int deleteByTaskGuids( @Param( "taskGuids" ) Collection<GUID> taskGuids );
}
