package com.walnut.odin.proc.client;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.walnut.odin.proc.RemoteProcessLifecycleException;
import com.walnut.odin.proc.RemoteProcessManagerNode;
import com.walnut.odin.proc.RemoteTerminationStatus;
import com.pinecone.hydra.proc.signal.ProcSignal;
import com.walnut.odin.proc.entity.RemoteProcessSignalResult;
import com.walnut.odin.proc.entity.RemoteVitalizationResponse;
import com.walnut.odin.proc.entity.UProcessMirrorDTO;

import java.util.Map;

public interface RemoteProcessManagerClient extends RemoteProcessManagerNode {

    /**
     *  createLocalUProcess
     *  Proactively creating local-UProcess.
     */
    UProcess createLocalUProcess( ExecutionImage image, UProcess parent, Map<String, String> startupArgs, Map<String, String> contextEnvironmentVars );

    void startLocalUProcess( GUID pid );

    default RemoteProcessSignalResult signalLocalUProcess( GUID pid, ProcSignal signal, long graceTimeoutMillis ) {
        return this.signalLocalUProcess( pid, signal, graceTimeoutMillis, null );
    }

    RemoteProcessSignalResult signalLocalUProcess( GUID pid, ProcSignal signal, long graceTimeoutMillis, String szReason );

    default RemoteTerminationStatus consumeSignalTerminationStatus( GUID pid ) {
        return null;
    }

    long getClientId();

    RemoteVitalizationResponse createLocalUProcess( UProcessMirrorDTO handlerDTO, UProcess[] lpProcess ) throws RemoteProcessLifecycleException;

    RemoteVitalizationResponse vitalizeLocalUProcess( UProcessMirrorDTO handlerDTO ) throws RemoteProcessLifecycleException;

    DuplexAppointClient duplexAppointClient();

}

