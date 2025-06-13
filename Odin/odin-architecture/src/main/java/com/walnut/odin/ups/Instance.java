package com.walnut.odin.ups;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.marshal.KernelTaskScheduleCycle;
import com.pinecone.hydra.task.marshal.KernelTaskScheduleType;

import java.time.LocalDateTime;

public interface Instance extends Pinenut {

    void setGuid( GUID guid );

    void setAffiliatedTaskGuid( GUID affiliatedTaskGuid );

    void setInstanceName( String instanceName );

    void setBusinessTime( LocalDateTime businessTime );

    void setPriority( int priority );

    void setActuallyPriority( int actuallyPriority );

    void setInstanceStatus( TaskInstanceStatus instanceStatus );

    void setTaskType( String taskType );

    void setRunCount( int runCount );

    void setDryRun( boolean dryRun );

    void setKernelScheduleCycle( KernelTaskScheduleCycle kernelScheduleCycle );

    void setKernelScheduleType ( KernelTaskScheduleType kernelScheduleType ) ;

    void setLastStartTime ( LocalDateTime lastStartTime );

    void setLastEndTime ( LocalDateTime lastEndTime );

    void setCreateTime ( LocalDateTime createTime );

    void setUpdateTime ( LocalDateTime updateTime );

    TaskInstrument getTaskInstrument();

    String getRunStatus ();

    int getKernelScheduleCycleCode () ;

    int getKernelScheduleTypeCode () ;

    GUID getGuid();

    GUID getAffiliatedTaskGuid();

    String getInstanceName();

    LocalDateTime getBusinessTime ();

    short getPriority();

    short getActuallyPriority();

    TaskInstanceStatus getInstanceStatus ();

    String getTaskType ();

    int getRunCount ();

    boolean isDryRun() ;

    KernelTaskScheduleCycle getKernelScheduleCycle ();

    KernelTaskScheduleType getKernelScheduleType ();

    LocalDateTime getLastStartTime ();

    LocalDateTime getLastEndTime ();

    LocalDateTime getCreateTime ();

    LocalDateTime getUpdateTime ();
}
