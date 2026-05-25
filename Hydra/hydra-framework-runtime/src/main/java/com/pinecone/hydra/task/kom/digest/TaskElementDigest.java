package com.pinecone.hydra.task.kom.digest;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.TaskFamilyMeta;
import com.pinecone.hydra.task.marshal.TaskScheduleCycle;
import com.pinecone.hydra.task.marshal.TaskScheduleType;

import java.time.LocalDateTime;

public interface TaskElementDigest extends TaskFamilyMeta {

    long getEnumId();

    void setEnumId( long nEnumId );

    GUID getGuid();

    void setGuid( GUID guid );

    String getName();

    void setName( String szName );

    String getType();

    void setType( String szType );

    String getImagePath();

    void setImagePath( String szImagePath );

    String getResourceType();

    void setResourceType( String szResourceType );

    String getDeploymentMethod();

    void setDeploymentMethod( String szDeploymentMethod );

    short getPriority();

    void setPriority( short nPriority );

    short getActuallyPriority();

    void setActuallyPriority( short nActuallyPriority );

    boolean isDryRun();

    void setDryRun( boolean bDryRun );

    String getScheduleCron();

    void setScheduleCron( String szScheduleCron );

    TaskScheduleCycle getScheduleCycle();

    void setScheduleCycle( TaskScheduleCycle scheduleCycle );

    TaskScheduleType getScheduleType();

    void setScheduleType( TaskScheduleType scheduleType );

    boolean isEnable();

    void setEnable( boolean bEnable );

    LocalDateTime getScheduleStartTime();

    void setScheduleStartTime( LocalDateTime scheduleStartTime );

    LocalDateTime getScheduleEndTime();

    void setScheduleEndTime( LocalDateTime scheduleEndTime );

    LocalDateTime getNextScheduleTime();

    void setNextScheduleTime( LocalDateTime nextScheduleTime );

    String getProcessorName();

    void setProcessorName( String szProcessorName );

    String getKomPath();

    void setKomPath( String szKomPath );

    String getSystemKernelObjectPath();

    void setSystemKernelObjectPath( String szSystemKernelObjectPath );

    String getProjectGuid();

    void setProjectGuid( String szProjectGuid );

    LocalDateTime getCreateTime();

    void setCreateTime( LocalDateTime createTime );

    LocalDateTime getUpdateTime();

    void setUpdateTime( LocalDateTime updateTime );
}
