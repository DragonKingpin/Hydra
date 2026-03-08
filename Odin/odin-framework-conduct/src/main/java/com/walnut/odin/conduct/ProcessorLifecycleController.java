package com.walnut.odin.conduct;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.odin.conduct.entity.RegimentJoinRequest;
import com.walnut.odin.conduct.entity.RegimentJoinResponse;

@Controller
@AddressMapping( "com.walnut.odin.conduct.ProcessorLifecycleIface." )
public class ProcessorLifecycleController implements Pinenut {

    private CollectiveTaskRegiment collectiveTaskRegiment;

    public ProcessorLifecycleController( CollectiveTaskRegiment collectiveTaskRegiment ) {
        this.collectiveTaskRegiment = collectiveTaskRegiment;
    }

    @AddressMapping( "joinRegiment" )
    public RegimentJoinResponse joinRegiment( RegimentJoinRequest request ) {
        return this.collectiveTaskRegiment.invokeJoinRegiment( request );
    }

}
