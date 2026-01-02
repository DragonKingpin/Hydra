package com.walnut.archcraft.redstone.response;

import java.util.function.Supplier;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.system.HyComponent;

public interface ResponseObjectManager extends Pinenut, HyComponent {

    String nextTraceId();

    <T extends RedTraceableResponse> T newResponse(Supplier<T> cons);

}
