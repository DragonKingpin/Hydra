package com.pinecone.hydra.task.kom.instance.dto;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.entity.EnumIndexableEntity;

public interface Instance extends EnumIndexableEntity {

     void setGuid( GUID guid );
     GUID getGuid();

     void setBusinessDate ( String businessDate );
     String getBusinessDate ();

     void setPriority ( String priority );
     String getPriority ();

     void setRunStatus ( String runStatus );
     String getRunStatus ();

     void setScheduleCycle ( String scheduleCycle );
     String getScheduleCycle ();

     void setTaskType  ( String taskType );
     String getTaskType ();


     void setRunCount ( String runCount );
     short getRunCount ();

     void setScheduleType ( String scheduleType );
     String getScheduleType ();

     void setlastStartTime ( String lastStartTime );
     String getlastStartTime ();

     void setlastEndTime ( String lastEndTime );
     String getlastEndTime ();
}
