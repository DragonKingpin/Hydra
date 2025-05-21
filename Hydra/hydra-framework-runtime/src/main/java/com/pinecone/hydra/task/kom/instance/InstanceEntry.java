package com.pinecone.hydra.task.kom.instance;

import java.time.LocalDateTime;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.TaskInstanceMeta;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.EntryNode;
import com.pinecone.hydra.task.marshal.KernelTaskScheduleCycle;
import com.pinecone.hydra.task.marshal.KernelTaskScheduleType;

public interface InstanceEntry extends TaskInstanceMeta, EntryNode {

     @Override
     default String getName() {
          return this.getInstanceName();
     }

     void setGuid ( GUID guid );

     void setAffiliatedTaskGuid ( GUID affiliatedTaskGuid );

     void setInstanceName ( String instanceName );

     void setBusinessTime ( LocalDateTime businessTime );

     void setPriority ( int priority );

     void setActuallyPriority ( int actuallyPriority );

     void setInstanceStatus ( TaskInstanceStatus instanceStatus );

     void setTaskType ( String taskType );

     void setRunCount ( int runCount );

     void setDryRun ( boolean dryRun );

     void setKernelScheduleCycle ( KernelTaskScheduleCycle kernelScheduleCycle ) ;

     void setKernelScheduleType ( KernelTaskScheduleType kernelScheduleType ) ;

     void setLastStartTime ( LocalDateTime lastStartTime );

     void setLastEndTime ( LocalDateTime lastEndTime );

     void setCreateTime ( LocalDateTime createTime );

     void setUpdateTime ( LocalDateTime updateTime );

     TaskInstrument getTaskInstrument();



     String getRunStatus ();

     int getKernelScheduleCycleCode () ;

     int getKernelScheduleTypeCode () ;
}
