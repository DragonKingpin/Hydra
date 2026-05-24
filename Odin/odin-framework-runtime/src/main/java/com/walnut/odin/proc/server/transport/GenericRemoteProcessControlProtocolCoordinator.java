package com.walnut.odin.proc.server.transport;

import com.pinecone.framework.util.json.JSON;
import com.pinecone.framework.util.json.TypeReference;
import com.pinecone.framework.util.id.GuidAllocator;
import com.walnut.odin.proc.control.RemoteProcessControlFrame;
import com.walnut.odin.proc.control.RemoteProcessControlFrameType;
import com.walnut.odin.proc.entity.UProcessMirrorDTO;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;

import java.util.ArrayList;
import java.util.List;

public class GenericRemoteProcessControlProtocolCoordinator implements RemoteProcessControlProtocolCoordinator {

    protected RemoteProcessManagerServer    mRemoteProcessManagerServer;

    protected GuidAllocator                 mGuidAllocator;

    public GenericRemoteProcessControlProtocolCoordinator( RemoteProcessManagerServer remoteProcessManagerServer ) {
        this.mRemoteProcessManagerServer = remoteProcessManagerServer;
        this.mGuidAllocator              = remoteProcessManagerServer.getGuidAllocator();
    }

    protected String nextGuidString() {
        return this.mGuidAllocator.nextGUID().toString();
    }

    @Override
    public RemoteProcessControlFrame exchangeControlFrame( RemoteProcessControlTransport transport, RemoteProcessControlFrame frame ) {
        if ( frame == null ) {
            return this.errorFrame( null, 0, "Remote process control frame is null." );
        }

        RemoteProcessControlFrameType frameType = frame.optFrameType();
        switch ( frameType ) {
            case ClientMuster: {
                return this.acceptClientMuster( transport, frame );
            }
            case ClientSnapshotBegin: {
                if ( !this.acceptCurrentSession( frame ) ) {
                    return this.errorFrame( frame, frame.getClientId(), "Remote process control session is not current." );
                }
                this.mRemoteProcessManagerServer.beginClientProcessSnapshot( frame.getClientId() );
                return this.ackFrame( frame, "Client process snapshot started." );
            }
            case ProcessMirror: {
                if ( !this.acceptCurrentSession( frame ) ) {
                    return this.errorFrame( frame, frame.getClientId(), "Remote process control session is not current." );
                }
                if ( frame.getProcessMirror() == null ) {
                    return this.errorFrame( frame, frame.getClientId(), "Process mirror frame is missing mirror payload." );
                }
                this.mRemoteProcessManagerServer.acceptClientProcessMirror( frame.getClientId(), frame.getProcessMirror() );
                return this.ackFrame( frame, "Process mirror accepted." );
            }
            case ClientSnapshotEnd: {
                if ( !this.acceptCurrentSession( frame ) ) {
                    return this.errorFrame( frame, frame.getClientId(), "Remote process control session is not current." );
                }
                this.mRemoteProcessManagerServer.endClientProcessSnapshot( frame.getClientId() );
                return this.ackFrame( frame, "Client process snapshot committed." );
            }
            case Apoptosis: {
                if ( !this.acceptCurrentSession( frame ) ) {
                    return this.errorFrame( frame, frame.getClientId(), "Remote process control session is not current." );
                }
                this.mRemoteProcessManagerServer.detachClient( frame.getClientId() );
                return this.ackFrame( frame, "Client detached." );
            }
            default: {
                return this.errorFrame( frame, frame.getClientId(), "Unsupported remote process control frame type: `" + frame.getFrameType() + "`." );
            }
        }
    }

    protected RemoteProcessControlFrame acceptClientMuster( RemoteProcessControlTransport transport, RemoteProcessControlFrame frame ) {
        long nClientId = frame.getClientId();
        if ( transport == null || !transport.containsClient( nClientId ) ) {
            return this.errorFrame( frame, nClientId, "Remote process control passive channel is not ready." );
        }

        this.mRemoteProcessManagerServer.transportRegistry().bindClient( nClientId, transport );
        String szSessionGuid = this.mRemoteProcessManagerServer.openClientControlSession( nClientId );

        return this.readyFrame( frame, nClientId, szSessionGuid );
    }

    protected void acceptClientSnapshot( long nClientId, List<UProcessMirrorDTO> processMirrors ) {
        this.mRemoteProcessManagerServer.beginClientProcessSnapshot( nClientId );

        if ( processMirrors != null ) {
            for ( UProcessMirrorDTO processMirror : processMirrors ) {
                this.mRemoteProcessManagerServer.acceptClientProcessMirror( nClientId, processMirror );
            }
        }

        this.mRemoteProcessManagerServer.endClientProcessSnapshot( nClientId );
    }

    @Override
    public String musterClient( RemoteProcessControlTransport transport, long nClientId, String szFrameGuid, String szSnapshotJson ) {
        RemoteProcessControlFrame request = this.requestFrame( nClientId, null, szFrameGuid, RemoteProcessControlFrameType.ClientMuster );
        try {
            if ( transport == null || !transport.containsClient( nClientId ) ) {
                return this.responseJson( this.errorFrame( request, nClientId, "Remote process control passive channel is not ready." ) );
            }

            this.mRemoteProcessManagerServer.transportRegistry().bindClient( nClientId, transport );
            String szSessionGuid = this.mRemoteProcessManagerServer.openClientControlSession( nClientId );
            this.acceptClientSnapshot( nClientId, this.decodeProcessMirrors( szSnapshotJson ) );
            this.mRemoteProcessManagerServer.getLogger().info(
                    "[RemoteProcessControlSync] [ClientMuster] (ClientId: `{}`, SessionGuid: `{}`) <Ready>",
                    nClientId,
                    szSessionGuid
            );

            return this.responseJson( this.readyFrame( request, nClientId, szSessionGuid ) );
        }
        catch ( Exception e ) {
            return this.responseJson( this.errorFrame( request, nClientId, e.getMessage() ) );
        }
    }

    @Override
    public String reportProcessMirror(
            RemoteProcessControlTransport transport, long nClientId, String szSessionGuid, String szFrameGuid, String szProcessMirrorJson
    ) {
        RemoteProcessControlFrame request = this.requestFrame( nClientId, szSessionGuid, szFrameGuid, RemoteProcessControlFrameType.ProcessMirror );
        try {
            if ( transport == null || !transport.containsClient( nClientId ) ) {
                return this.responseJson( this.errorFrame( request, nClientId, "Remote process control passive channel is not ready." ) );
            }
            if ( !this.mRemoteProcessManagerServer.isClientControlSession( nClientId, szSessionGuid ) ) {
                return this.responseJson( this.errorFrame( request, nClientId, "Remote process control session is not current." ) );
            }

            UProcessMirrorDTO processMirror = this.decodeProcessMirror( szProcessMirrorJson );
            if ( processMirror == null ) {
                return this.responseJson( this.errorFrame( request, nClientId, "Process mirror frame is missing mirror payload." ) );
            }

            this.mRemoteProcessManagerServer.acceptClientProcessMirror( nClientId, processMirror );
            return this.responseJson( this.ackFrame( request, "Process mirror accepted." ) );
        }
        catch ( Exception e ) {
            return this.responseJson( this.errorFrame( request, nClientId, e.getMessage() ) );
        }
    }

    @Override
    public String detachClient( RemoteProcessControlTransport transport, long nClientId, String szSessionGuid, String szFrameGuid ) {
        RemoteProcessControlFrame request = this.requestFrame( nClientId, szSessionGuid, szFrameGuid, RemoteProcessControlFrameType.Apoptosis );
        try {
            if ( szSessionGuid != null && !this.mRemoteProcessManagerServer.isClientControlSession( nClientId, szSessionGuid ) ) {
                return this.responseJson( this.errorFrame( request, nClientId, "Remote process control session is not current." ) );
            }
            this.mRemoteProcessManagerServer.detachClient( nClientId );
            return this.responseJson( this.ackFrame( request, "Client detached." ) );
        }
        catch ( Exception e ) {
            return this.responseJson( this.errorFrame( request, nClientId, e.getMessage() ) );
        }
    }

    protected List<UProcessMirrorDTO> decodeProcessMirrors( String szSnapshotJson ) {
        if ( szSnapshotJson == null || szSnapshotJson.isEmpty() ) {
            return new ArrayList<>();
        }
        return JSON.unmarshal( szSnapshotJson, new TypeReference<List<UProcessMirrorDTO>>() {} );
    }

    protected UProcessMirrorDTO decodeProcessMirror( String szProcessMirrorJson ) {
        if ( szProcessMirrorJson == null || szProcessMirrorJson.isEmpty() ) {
            return null;
        }
        return JSON.unmarshal( szProcessMirrorJson, UProcessMirrorDTO.class );
    }

    protected boolean acceptCurrentSession( RemoteProcessControlFrame frame ) {
        return frame != null && this.mRemoteProcessManagerServer.isClientControlSession(
                frame.getClientId(), frame.getSessionGuid()
        );
    }

    protected RemoteProcessControlFrame readyFrame( RemoteProcessControlFrame request, long nClientId, String szSessionGuid ) {
        RemoteProcessControlFrame ready = new RemoteProcessControlFrame();
        ready.setFrameGuid( this.nextGuidString() );
        if ( request != null ) {
            ready.setCorrelationGuid( request.getFrameGuid() );
        }
        ready.setClientId( nClientId );
        ready.setSessionGuid( szSessionGuid );
        ready.applyFrameType( RemoteProcessControlFrameType.ClientReady );
        ready.setMessage( "Client control session ready." );
        return ready;
    }

    protected RemoteProcessControlFrame requestFrame( long nClientId, String szSessionGuid, String szFrameGuid, RemoteProcessControlFrameType frameType ) {
        RemoteProcessControlFrame request = new RemoteProcessControlFrame();
        request.setFrameGuid( szFrameGuid );
        request.setClientId( nClientId );
        request.setSessionGuid( szSessionGuid );
        request.applyFrameType( frameType );
        return request;
    }

    protected String responseJson( RemoteProcessControlFrame frame ) {
        if ( frame == null ) {
            return "";
        }
        return frame.toJSONString();
    }

    protected RemoteProcessControlFrame ackFrame( RemoteProcessControlFrame request, String szMessage ) {
        RemoteProcessControlFrame ack = new RemoteProcessControlFrame();
        ack.setFrameGuid( this.nextGuidString() );
        if ( request != null ) {
            ack.setCorrelationGuid( request.getFrameGuid() );
            ack.setClientId( request.getClientId() );
            ack.setSessionGuid( request.getSessionGuid() );
        }
        ack.applyFrameType( RemoteProcessControlFrameType.FrameAck );
        ack.setMessage( szMessage );
        return ack;
    }

    protected RemoteProcessControlFrame errorFrame( RemoteProcessControlFrame request, long nClientId, String szMessage ) {
        RemoteProcessControlFrame error = new RemoteProcessControlFrame();
        error.setFrameGuid( this.nextGuidString() );
        if ( request != null ) {
            error.setCorrelationGuid( request.getFrameGuid() );
            error.setClientId( request.getClientId() );
            error.setSessionGuid( request.getSessionGuid() );
        }
        else {
            error.setClientId( nClientId );
        }
        error.applyFrameType( RemoteProcessControlFrameType.Error );
        error.setMessage( szMessage );
        return error;
    }
}
