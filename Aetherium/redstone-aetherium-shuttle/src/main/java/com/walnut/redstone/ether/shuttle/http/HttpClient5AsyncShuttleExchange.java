package com.walnut.redstone.ether.shuttle.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.walnut.redstone.ether.shuttle.config.ShuttleConfig;
import com.walnut.redstone.ether.shuttle.config.ShuttleTargetConfig;
import com.walnut.redstone.ether.shuttle.error.ShuttleErrorCode;
import com.walnut.redstone.ether.shuttle.error.ShuttleException;
import com.walnut.redstone.ether.shuttle.exchange.ShuttleExchange;
import com.walnut.redstone.ether.shuttle.exchange.ShuttleRequest;
import com.walnut.redstone.ether.shuttle.exchange.ShuttleResponse;
import com.walnut.redstone.ether.shuttle.route.ShuttleTargetResolver;

import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;

public class HttpClient5AsyncShuttleExchange implements ShuttleExchange {
    protected final ShuttleConfig config;
    protected final CloseableHttpAsyncClient httpClient;
    protected final ShuttleTargetResolver targetResolver;

    public HttpClient5AsyncShuttleExchange( ShuttleConfig config, CloseableHttpAsyncClient httpClient ) {
        this.config = config;
        this.httpClient = httpClient;
        this.targetResolver = new ShuttleTargetResolver( config );
    }

    @Override
    public CompletableFuture<ShuttleResponse> exchange( ShuttleRequest request ) {
        CompletableFuture<ShuttleResponse> ret = new CompletableFuture<>();
        try {
            this.httpClient.execute( this.createHttpRequest( request ), new FutureCallback<SimpleHttpResponse>() {
                @Override
                public void completed( SimpleHttpResponse response ) {
                    ret.complete( HttpClient5AsyncShuttleExchange.this.toShuttleResponse( response ) );
                }

                @Override
                public void failed( Exception ex ) {
                    ret.completeExceptionally( ex );
                }

                @Override
                public void cancelled() {
                    ret.cancel( true );
                }
            } );
        }
        catch ( Exception ex ) {
            ret.completeExceptionally( ex );
        }
        return ret;
    }

    protected SimpleHttpRequest createHttpRequest( ShuttleRequest request ) throws IOException {
        if ( request == null || request.getMethod() == null ) {
            throw new ShuttleException( ShuttleErrorCode.InvalidRequest, "Shuttle request or method is null." );
        }
        SimpleHttpRequest ret = SimpleHttpRequest.create( request.getMethod().name(), this.createTargetUri( request ) );
        this.copyHeaders( request, ret );
        if ( request.getBodyStream() != null ) {
            ret.setBody( request.getBodyStream().readAllBytes(), this.contentType( request ) );
        }
        return ret;
    }

    protected URI createTargetUri( ShuttleRequest request ) {
        ShuttleTargetConfig target = this.targetResolver.resolve( request.getTargetName() );
        StringBuilder ret = new StringBuilder();
        ret.append( this.trimRightSlash( target.getBaseUrl() ) );
        ret.append( this.normalizePath( request.getPath() ) );
        if ( !this.blank( request.getQueryString() ) ) {
            ret.append( '?' ).append( request.getQueryString() );
        }
        return URI.create( ret.toString() );
    }

    protected void copyHeaders( ShuttleRequest request, SimpleHttpRequest httpRequest ) {
        if ( request.getHeaders() == null || request.getHeaders().isEmpty() ) {
            return;
        }
        Set<String> blockedHeaders = this.blockedHeaders();
        for ( Map.Entry<String, String> entry : request.getHeaders().entrySet() ) {
            String name = entry.getKey();
            if ( this.blank( name ) || blockedHeaders.contains( name.toLowerCase( Locale.ROOT ) ) ) {
                continue;
            }
            httpRequest.setHeader( name, entry.getValue() );
        }
    }

    protected ShuttleResponse toShuttleResponse( SimpleHttpResponse response ) {
        ShuttleResponse ret = new ShuttleResponse();
        ret.setStatusCode( response.getCode() );
        for ( Header header : response.getHeaders() ) {
            ret.getHeaders().put( header.getName(), header.getValue() );
        }
        byte[] body = response.getBodyBytes();
        ret.setBodyStream( new ByteArrayInputStream( body == null ? new byte[0] : body ) );
        return ret;
    }

    protected ContentType contentType( ShuttleRequest request ) {
        String contentType = this.headerValue( request, "content-type" );
        if ( this.blank( contentType ) ) {
            return ContentType.APPLICATION_OCTET_STREAM;
        }
        return ContentType.parse( contentType );
    }

    protected String headerValue( ShuttleRequest request, String name ) {
        if ( request.getHeaders() == null ) {
            return null;
        }
        for ( Map.Entry<String, String> entry : request.getHeaders().entrySet() ) {
            if ( entry.getKey() != null && entry.getKey().equalsIgnoreCase( name ) ) {
                return entry.getValue();
            }
        }
        return null;
    }

    protected Set<String> blockedHeaders() {
        return this.config.getHeaderPolicy().blockedHeaderSet();
    }

    protected String normalizePath( String path ) {
        if ( this.blank( path ) ) {
            return "/";
        }
        return path.startsWith( "/" ) ? path : "/" + path;
    }

    protected String trimRightSlash( String value ) {
        if ( value.endsWith( "/" ) ) {
            return value.substring( 0, value.length() - 1 );
        }
        return value;
    }

    protected boolean blank( String value ) {
        return value == null || value.trim().isEmpty();
    }
}
