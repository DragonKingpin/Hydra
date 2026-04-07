package com.walnut.odin.conduct;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umct.stereotype.Iface;
import com.walnut.odin.conduct.entity.RegimentJoinRequest;
import com.walnut.odin.conduct.entity.RegimentJoinResponse;

@Iface
public interface ProcessorLifecycleIface extends Pinenut {

    RegimentJoinResponse joinRegiment( RegimentJoinRequest request );

}
