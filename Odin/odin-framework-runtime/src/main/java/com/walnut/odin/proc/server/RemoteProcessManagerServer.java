package com.walnut.odin.proc.server;

import java.net.URI;
import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.UProcess;
import com.walnut.odin.proc.RemoteProcess;
import com.walnut.odin.proc.RemoteProcessLifecycleException;
import com.walnut.odin.proc.RemoteProcessManagerNode;
import com.walnut.odin.proc.RemoteProcessServiceRPCException;
import com.walnut.odin.proc.dto.RemoteVitalizationResponse;
import com.walnut.odin.proc.dto.UProcessMirrorDTO;

public interface RemoteProcessManagerServer extends RemoteProcessManagerNode {

    void registerProcess( long clientId, UProcessMirrorDTO processDTO );

    void startRemoteUProcess( GUID pid ) throws RemoteProcessServiceRPCException;

    RemoteVitalizationResponse vitalizeRemoteUProcess(long clientId, String imageAddress, boolean isURI, GUID parentPID, Map<String, String[]> startupArgs, Map<String, String[]> contextEnvironmentVars ) throws RemoteProcessLifecycleException;

    RemoteVitalizationResponse vitalizeRemoteUProcess( long clientId, String imagePath, GUID parentPID, Map<String, String[]> startupArgs, Map<String, String[]> contextEnvironmentVars ) throws RemoteProcessLifecycleException;

    RemoteVitalizationResponse vitalizeRemoteUProcess( long clientId, URI imageURI, GUID parentPID, Map<String, String[]> startupArgs, Map<String, String[]> contextEnvironmentVars ) throws RemoteProcessLifecycleException;

    void register( UProcess that );

    void erase( UProcess that );

    Long queryClientIdByPID( GUID pid );

    RemoteProcess createMediatedRemoteProcess( long clientId, RemoteVitalizationResponse response );

    RemoteProcess createMediatedRemoteProcess( long clientId, UProcessMirrorDTO processDTO );

}
