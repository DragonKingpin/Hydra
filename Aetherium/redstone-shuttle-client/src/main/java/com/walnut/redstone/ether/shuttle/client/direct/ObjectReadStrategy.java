package com.walnut.redstone.ether.shuttle.client.direct;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ObjectReadStrategy extends Pinenut {
    boolean supports( ObjectReadRequest request );

    ObjectReadStream read( ObjectReadRequest request );
}
