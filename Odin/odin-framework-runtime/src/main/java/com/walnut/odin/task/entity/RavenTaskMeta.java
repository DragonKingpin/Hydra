package com.walnut.odin.task.entity;

import java.net.URI;
import java.util.Set;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.TaskExtraMeta;
import com.pinecone.hydra.task.TaskFamilyMeta;

public interface RavenTaskMeta extends TaskExtraMeta {

    Set<String> JsonifyExpectedKey = Set.of( "kernelMeta" );

    int getTaskVersion();

    void setTaskVersion( int taskVersion ) ;

    boolean isRootTask();

    void setRootTask( boolean rootTask );

    @Override
    TaskFamilyMeta getKernelMeta();

    void setKernelMeta( TaskFamilyMeta kernelMeta );

    @Override
    GUID getGuid();

    void setGuid( GUID guid );

    @Override
    String getTaskName();

    GUID getDeploySchemeId() ;

    void setDeploySchemeId( GUID deploySchemeId ) ;

}
