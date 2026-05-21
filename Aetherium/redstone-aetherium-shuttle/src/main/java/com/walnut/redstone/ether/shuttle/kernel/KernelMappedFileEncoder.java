package com.walnut.redstone.ether.shuttle.kernel;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.shuttle.exchange.ShuttleRequest;
import com.walnut.redstone.ether.shuttle.exchange.ShuttleResponse;

public interface KernelMappedFileEncoder extends Pinenut {
    ShuttleResponse encode( KernelMappedFile file, ShuttleRequest request );
}
