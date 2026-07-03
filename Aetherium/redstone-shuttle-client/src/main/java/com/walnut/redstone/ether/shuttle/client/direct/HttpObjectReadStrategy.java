package com.walnut.redstone.ether.shuttle.client.direct;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Set;

import com.walnut.redstone.ether.object.ObjectMetadata;

public class HttpObjectReadStrategy implements ObjectReadStrategy {
    protected final Set<String> schemes = Set.of( "http", "https" );
    protected final HttpClient httpClient;
    protected final Duration timeout;

    public HttpObjectReadStrategy() {
        this( HttpClient.newHttpClient(), Duration.ofSeconds( 30L ) );
    }

    public HttpObjectReadStrategy( HttpClient httpClient, Duration timeout ) {
        if ( httpClient == null ) {
            throw new IllegalArgumentException( "Http client is required." );
        }
        this.httpClient = httpClient;
        this.timeout = timeout == null ? Duration.ofSeconds( 30L ) : timeout;
    }

    @Override
    public boolean supports( ObjectReadRequest request ) {
        return request != null && this.schemes.contains( request.scheme() );
    }

    @Override
    public ObjectReadStream read( ObjectReadRequest request ) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder( request.getUri() )
                    .timeout( this.timeout )
                    .GET();
            for ( Map.Entry<String, String> entry : request.getHeaders().entrySet() ) {
                builder.header( entry.getKey(), entry.getValue() );
            }
            HttpResponse<InputStream> response = this.httpClient.send( builder.build(), HttpResponse.BodyHandlers.ofInputStream() );
            if ( response.statusCode() < 200 || response.statusCode() >= 300 ) {
                response.body().close();
                throw new ObjectReadException( "HTTP object read failed: status=" + response.statusCode() + ", uri=" + request.getRawUri() );
            }
            return new ObjectReadStream( response.body(), request.getRawUri(), this.metadata( request.getUri(), response ) );
        }
        catch ( IOException e ) {
            throw new ObjectReadException( "Failed to read HTTP object: " + request.getRawUri(), e );
        }
        catch ( InterruptedException e ) {
            Thread.currentThread().interrupt();
            throw new ObjectReadException( "HTTP object read interrupted: " + request.getRawUri(), e );
        }
    }

    protected ObjectMetadata metadata( URI uri, HttpResponse<?> response ) {
        ObjectMetadata ret = new ObjectMetadata();
        ret.setName( this.fileName( uri ) );
        response.headers().firstValue( "Content-Length" ).ifPresent( value -> ret.setSize( this.parseLong( value ) ) );
        response.headers().firstValue( "Content-Type" ).ifPresent( ret::setContentType );
        response.headers().firstValue( "ETag" ).ifPresent( ret::setEtag );
        return ret;
    }

    protected Long parseLong( String value ) {
        try {
            return Long.parseLong( value );
        }
        catch ( NumberFormatException e ) {
            return null;
        }
    }

    protected String fileName( URI uri ) {
        String path = uri.getPath();
        if ( path == null || path.isEmpty() || path.endsWith( "/" ) ) {
            return null;
        }
        int index = path.lastIndexOf( '/' );
        if ( index < 0 ) {
            return path;
        }
        return path.substring( index + 1 );
    }
}
