package com.walnut.odin.proc.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.odin.proc.dto.UProcessDTO;

import java.util.Map;

@Controller
@AddressMapping("com.walnut.odin.proc.server.PMSMethodIface.")
public class PMCController implements Pinenut {
    private LocalProcessManagerClient   mLocalProcessManagerClient;

    public PMCController( LocalProcessManagerClient localProcessManagerClient ) {
        this.mLocalProcessManagerClient = localProcessManagerClient;
    }

    @AddressMapping("start")
    public void start( String processId ) {
        this.mLocalProcessManagerClient.start( processId );
    }

    @AddressMapping("createProcess")
    public void createProcess(ExecutionImage image, UProcess parent, Map<String, String[]> startupArgs, Map<String, String[]> contextEnvironmentVars ) {
        this.mLocalProcessManagerClient.createProcess( image, parent, startupArgs,contextEnvironmentVars );
    }
}
