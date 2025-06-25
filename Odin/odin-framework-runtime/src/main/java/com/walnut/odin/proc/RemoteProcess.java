package com.walnut.odin.proc;

import com.pinecone.hydra.proc.RemoteUProcess;
import com.walnut.odin.proc.dto.UProcessRuntimeMeta;

import java.time.LocalDateTime;


public interface RemoteProcess extends RemoteUProcess {

    long getControlClientId();

    LocalDateTime remoteGetEndTime();

    LocalDateTime remoteGetLastUpdateTime();

    UProcessRuntimeMeta retrieveRemoteRuntimeMeta() throws RemoteProcessLifecycleException;

}
