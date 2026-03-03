package com.walnut.odin.proc.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.odin.proc.RemoteProcessLifecycleException;
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

    @AddressMapping("vitalizeRemoteUProcess")
    public RemoteVitalizationResponse vitalizeRemoteUProcess( UProcessMirrorDTO handlerDTO ) throws RemoteProcessLifecycleException {
        String imageAddress = handlerDTO.getImageAddress();
        this.mRemoteProcessManagerClient.getLogger().info( "[RemoteProcessVitalization] [PRC] (Process: `{}`) <InstructionAccepted>", imageAddress );
        RemoteVitalizationResponse response = this.mRemoteProcessManagerClient.vitalizeLocalUProcess( handlerDTO );
        this.mRemoteProcessManagerClient.getLogger().info( "[RemoteProcessVitalization] [PRC] (Process: `{}`) <InstructionPerformed>", imageAddress );
        return response;
    }

    @AddressMapping("createRemoteUProcess")
    public RemoteVitalizationResponse createRemoteUProcess( UProcessMirrorDTO handlerDTO ) throws RemoteProcessLifecycleException {
        String imageAddress = handlerDTO.getImageAddress();
        this.mRemoteProcessManagerClient.getLogger().info( "[RemoteProcessCreation] [PRC] (Process: `{}`) <InstructionAccepted>", imageAddress );
        RemoteVitalizationResponse response = this.mRemoteProcessManagerClient.createLocalUProcess( handlerDTO, null );
        this.mRemoteProcessManagerClient.getLogger().info( "[RemoteProcessCreation] [PRC] (Process: `{}`) <InstructionPerformed>", imageAddress );
        return response;
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
