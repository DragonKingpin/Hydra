package com.walnut.redstone.ether.shuttle.lifecycle;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ShuttleLifecycle extends Pinenut {
    void start();

    void stop();

    boolean isRunning();
}
