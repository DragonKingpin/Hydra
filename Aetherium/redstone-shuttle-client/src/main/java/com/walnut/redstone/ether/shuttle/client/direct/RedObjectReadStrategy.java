package com.walnut.redstone.ether.shuttle.client.direct;

import java.net.URI;
import java.util.Set;

import com.walnut.redstone.ether.shuttle.client.RedShuttleClient;
import com.walnut.redstone.ether.shuttle.client.object.RedObjectStream;

public class RedObjectReadStrategy implements ObjectReadStrategy {
    protected final Set<String> schemes = Set.of( "red", "uofs", "s3" );
    protected final RedShuttleClient redShuttleClient;
    protected final HttpObjectReadStrategy httpObjectReadStrategy;

    public RedObjectReadStrategy( RedShuttleClient redShuttleClient ) {
        if ( redShuttleClient == null ) {
            throw new IllegalArgumentException( "Red shuttle client is required." );
        }
        this.redShuttleClient = redShuttleClient;
        this.httpObjectReadStrategy = new HttpObjectReadStrategy();
    }

    @Override
    public boolean supports( ObjectReadRequest request ) {
        return request != null && this.schemes.contains( request.scheme() );
    }

    @Override
    public ObjectReadStream read( ObjectReadRequest request ) {
        if ( this.isExplicitRedHttpEndpoint( request ) ) {
            return this.httpObjectReadStrategy.read( this.toHttpRequest( request ) );
        }
        try {
            RedObjectStream stream = this.redShuttleClient.objects().get( request.getRawUri() );
            return new ObjectReadStream( stream, request.getRawUri(), stream.getMetadata() );
        }
        catch ( RuntimeException e ) {
            throw new ObjectReadException( "Failed to read object through red shuttle: " + request.getRawUri(), e );
        }
    }

    protected boolean isExplicitRedHttpEndpoint( ObjectReadRequest request ) {
        if ( request == null || request.getUri() == null ) {
            return false;
        }
        String scheme = request.scheme();
        if ( !"red".equals( scheme ) && !"uofs".equals( scheme ) ) {
            return false;
        }
        String authority = request.getUri().getAuthority();
        if ( authority == null || authority.trim().isEmpty() ) {
            return false;
        }
        return authority.contains( ":" )
                || "localhost".equalsIgnoreCase( authority )
                || authority.contains( "." );
    }

    protected ObjectReadRequest toHttpRequest( ObjectReadRequest request ) {
        URI uri = request.getUri();
        String raw = "http://" + uri.getAuthority() + this.pathAndQuery( uri );
        ObjectReadRequest ret = ObjectReadRequest.of( raw );
        ret.setHeaders( request.getHeaders() );
        return ret;
    }

    protected String pathAndQuery( URI uri ) {
        String path = uri.getRawPath();
        if ( path == null || path.isEmpty() ) {
            path = "/";
        }
        if ( uri.getRawQuery() == null || uri.getRawQuery().isEmpty() ) {
            return path;
        }
        return path + "?" + uri.getRawQuery();
    }
}
