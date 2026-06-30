package com.walnut.redstone.ether.shuttle.client.direct;

import java.net.URI;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ObjectReadClient extends Pinenut {
    boolean supports( String uri );

    boolean supports( URI uri );

    boolean supports( ObjectReadRequest request );

    ObjectReadStream read( String uri );

    ObjectReadStream read( URI uri );

    ObjectReadStream read( ObjectReadRequest request );
}
