package com.pinecone.hydra.task.kom.instance;

import java.time.LocalDateTime;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.TaskInstanceMeta;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.EntryNode;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.marshal.TaskScheduleCycle;
import com.pinecone.hydra.task.marshal.TaskScheduleType;

public interface InstanceEntry extends TaskInstanceMeta, EntryNode {

     @Override
     default String getName() {
          return this.getInstanceName();
     }

     String getTaskName();

     TaskElement taskElement();

     void setGuid ( GUID guid );

     void setTaskGuid ( GUID taskGuid );

     void setInstanceName ( String instanceName );

     void setBusinessTime ( LocalDateTime businessTime );

     void setTaskName ( String taskName );

     void setPriority ( int priority );

     void setImagePath( String imagePath );

     void setActuallyPriority ( int actuallyPriority );

     void setInstanceStatus ( TaskInstanceStatus instanceStatus );

     void setTaskType ( String taskType );

     void setRunCount ( int runCount );

     void setSequenceCnt( int sequenceCnt );

     void setRetryCnt( int retryCnt );

     void setDryRun ( boolean dryRun );

     void setErrorCause( String errorCause );

     void setScheduleCycle ( TaskScheduleCycle kernelScheduleCycle ) ;

     void setScheduleType ( TaskScheduleType kernelScheduleType ) ;

     void setLastStartTime ( LocalDateTime lastStartTime );

     void setLastEndTime ( LocalDateTime lastEndTime );

     void setCreateTime ( LocalDateTime createTime );

     void setUpdateTime ( LocalDateTime updateTime );

     TaskInstrument getTaskInstrument();



     String getRunStatus ();

}
