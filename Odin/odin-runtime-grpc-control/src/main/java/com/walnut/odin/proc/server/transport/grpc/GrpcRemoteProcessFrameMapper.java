package com.walnut.odin.proc.server.transport.grpc;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.walnut.odin.proc.RemoteVitalizationStatus;
import com.walnut.odin.proc.entity.RemoteVitalizationResponse;
import com.walnut.odin.proc.entity.UProcessMirrorDTO;
import com.walnut.odin.proc.entity.UProcessRuntimeMeta;
import com.walnut.odin.proc.server.transport.grpc.lifecycle.CommandResult;
import com.walnut.odin.proc.server.transport.grpc.lifecycle.ErrorFrame;
import com.walnut.odin.proc.server.transport.grpc.lifecycle.ProcessId;
import com.walnut.odin.proc.server.transport.grpc.lifecycle.ProcessRuntimeMeta;
import com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame;
import com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrameType;
import com.walnut.odin.proc.server.transport.grpc.lifecycle.UProcessMirror;

public class GrpcRemoteProcessFrameMapper implements Pinenut {

    protected GuidAllocator mGuidAllocator;

    public GrpcRemoteProcessFrameMapper( GuidAllocator guidAllocator ) {
        this.mGuidAllocator = guidAllocator;
    }

    public UProcessMirror toProcessMirror( UProcessMirrorDTO source ) {
        UProcessMirror.Builder builder = UProcessMirror.newBuilder();
        if ( source == null ) {
            return builder.build();
        }
        this.apply( builder::setPid, source.getPID() );
        this.apply( builder::setProcessName, source.getName() );
        this.apply( builder::setImagePath, source.getImageAddress() );
        return builder.build();
    }

    public RemoteVitalizationResponse toVitalizationResponse( CommandResult result, UProcessMirrorDTO source ) {
        RemoteVitalizationResponse response = new RemoteVitalizationResponse();
        if ( result == null ) {
            response.setRemoteVitalizationStatus( RemoteVitalizationStatus.Error );
            response.setErrorMsg( "Empty gRPC command result." );
            this.applyMirrorContext( response, source );
            return response;
        }
        response.setRemoteVitalizationStatus( result.getSuccess() ? RemoteVitalizationStatus.New : RemoteVitalizationStatus.Error );
        response.setErrorMsg( result.getMessage() );
        ProcessRuntimeMeta meta = result.getProcessRuntimeMeta();
        String szFallbackPID = source == null ? null : source.getPID();
        String szPID = meta == null || meta.getPid().isEmpty() ? szFallbackPID : meta.getPid();
        response.setPID( szPID );
        response.setName( szPID );
        this.applyMirrorContext( response, source );
        return response;
    }

    public UProcessRuntimeMeta toRuntimeMeta( ProcessRuntimeMeta source ) {
        UProcessRuntimeMeta meta = new UProcessRuntimeMeta();
        if ( source == null ) {
            return meta;
        }
        meta.setPID( source.getPid() );
        meta.setStatus( source.getStatus() );
        meta.setTerminated( source.getFinishTimeMillis() > 0 );
        meta.setStartTime( Long.toString( source.getStartTimeMillis() ) );
        meta.setEndTime( Long.toString( source.getFinishTimeMillis() ) );
        meta.setLastUpdateTime( Long.toString( System.currentTimeMillis() ) );
        return meta;
    }

    public RemoteProcessControlFrame commandFrame( long clientId, String szCorrelationGuid, RemoteProcessControlFrameType type, UProcessMirrorDTO processDTO ) {
        RemoteProcessControlFrame.Builder builder = this.baseFrame( clientId, szCorrelationGuid, type );
        builder.setUprocessMirror( this.toProcessMirror( processDTO ) );
        return builder.build();
    }

    public RemoteProcessControlFrame processIdFrame( long clientId, String szCorrelationGuid, RemoteProcessControlFrameType type, GUID pid ) {
        RemoteProcessControlFrame.Builder builder = this.baseFrame( clientId, szCorrelationGuid, type );
        if ( pid != null ) {
            builder.setProcessId( ProcessId.newBuilder().setPid( pid.toString() ).build() );
        }
        return builder.build();
    }

    public RemoteProcessControlFrame errorFrame( RemoteProcessControlFrame request, String szCode, String szMessage ) {
        RemoteProcessControlFrame.Builder builder = this.baseFrame(
                request == null ? 0L : request.getClientId(),
                request == null ? "" : request.getFrameGuid(),
                RemoteProcessControlFrameType.ERROR
        );
        builder.setError(
                ErrorFrame.newBuilder()
                        .setErrorCode( szCode == null ? "" : szCode )
                        .setMessage( szMessage == null ? "" : szMessage )
                        .build()
        );
        return builder.build();
    }

    protected RemoteProcessControlFrame.Builder baseFrame( long clientId, String szCorrelationGuid, RemoteProcessControlFrameType type ) {
        return RemoteProcessControlFrame.newBuilder()
                .setFrameGuid( this.nextGuidString() )
                .setCorrelationGuid( szCorrelationGuid == null ? "" : szCorrelationGuid )
                .setClientId( clientId )
                .setCreateTimeMillis( System.currentTimeMillis() )
                .setFrameType( type );
    }

    protected String nextGuidString() {
        return this.mGuidAllocator.nextGUID().toString();
    }

    protected void apply( StringSetter setter, String value ) {
        if ( value != null ) {
            setter.set( value );
        }
    }

    protected void applyMirrorContext( RemoteVitalizationResponse response, UProcessMirrorDTO source ) {
        if ( response == null || source == null ) {
            return;
        }
        response.setLocalPID( source.getLocalPID() );
        response.setStartupArguments( source.getStartupArguments() );
        response.setEnvironmentVariables( source.getEnvironmentVariables() );
        response.setImageAddress( source.getImageAddress() );
        response.setImageAddressURI( source.isImageAddressURI() );
        response.setImageResolutionMode( source.getImageResolutionMode() );
    }

    protected interface StringSetter {
        void set( String value );
    }

}
