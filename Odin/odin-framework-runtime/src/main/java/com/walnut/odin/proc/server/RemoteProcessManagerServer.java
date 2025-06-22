package com.walnut.odin.proc.server;

import java.net.URI;
import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.proc.RemoteProcessLifecycleException;
import com.walnut.odin.proc.RemoteProcessManagerNode;
import com.walnut.odin.proc.RemoteProcessServiceRPCException;
import com.walnut.odin.proc.dto.RemoteVitalizationResponse;
import com.walnut.odin.proc.dto.UProcessHandlerDTO;

public interface RemoteProcessManagerServer extends RemoteProcessManagerNode {

    void registerProcess( long clientId, UProcessHandlerDTO processDTO );

    void startRemoteUProcess( GUID pid ) throws RemoteProcessServiceRPCException;

    RemoteVitalizationResponse vitalizeRemoteUProcess(long clientId, String imageAddress, boolean isURI, GUID parentPID, Map<String, String[]> startupArgs, Map<String, String[]> contextEnvironmentVars ) throws RemoteProcessLifecycleException;

    RemoteVitalizationResponse vitalizeRemoteUProcess( long clientId, String imagePath, GUID parentPID, Map<String, String[]> startupArgs, Map<String, String[]> contextEnvironmentVars ) throws RemoteProcessLifecycleException;

    RemoteVitalizationResponse vitalizeRemoteUProcess( long clientId, URI imageURI, GUID parentPID, Map<String, String[]> startupArgs, Map<String, String[]> contextEnvironmentVars ) throws RemoteProcessLifecycleException;

}
