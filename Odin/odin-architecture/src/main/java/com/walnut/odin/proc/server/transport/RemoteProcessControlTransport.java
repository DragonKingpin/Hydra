package com.walnut.odin.proc.server.transport;

import java.util.Collection;
import java.util.Collections;

import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.proc.RemoteProcessLifecycleException;
import com.walnut.odin.proc.RemoteProcessServiceRPCException;
import com.walnut.odin.proc.entity.RemoteVitalizationResponse;
import com.walnut.odin.proc.entity.UProcessMirrorDTO;
import com.walnut.odin.proc.entity.UProcessRuntimeMeta;
import com.walnut.odin.proc.server.transport.entity.TransportConnection;

public interface RemoteProcessControlTransport extends RemoteProcessControlTransportLifecycle {

    RemoteProcessControlTransportType transportType();

    boolean containsClient( long clientId );

    default RemoteProcessControlTransport addEventHooker( RemoteProcessControlEventHooker hooker ) {
        if ( hooker != null ) {
            hooker.onTransportHooked( this );
        }
        return this;
    }

    void registerController( Object controller ) throws RemoteProcessServiceRPCException;

    default boolean supportsRuntimeIfaceCompile() {
        return false;
    }

    void compileIface( Class<?> ifaceClass, boolean bAsIface ) throws RemoteProcessServiceRPCException;

    void startRemoteUProcess( long clientId, GUID pid ) throws RemoteProcessServiceRPCException;

    RemoteVitalizationResponse vitalizeRemoteUProcess( long clientId, UProcessMirrorDTO processDTO ) throws RemoteProcessLifecycleException;

    RemoteVitalizationResponse createRemoteUProcess( long clientId, UProcessMirrorDTO processDTO ) throws RemoteProcessLifecycleException;

    boolean hasOwnProcess( long clientId, GUID pid ) throws RemoteProcessServiceRPCException;

    boolean containProcess( long clientId, GUID pid ) throws RemoteProcessServiceRPCException;

    UProcessRuntimeMeta queryProcessRuntimeMeta( long clientId, GUID pid ) throws RemoteProcessLifecycleException;

    default Collection<TransportConnection> queryClientConnections( long clientId ) {
        return Collections.emptyList();
    }

}
