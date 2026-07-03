package com.walnut.redstone.ether.shuttle.client.kernel;

import com.walnut.redstone.ether.red.RedSchemes;
import com.walnut.redstone.ether.shuttle.client.RedShuttleClientConfig;
import com.walnut.redstone.ether.shuttle.client.object.RedObjectOperations;
import com.walnut.redstone.ether.shuttle.client.object.RedObjectStream;

public class GenericRedKernelOperations implements RedKernelOperations {
    protected final RedShuttleClientConfig config;
    protected final RedObjectOperations objectOperations;

    public GenericRedKernelOperations( RedShuttleClientConfig config, RedObjectOperations objectOperations ) {
        this.config = config;
        this.objectOperations = objectOperations;
    }

    @Override
    public RedObjectStream read( String szPath ) {
        return this.objectOperations.get( this.toSystemUri( szPath ) );
    }

    @Override
    public RedObjectStream readUri( String szUri ) {
        return this.objectOperations.get( szUri );
    }

    protected String toSystemUri( String szPath ) {
        String szBody = szPath == null ? "" : szPath;
        while ( szBody.startsWith( "/" ) ) {
            szBody = szBody.substring( 1 );
        }
        return RedSchemes.Red + "://" + this.systemBucket() + "/" + szBody;
    }

    protected String systemBucket() {
        if ( this.config == null || this.config.getSystemBucket() == null || this.config.getSystemBucket().trim().isEmpty() ) {
            return RedShuttleClientConfig.DefaultSystemBucket;
        }
        return this.config.getSystemBucket();
    }
}
