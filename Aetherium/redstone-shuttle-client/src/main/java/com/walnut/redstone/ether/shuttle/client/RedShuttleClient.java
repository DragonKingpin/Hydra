package com.walnut.redstone.ether.shuttle.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.shuttle.client.kernel.RedKernelOperations;
import com.walnut.redstone.ether.shuttle.client.object.RedObjectOperations;

public interface RedShuttleClient extends Pinenut {
    RedObjectOperations objects();

    RedKernelOperations kernel();
}
