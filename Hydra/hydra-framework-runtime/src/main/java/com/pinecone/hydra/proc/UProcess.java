package com.pinecone.hydra.proc;

import java.time.LocalDateTime;
import java.util.Map;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.entity.ProcessElement;
import com.pinecone.hydra.proc.image.EntryPointRunnable;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.ns.ProcSpace;
import com.pinecone.hydra.proc.tomb.RuntimeTombstone;
import com.pinecone.hydra.system.ko.entity.ObjectTable;

public interface UProcess extends Processum, ProcessElement {

    ProcessActionTape actionTape();

    void applyStatus( UProcessStatus status );

    UProcess parentProcess();

    GUID actualParentPID();

    void applyActualParentPID( GUID pid );

    ProcessManager getOwnedProcessManager();

    ProcSpace getProcNamespace();

    RuntimeTombstone getRuntimeTombstone();

    ObjectTable getObjectTable();

    ExecutionImage getExecutionImage();

    EntryPointRunnable getEntryPoint();

    ControllableLevel getControllableLevel();

    LocalDateTime getEndTime() ;

    LocalDateTime getLastUpdateTime() ;

    Map<String, String> getStartupArguments();

    Map<String, String> getEnvironmentVariables();

    Processum affinityLocalProcess();

    void triggerUpdateTerminationStatus();

    void triggerAfterRunnableTerminationStatus();

}

