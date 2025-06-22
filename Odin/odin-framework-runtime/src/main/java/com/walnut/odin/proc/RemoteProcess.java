package com.walnut.odin.proc;

import com.pinecone.hydra.proc.RemoteUProcess;

import java.time.LocalDateTime;


public interface RemoteProcess extends RemoteUProcess {

    LocalDateTime remoteGetEndTime();

    LocalDateTime remoteGetLastUpdateTime();

}
