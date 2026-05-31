package com.walnut.redstone.ether.shuttle.kernel;

import java.io.ByteArrayInputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.walnut.redstone.ether.red.RedHeaders;
import com.walnut.redstone.ether.shuttle.exchange.ShuttleMethod;
import com.walnut.redstone.ether.shuttle.exchange.ShuttleRequest;
import com.walnut.redstone.ether.shuttle.exchange.ShuttleResponse;

public class KernelNamespaceExchange {
    protected final KernelNamespaceBackend    mBackend;
    protected final KernelMappedFileEncoder   mEncoder;

    public KernelNamespaceExchange( KernelNamespaceBackend backend, KernelMappedFileEncoder encoder ) {
        this.mBackend = backend;
        this.mEncoder = encoder == null ? new GenericKernelMappedFileEncoder() : encoder;
    }

    public CompletableFuture<ShuttleResponse> exchange( ShuttleRequest request ) {
        if ( this.mBackend == null ) {
            return CompletableFuture.completedFuture( this.empty( 503 ) );
        }
        if ( request.getMethod() == ShuttleMethod.GET ) {
            return CompletableFuture.completedFuture( this.mEncoder.encode( this.mBackend.read( request.getPath() ), request ) );
        }
        if ( request.getMethod() == ShuttleMethod.HEAD ) {
            return CompletableFuture.completedFuture( this.head( request.getPath() ) );
        }
        return CompletableFuture.completedFuture( this.methodNotAllowed() );
    }

    protected ShuttleResponse head( String szPath ) {
        KernelMappedFileMeta meta = this.mBackend.stat( szPath );
        if ( meta == null || !meta.isExists() ) {
            return this.empty( 404 );
        }
        ShuttleResponse response = this.empty( 200 );
        response.getHeaders().put( RedHeaders.ContentType, meta.getContentType() == null ? GenericKernelMappedFileEncoder.ContentTypeJson : meta.getContentType() );
        response.getHeaders().put( RedHeaders.KernelReadonly, String.valueOf( !meta.isWritable() ) );
        if ( meta.getContentLength() != null ) {
            response.getHeaders().put( RedHeaders.ContentLength, String.valueOf( meta.getContentLength() ) );
        }
        if ( meta.getHeaders() != null ) {
            response.getHeaders().putAll( meta.getHeaders() );
        }
        return response;
    }

    protected ShuttleResponse methodNotAllowed() {
        ShuttleResponse response = this.empty( 405 );
        response.getHeaders().put( "Allow", "GET, HEAD" );
        response.getHeaders().put( RedHeaders.KernelReadonly, "true" );
        return response;
    }

    protected ShuttleResponse empty( int nStatusCode ) {
        ShuttleResponse response = new ShuttleResponse();
        response.setStatusCode( nStatusCode );
        response.setHeaders( new LinkedHashMap<>( Map.of( RedHeaders.ContentLength, "0" ) ) );
        response.setBodyStream( new ByteArrayInputStream( new byte[0] ) );
        return response;
    }
}
