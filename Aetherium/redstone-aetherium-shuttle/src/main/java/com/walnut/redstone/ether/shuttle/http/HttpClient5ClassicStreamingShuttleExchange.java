package com.walnut.redstone.ether.shuttle.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.walnut.redstone.ether.red.RedHeaders;
import com.walnut.redstone.ether.shuttle.config.ShuttleConfig;
import com.walnut.redstone.ether.shuttle.config.ShuttleTargetConfig;
import com.walnut.redstone.ether.shuttle.error.ShuttleErrorCode;
import com.walnut.redstone.ether.shuttle.error.ShuttleException;
import com.walnut.redstone.ether.shuttle.exchange.ShuttleExchange;
import com.walnut.redstone.ether.shuttle.exchange.ShuttleRequest;
import com.walnut.redstone.ether.shuttle.exchange.ShuttleResponse;
import com.walnut.redstone.ether.shuttle.route.ShuttleTargetResolver;

import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpHead;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;

public class HttpClient5ClassicStreamingShuttleExchange implements ShuttleExchange {
    protected final ShuttleConfig config;
    protected final CloseableHttpClient httpClient;
    protected final ShuttleTargetResolver targetResolver;

    public HttpClient5ClassicStreamingShuttleExchange( ShuttleConfig config, CloseableHttpClient httpClient ) {
        this.config = config;
        this.httpClient = httpClient;
        this.targetResolver = new ShuttleTargetResolver( config );
    }

    @Override
    public CompletableFuture<ShuttleResponse> exchange( ShuttleRequest request ) {
        CompletableFuture<ShuttleResponse> ret = new CompletableFuture<>();
        try {
            CloseableHttpResponse response = this.httpClient.execute( this.createHttpRequest( request ) );
            ret.complete( this.toShuttleResponse( response ) );
        }
        catch ( Exception ex ) {
            ret.completeExceptionally( ex );
        }
        return ret;
    }

    protected HttpUriRequestBase createHttpRequest( ShuttleRequest request ) throws IOException {
        if ( request == null || request.getMethod() == null ) {
            throw new ShuttleException( ShuttleErrorCode.InvalidRequest, "Shuttle request or method is null." );
        }
        HttpUriRequestBase ret;
        URI uri = this.createTargetUri( request );
        switch ( request.getMethod() ) {
            case GET:
                ret = new HttpGet( uri );
                break;
            case HEAD:
                ret = new HttpHead( uri );
                break;
            case PUT:
                ret = new HttpPut( uri );
                break;
            case POST:
                ret = new HttpPost( uri );
                break;
            case DELETE:
                ret = new HttpDelete( uri );
                break;
            case PATCH:
                ret = new HttpPatch( uri );
                break;
            default:
                throw new ShuttleException( ShuttleErrorCode.InvalidRequest, "Unsupported shuttle method: " + request.getMethod() );
        }
        this.copyHeaders( request, ret );
        this.setEntity( request, ret );
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

    protected void copyHeaders( ShuttleRequest request, HttpUriRequestBase httpRequest ) {
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

    protected void setEntity( ShuttleRequest request, HttpUriRequestBase httpRequest ) {
        if ( request.getBodyStream() == null ) {
            return;
        }
        if ( request.getContentLength() == null || request.getContentLength() < 0L ) {
            throw new ShuttleException( ShuttleErrorCode.InvalidRequest, RedHeaders.ContentLength + " is required for streaming shuttle request body." );
        }
        httpRequest.setEntity( new InputStreamEntity(
                request.getBodyStream(),
                request.getContentLength(),
                this.contentType( request )
        ) );
    }

    protected ShuttleResponse toShuttleResponse( CloseableHttpResponse response ) throws IOException {
        ShuttleResponse ret = new ShuttleResponse();
        ret.setStatusCode( response.getCode() );
        for ( Header header : response.getHeaders() ) {
            ret.getHeaders().put( header.getName(), header.getValue() );
        }
        HttpEntity entity = response.getEntity();
        if ( entity == null ) {
            ret.setBodyStream( new CloseableShuttleInputStream( new ByteArrayInputStream( new byte[0] ), response ) );
            return ret;
        }
        InputStream bodyStream = entity.getContent();
        ret.setBodyStream( new CloseableShuttleInputStream( bodyStream, response ) );
        return ret;
    }

    protected ContentType contentType( ShuttleRequest request ) {
        String contentType = this.headerValue( request, HttpHeaders.CONTENT_TYPE );
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
