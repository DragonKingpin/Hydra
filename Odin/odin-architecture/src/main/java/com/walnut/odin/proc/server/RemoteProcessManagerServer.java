package com.walnut.odin.proc.server;

import java.net.URI;
import java.util.Collection;
import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.UProcess;
import com.walnut.odin.proc.RemoteProcess;
import com.walnut.odin.proc.RemoteProcessLifecycleException;
import com.walnut.odin.proc.RemoteProcessManagerNode;
import com.walnut.odin.proc.RemoteProcessServiceRPCException;
import com.walnut.odin.proc.entity.RemoteProcessCreationContext;
import com.walnut.odin.proc.entity.RemoteVitalizationResponse;
import com.walnut.odin.proc.entity.UProcessMirrorDTO;
import com.walnut.odin.proc.server.transport.RemoteProcessControlEventHooker;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransport;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransportRegistry;

public interface RemoteProcessManagerServer extends RemoteProcessManagerNode {

    RemoteProcessManagerServer hookTransport( RemoteProcessControlTransport transport );

    RemoteProcessManagerServer hookTransportEvent( RemoteProcessControlEventHooker hooker );

    RemoteProcessControlTransportRegistry transportRegistry();

    Collection<RemoteProcessControlTransport> transports();

    boolean hasClient( long clientId );

    boolean isControlClientReady( long clientId );

    Collection<Long> readyControlClientIds();

    void markControlClientReady( long clientId );

    String openClientControlSession( long clientId );

    boolean isClientControlSession( long clientId, String szSessionGuid );

    void detachClient( long clientId );

    void registerController( Object controller ) throws RemoteProcessServiceRPCException;

    void compileIface( Class<?> ifaceClass, boolean bAsIface ) throws RemoteProcessServiceRPCException;

    void registerProcess( long clientId, UProcessMirrorDTO processDTO );

    void beginClientProcessSnapshot( long clientId );

    void acceptClientProcessMirror( long clientId, UProcessMirrorDTO processDTO );

    void endClientProcessSnapshot( long clientId );

    void startRemoteUProcess( GUID pid ) throws RemoteProcessServiceRPCException;

    RemoteVitalizationResponse vitalizeRemoteUProcess( long clientId, String imageAddress, boolean isURI, GUID parentPID, Map<String, String> startupArgs, Map<String, String> contextEnvironmentVars ) throws RemoteProcessLifecycleException;

    RemoteVitalizationResponse vitalizeRemoteUProcess( long clientId, RemoteProcessCreationContext context ) throws RemoteProcessLifecycleException;

    RemoteVitalizationResponse vitalizeRemoteUProcess( long clientId, String imagePath, GUID parentPID, Map<String, String> startupArgs, Map<String, String> contextEnvironmentVars ) throws RemoteProcessLifecycleException;

    RemoteVitalizationResponse vitalizeRemoteUProcess( long clientId, URI imageURI, GUID parentPID, Map<String, String> startupArgs, Map<String, String> contextEnvironmentVars ) throws RemoteProcessLifecycleException;


    RemoteCreationResult createRemoteUProcess( long clientId, String imageAddress, boolean isURI, GUID parentPID, Map<String, String> startupArgs, Map<String, String> contextEnvironmentVars ) throws RemoteProcessLifecycleException;

    RemoteCreationResult createRemoteUProcess( long clientId, RemoteProcessCreationContext context ) throws RemoteProcessLifecycleException;

    RemoteCreationResult createRemoteUProcess( long clientId, String imagePath, GUID parentPID, Map<String, String> startupArgs, Map<String, String> contextEnvironmentVars ) throws RemoteProcessLifecycleException;

    RemoteCreationResult createRemoteUProcess( long clientId, URI imageURI, GUID parentPID, Map<String, String> startupArgs, Map<String, String> contextEnvironmentVars ) throws RemoteProcessLifecycleException;


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
