package com.acorn.redqueen.service.registry.grpc.client;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.pinecone.framework.system.prototype.Pinenut;

public class GrpcCorrelationWaiter<T> implements Pinenut {

    protected Map<String, CompletableFuture<T>> mFutureMap = new ConcurrentHashMap<>();

    public void prepare( String szCorrelationGuid ) {
        this.mFutureMap.put( szCorrelationGuid, new CompletableFuture<>() );
    }

    public void complete( String szCorrelationGuid, T value ) {
        CompletableFuture<T> future = this.mFutureMap.remove( szCorrelationGuid );
        if ( future != null ) {
            future.complete( value );
        }
    }

    public void completeExceptionally( String szCorrelationGuid, Throwable throwable ) {
        CompletableFuture<T> future = this.mFutureMap.remove( szCorrelationGuid );
        if ( future != null ) {
            future.completeExceptionally( throwable );
        }
    }

    public void completeAllExceptionally( Throwable throwable ) {
        for ( Map.Entry<String, CompletableFuture<T>> entry : this.mFutureMap.entrySet() ) {
            CompletableFuture<T> future = this.mFutureMap.remove( entry.getKey() );
            if ( future != null ) {
                future.completeExceptionally( throwable );
            }
        }
    }

    public T await( String szCorrelationGuid, long nTimeoutMillis ) throws Exception {
        CompletableFuture<T> future = this.mFutureMap.get( szCorrelationGuid );
        if ( future == null ) {
            throw new IllegalStateException( "No pending gRPC correlation: " + szCorrelationGuid );
        }
        try {
            return future.get( nTimeoutMillis, TimeUnit.MILLISECONDS );
        }
        finally {
            this.mFutureMap.remove( szCorrelationGuid );
        }
    }

}



