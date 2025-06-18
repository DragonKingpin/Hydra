package com.walnut.odin.proc;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.RemoteUProcess;

import java.time.LocalDateTime;
import java.util.Map;


public interface OdinRemoteProcess extends RemoteUProcess {
    String getName();

    long getPID();

    GUID getGuid();

    LocalDateTime remoteGetEndTime();

    LocalDateTime remoteGetLastUpdateTime();

    Map<String, String[]> getStartupArguments();

    Map<String, String[]> getEnvironmentVariables();
}
