package com.pinecone.hydra.proc;

import java.time.LocalDateTime;
import java.util.Map;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.proc.entity.ProcessElement;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.ns.ProcSpace;
import com.pinecone.hydra.system.ko.entity.ObjectTable;

public interface UProcess extends Processum, ProcessElement {

    UProcess parentProcess();

    ProcessManager getOwnedProcessManager();

    ProcSpace getProcNamespace();

    ObjectTable getObjectTable();

    ExecutionImage getExecutionImage();

    ControllableLevel getControllableLevel();

    LocalDateTime getEndTime() ;

    LocalDateTime getLastUpdateTime() ;

    Map<String, String[]> getStartupArguments();

    Map<String, String[]> getEnvironmentVariables();

    Processum getCurrentLocalSystemProcess();

    void triggerUpdateTerminationStatus();

}
