package com.walnut.odin.proc.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnut.odin.proc.RemoteProcessLifecycleException;
import com.walnut.odin.proc.dto.RemoteVitalizationResponse;
import com.walnut.odin.proc.dto.UProcessMirrorDTO;

@Controller
@AddressMapping( "com.walnut.odin.proc.server.MasterProcessLifecycleIface." )
public class ReactiveMasterProcessLifecycleController implements Pinenut {

    private RemoteProcessManagerClient mRemoteProcessManagerClient;

    public ReactiveMasterProcessLifecycleController( RemoteProcessManagerClient remoteProcessManagerClient ) {
        this.mRemoteProcessManagerClient = remoteProcessManagerClient;
    }

    @AddressMapping("startRemoteUProcess")
    public void startRemoteUProcess( String szPid ) {
        this.mRemoteProcessManagerClient.startLocalUProcess( GUIDs.GUID128(szPid) );
    }

    @AddressMapping("vitalizeRemoteUProcess")
    public RemoteVitalizationResponse vitalizeRemoteUProcess( String imageAddress, boolean isURI, UProcessMirrorDTO handlerDTO ) throws RemoteProcessLifecycleException {
        this.mRemoteProcessManagerClient.getLogger().info( "[RemoteProcessVitalization] [PRC] (Process: `{}`) <InstructionAccepted>", imageAddress );
        RemoteVitalizationResponse response = this.mRemoteProcessManagerClient.vitalizeLocalUProcess( imageAddress, isURI, handlerDTO );
        this.mRemoteProcessManagerClient.getLogger().info( "[RemoteProcessVitalization] [PRC] (Process: `{}`) <InstructionPerformed>", imageAddress );
        return response;
    }

    @AddressMapping("createRemoteUProcess")
    public RemoteVitalizationResponse createRemoteUProcess( String imageAddress, boolean isURI, UProcessMirrorDTO handlerDTO ) throws RemoteProcessLifecycleException {
        this.mRemoteProcessManagerClient.getLogger().info( "[RemoteProcessCreation] [PRC] (Process: `{}`) <InstructionAccepted>", imageAddress );
        RemoteVitalizationResponse response = this.mRemoteProcessManagerClient.createLocalUProcess( imageAddress, isURI, handlerDTO, null );
        this.mRemoteProcessManagerClient.getLogger().info( "[RemoteProcessCreation] [PRC] (Process: `{}`) <InstructionPerformed>", imageAddress );
        return response;
    }

}
