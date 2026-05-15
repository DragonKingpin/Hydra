package com.pinecone.hydra.storage.volume.block.stripe;

import com.pinecone.framework.system.prototype.Pinenut;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface VolumeIoExecutor extends Pinenut {
    StripeIoResult execute( StripedIoPlan plan, ByteBuffer buffer, boolean write ) throws IOException;
}
