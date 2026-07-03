package com.acorn.skynet.device.husky.client;

import java.util.function.Supplier;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.umc.wolf.client.UlfClient;

public class HuskyDeviceClientTransportFactory implements Pinenut {

    public HuskyDeviceClientTransport create( UlfClient rpcClient ) {
        return new HuskyDeviceClientTransport( rpcClient );
    }

    public HuskyDeviceClientTransport create( UlfClient rpcClient, GuidAllocator guidAllocator ) {
        return new HuskyDeviceClientTransport( rpcClient, guidAllocator );
    }

    public HuskyDeviceClientTransport create(
            UlfClient rpcClient,
            GuidAllocator guidAllocator,
            Supplier<UlfClient> rpcClientSupplier
    ) {
        return new HuskyDeviceClientTransport( rpcClient, guidAllocator, rpcClientSupplier );
    }
}
