package com.walnut.odin.proc.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;

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
}
