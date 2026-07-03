package com.walnut.redstone.ether.shuttle.client.object;

import java.io.InputStream;

import com.walnut.redstone.ether.object.ObjectListResult;
import com.walnut.redstone.ether.object.ObjectMetadata;
import com.walnut.redstone.ether.shuttle.client.engine.ObjectStorageEngine;
import com.walnut.redstone.ether.shuttle.client.uri.RedShuttleEndpointResolver;
import com.walnut.redstone.ether.shuttle.client.uri.RedShuttleUri;
import com.walnut.redstone.ether.shuttle.client.uri.RedShuttleUriParser;

public class GenericRedObjectOperations implements RedObjectOperations {
    protected final RedShuttleUriParser uriParser;
    protected final RedShuttleEndpointResolver endpointResolver;
    protected final ObjectStorageEngine objectStorageEngine;

    public GenericRedObjectOperations(
            RedShuttleUriParser uriParser,
            RedShuttleEndpointResolver endpointResolver,
            ObjectStorageEngine objectStorageEngine
    ) {
        this.uriParser = uriParser;
        this.endpointResolver = endpointResolver;
        this.objectStorageEngine = objectStorageEngine;
    }

    @Override
    public RedObjectStream get( String szUri ) {
        return this.get( szUri, RedObjectGetOptions.empty() );
    }

    @Override
    public RedObjectStream get( String szUri, RedObjectGetOptions options ) {
        return this.get( this.toObjectRef( this.uriParser.parse( szUri ) ), options );
    }

    @Override
    public RedObjectStream get( RedObjectRef ref, RedObjectGetOptions options ) {
        return this.objectStorageEngine.getObject( ref, options == null ? RedObjectGetOptions.empty() : options );
    }

    @Override
    public ObjectMetadata stat( String szUri ) {
        return this.stat( this.toObjectRef( this.uriParser.parse( szUri ) ) );
    }

    @Override
    public ObjectMetadata stat( RedObjectRef ref ) {
        return this.objectStorageEngine.statObject( ref );
    }

    @Override
    public RedObjectPutResult put( String szUri, InputStream inputStream, long nSize, RedObjectPutOptions options ) {
        return this.put( this.toObjectRef( this.uriParser.parse( szUri ) ), inputStream, nSize, options );
    }

    @Override
    public RedObjectPutResult put( RedObjectRef ref, InputStream inputStream, long nSize, RedObjectPutOptions options ) {
        return this.objectStorageEngine.putObject(
                ref,
                inputStream,
                nSize,
                options == null ? RedObjectPutOptions.empty() : options
        );
    }

    @Override
    public void delete( String szUri ) {
        this.delete( this.toObjectRef( this.uriParser.parse( szUri ) ) );
    }

    @Override
    public void delete( RedObjectRef ref ) {
        this.objectStorageEngine.deleteObject( ref );
    }

    @Override
    public ObjectListResult list( String szUri, RedObjectListOptions options ) {
        return this.list( this.toBucketRef( this.uriParser.parse( szUri ) ), options );
    }

    @Override
    public ObjectListResult list( RedBucketRef bucketRef, RedObjectListOptions options ) {
        return this.objectStorageEngine.listObjects(
                bucketRef,
                options == null ? RedObjectListOptions.empty() : options
        );
    }

    protected RedObjectRef toObjectRef( RedShuttleUri uri ) {
        RedObjectRef ret = new RedObjectRef();
        ret.setEndpoint( this.endpointResolver.resolveEndpoint( uri ) );
        ret.setBucket( uri.getBucket() );
        ret.setKey( uri.getKey() );
        ret.setResourceType( uri.getResourceType() );
        return ret;
    }

    protected RedBucketRef toBucketRef( RedShuttleUri uri ) {
        RedBucketRef ret = new RedBucketRef();
        ret.setEndpoint( this.endpointResolver.resolveEndpoint( uri ) );
        ret.setBucket( uri.getBucket() );
        ret.setPrefix( uri.getKey() );
        return ret;
    }
}
