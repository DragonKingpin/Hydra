package com.walnut.odin.proc.server;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.odin.proc.RavenRemoteProcess;
import com.walnut.odin.proc.dto.UProcessDTO;


@Controller
@AddressMapping("com.walnut.odin.proc.client.PMCMethodIface.")
public class PMSController implements Pinenut {
    protected RemoteProcessManagerServer  mRemoteProcessManagerServer;

    public PMSController( RemoteProcessManagerServer remoteProcessManagerServer ) {
        this.mRemoteProcessManagerServer = remoteProcessManagerServer;
    }

    @AddressMapping("createProcess")
    public void createProcess( long pmcId, UProcessDTO processDTO ) {
        this.mRemoteProcessManagerServer.registerProcess( pmcId, processDTO );
    }

    @AddressMapping("test")
    public void test() {
        Debug.trace("这是测试");
    }

}
