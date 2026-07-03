package com.walnut.odin.proc.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.odin.proc.RemoteProcessLifecycleException;
import com.pinecone.hydra.proc.signal.ProcSignal;
import com.walnut.odin.proc.RemoteVitalizationStatus;
import com.walnut.odin.proc.entity.RemoteProcessSignalResult;
import com.walnut.odin.proc.entity.RemoteVitalizationResponse;
import com.walnut.odin.proc.entity.UProcessMirrorDTO;
import com.walnut.odin.proc.entity.UProcessRuntimeMeta;

@Controller
@AddressMapping( "com.walnut.odin.proc.server.MasterProcessLifecycleIface." )
public class ReactiveMasterProcessLifecycleController implements Pinenut {

    private final RemoteProcessManagerClient mRemoteProcessManagerClient;

    private final GuidAllocator mGuidAllocator;

    public ReactiveMasterProcessLifecycleController( RemoteProcessManagerClient remoteProcessManagerClient ) {
        this.mRemoteProcessManagerClient = remoteProcessManagerClient;
        this.mGuidAllocator = remoteProcessManagerClient.getGuidAllocator();
    }

    @AddressMapping("startRemoteUProcess")
    public void startRemoteUProcess( String szPid ) {
        this.mRemoteProcessManagerClient.startLocalUProcess( this.mGuidAllocator.parse(szPid) );
    }

    @AddressMapping("signalRemoteUProcess")
    public RemoteProcessSignalResult signalRemoteUProcess( String processId, String signal, long graceTimeoutMillis, String reason ) {
        return this.mRemoteProcessManagerClient.signalLocalUProcess(
                this.mGuidAllocator.parse( processId ),
                ProcSignal.parse( signal ),
                graceTimeoutMillis,
                reason
        );
    }

    @AddressMapping("vitalizeRemoteUProcess")
    public RemoteVitalizationResponse vitalizeRemoteUProcess( UProcessMirrorDTO handlerDTO ) throws RemoteProcessLifecycleException {
        String imageAddress = handlerDTO == null ? null : handlerDTO.getImageAddress();
        this.mRemoteProcessManagerClient.getLogger().info( "[RemoteProcessVitalization] [PRC] (Process: `{}`) <InstructionAccepted>", imageAddress );
        try {
            RemoteVitalizationResponse response = this.mRemoteProcessManagerClient.vitalizeLocalUProcess( handlerDTO );
            this.mRemoteProcessManagerClient.getLogger().info( "[RemoteProcessVitalization] [PRC] (Process: `{}`) <InstructionPerformed>", imageAddress );
            return response;
        }
        catch ( Throwable cause ) {
            this.mRemoteProcessManagerClient.getLogger().warn(
                    "[RemoteProcessVitalization] [PRC] (Process: `{}`) <Failure>",
                    imageAddress,
                    cause
            );
            return this.errorResponse( handlerDTO, cause );
        }
    }

    @AddressMapping("createRemoteUProcess")
    public RemoteVitalizationResponse createRemoteUProcess( UProcessMirrorDTO handlerDTO ) throws RemoteProcessLifecycleException {
        String imageAddress = handlerDTO == null ? null : handlerDTO.getImageAddress();
        this.mRemoteProcessManagerClient.getLogger().info( "[RemoteProcessCreation] [PRC] (Process: `{}`) <InstructionAccepted>", imageAddress );
        try {
            RemoteVitalizationResponse response = this.mRemoteProcessManagerClient.createLocalUProcess( handlerDTO, null );
            this.mRemoteProcessManagerClient.getLogger().info( "[RemoteProcessCreation] [PRC] (Process: `{}`) <InstructionPerformed>", imageAddress );
            return response;
        }
        catch ( Throwable cause ) {
            this.mRemoteProcessManagerClient.getLogger().warn(
                    "[RemoteProcessCreation] [PRC] (Process: `{}`) <Failure>",
                    imageAddress,
                    cause
            );
            return this.errorResponse( handlerDTO, cause );
        }
    }

    private RemoteVitalizationResponse errorResponse( UProcessMirrorDTO handlerDTO, Throwable cause ) {
        RemoteVitalizationResponse response = new RemoteVitalizationResponse();
        response.setRemoteVitalizationStatus( RemoteVitalizationStatus.Error );
        response.setErrorMsg( this.describeThrowable( cause ) );
        if ( handlerDTO != null ) {
            response.setImageAddress( handlerDTO.getImageAddress() );
            response.setImageAddressURI( handlerDTO.isImageAddressURI() );
            try {
                response.setImageResolutionMode( handlerDTO.getImageResolutionMode() );
            }
            catch ( Exception ignored ) {
                // Preserve the original failure as the RPC response reason.
            }
        }
        return response;
    }

    private String describeThrowable( Throwable cause ) {
        if ( cause == null ) {
            return null;
        }
        String message = cause.getMessage();
        if ( message == null || message.trim().isEmpty() ) {
            return cause.getClass().getName();
        }
        return cause.getClass().getName() + ": " + message;
    }

    @AddressMapping("hasOwnProcess")
    public boolean hasOwnProcess( String processId ) {
        boolean has = this.mRemoteProcessManagerClient.hasOwnProcess( this.mGuidAllocator.parse(processId) );
        return has;
    }

    @AddressMapping("containProcess")
    public boolean containProcess( String processId ) {
        boolean has = this.mRemoteProcessManagerClient.containProcess( this.mGuidAllocator.parse(processId) );
        return has;
    }

    @AddressMapping("queryRemoteProcessRuntimeMeta")
    public UProcessRuntimeMeta queryRemoteProcessRuntimeMeta( String processId ) throws RemoteProcessLifecycleException {
        return this.mRemoteProcessManagerClient.queryProcessRuntimeMeta( this.mGuidAllocator.parse(processId) );
    }

}
