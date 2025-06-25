package com.walnut.odin.proc.client;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.walnut.odin.proc.RemoteProcessLifecycleException;
import com.walnut.odin.proc.RemoteProcessManagerNode;
import com.walnut.odin.proc.dto.RemoteVitalizationResponse;
import com.walnut.odin.proc.dto.UProcessMirrorDTO;

import java.util.Map;

public interface RemoteProcessManagerClient extends RemoteProcessManagerNode {

    /**
     *  createLocalUProcess
     *  Proactively creating local-UProcess.
     */
    UProcess createLocalUProcess( ExecutionImage image, UProcess parent, Map<String, String[]> startupArgs, Map<String, String[]> contextEnvironmentVars );

    void startLocalUProcess( GUID pid );

    long getClientId();

    RemoteVitalizationResponse createLocalUProcess( UProcessMirrorDTO handlerDTO, UProcess[] lpProcess ) throws RemoteProcessLifecycleException;

    RemoteVitalizationResponse vitalizeLocalUProcess( UProcessMirrorDTO handlerDTO ) throws RemoteProcessLifecycleException;

}
