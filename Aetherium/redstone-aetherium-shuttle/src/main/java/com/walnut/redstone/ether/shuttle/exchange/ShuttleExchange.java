package com.walnut.redstone.ether.shuttle.exchange;

import java.util.concurrent.CompletableFuture;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ShuttleExchange extends Pinenut {
    CompletableFuture<ShuttleResponse> exchange( ShuttleRequest request );
}
