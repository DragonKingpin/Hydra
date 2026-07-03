package com.walnut.redstone.ether.shuttle.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.shuttle.client.engine.ObjectStorageEngine;
import com.walnut.redstone.ether.shuttle.client.engine.minio.MinioObjectStorageEngine;

public class RedShuttleClientBuilder implements Pinenut {
    protected final RedShuttleClientConfig config = new RedShuttleClientConfig();
    protected ObjectStorageEngine objectStorageEngine;

    public static RedShuttleClientBuilder create() {
        return new RedShuttleClientBuilder();
    }

    public RedShuttleClientBuilder defaultEndpoint( String szDefaultEndpoint ) {
        this.config.setDefaultEndpoint( szDefaultEndpoint );
        return this;
    }

    public RedShuttleClientBuilder systemBucket( String szSystemBucket ) {
        this.config.setSystemBucket( szSystemBucket );
        return this;
    }

    public RedShuttleClientBuilder credentials( String szAccessKey, String szSecretKey ) {
        this.config.setAccessKey( szAccessKey );
        this.config.setSecretKey( szSecretKey );
        return this;
    }

    public RedShuttleClientBuilder secure( boolean bSecure ) {
        this.config.setSecure( bSecure );
        return this;
    }

    public RedShuttleClientBuilder partSize( long nPartSize ) {
        this.config.setPartSize( nPartSize );
        return this;
    }

    public RedShuttleClientBuilder objectStorageEngine( ObjectStorageEngine objectStorageEngine ) {
        this.objectStorageEngine = objectStorageEngine;
        return this;
    }

    public RedShuttleClient build() {
        ObjectStorageEngine engine = this.objectStorageEngine;
        if ( engine == null ) {
            engine = new MinioObjectStorageEngine( this.config );
        }
        return new GenericRedShuttleClient( this.config, engine );
    }
}
