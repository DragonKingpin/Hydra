package com.walnut.odin.proc.server;

import java.net.URI;
import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.uma.DuplexAppointServer;
import com.walnut.odin.proc.RemoteProcess;
import com.walnut.odin.proc.RemoteProcessLifecycleException;
import com.walnut.odin.proc.RemoteProcessManagerNode;
import com.walnut.odin.proc.RemoteProcessServiceRPCException;
import com.walnut.odin.proc.entity.RemoteVitalizationResponse;
import com.walnut.odin.proc.entity.UProcessMirrorDTO;

public interface RemoteProcessManagerServer extends RemoteProcessManagerNode {

    DuplexAppointServer duplexAppointServer();

    void registerProcess( long clientId, UProcessMirrorDTO processDTO );

    void startRemoteUProcess( GUID pid ) throws RemoteProcessServiceRPCException;

    RemoteVitalizationResponse vitalizeRemoteUProcess( long clientId, String imageAddress, boolean isURI, GUID parentPID, Map<String, String[]> startupArgs, Map<String, String[]> contextEnvironmentVars ) throws RemoteProcessLifecycleException;

    RemoteVitalizationResponse vitalizeRemoteUProcess( long clientId, String imagePath, GUID parentPID, Map<String, String[]> startupArgs, Map<String, String[]> contextEnvironmentVars ) throws RemoteProcessLifecycleException;

    RemoteVitalizationResponse vitalizeRemoteUProcess( long clientId, URI imageURI, GUID parentPID, Map<String, String[]> startupArgs, Map<String, String[]> contextEnvironmentVars ) throws RemoteProcessLifecycleException;


    RemoteCreationResult createRemoteUProcess( long clientId, String imageAddress, boolean isURI, GUID parentPID, Map<String, String[]> startupArgs, Map<String, String[]> contextEnvironmentVars ) throws RemoteProcessLifecycleException;

    RemoteCreationResult createRemoteUProcess( long clientId, String imagePath, GUID parentPID, Map<String, String[]> startupArgs, Map<String, String[]> contextEnvironmentVars ) throws RemoteProcessLifecycleException;

    RemoteCreationResult createRemoteUProcess( long clientId, URI imageURI, GUID parentPID, Map<String, String[]> startupArgs, Map<String, String[]> contextEnvironmentVars ) throws RemoteProcessLifecycleException;


    @Override
    void register( UProcess that );

    @Override
    void erase( UProcess that );

    Long queryClientIdByPID( GUID pid );

    RemoteProcess createMediatedRemoteProcess( long clientId, RemoteVitalizationResponse response );

    RemoteProcess createMediatedRemoteProcess( long clientId, UProcessMirrorDTO processDTO );



    class RemoteCreationResult {
        RemoteVitalizationResponse response;
        RemoteProcess process;

        public RemoteProcess getProcess() {
            return this.process;
        }

        public RemoteVitalizationResponse getResponse() {
            return this.response;
        }
    }

}
