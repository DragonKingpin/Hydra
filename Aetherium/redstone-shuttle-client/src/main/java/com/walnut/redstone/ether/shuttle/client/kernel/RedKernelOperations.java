package com.walnut.redstone.ether.shuttle.client.kernel;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.shuttle.client.object.RedObjectStream;

public interface RedKernelOperations extends Pinenut {
    RedObjectStream read( String szPath );

    RedObjectStream readUri( String szUri );
}
