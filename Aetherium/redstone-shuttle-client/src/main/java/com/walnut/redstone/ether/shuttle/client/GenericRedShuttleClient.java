package com.walnut.redstone.ether.shuttle.client;

import com.walnut.redstone.ether.shuttle.client.engine.ObjectStorageEngine;
import com.walnut.redstone.ether.shuttle.client.kernel.GenericRedKernelOperations;
import com.walnut.redstone.ether.shuttle.client.kernel.RedKernelOperations;
import com.walnut.redstone.ether.shuttle.client.object.GenericRedObjectOperations;
import com.walnut.redstone.ether.shuttle.client.object.RedObjectOperations;
import com.walnut.redstone.ether.shuttle.client.uri.RedShuttleEndpointResolver;
import com.walnut.redstone.ether.shuttle.client.uri.RedShuttleUriParser;

public class GenericRedShuttleClient implements RedShuttleClient {
    protected final RedObjectOperations objectOperations;
    protected final RedKernelOperations kernelOperations;

    public GenericRedShuttleClient(
            RedShuttleClientConfig config,
            ObjectStorageEngine objectStorageEngine
    ) {
        RedShuttleUriParser uriParser = new RedShuttleUriParser( config.getSystemBucket() );
        RedShuttleEndpointResolver endpointResolver = new RedShuttleEndpointResolver( config );
        this.objectOperations = new GenericRedObjectOperations( uriParser, endpointResolver, objectStorageEngine );
        this.kernelOperations = new GenericRedKernelOperations( config, this.objectOperations );
    }

    @Override
    public RedObjectOperations objects() {
        return this.objectOperations;
    }

    @Override
    public RedKernelOperations kernel() {
        return this.kernelOperations;
    }
}
