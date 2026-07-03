package com.acorn.skynet.device.grpc.client;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.pinecone.framework.system.prototype.Pinenut;

public class GrpcCorrelationWaiter<T> implements Pinenut {

    protected final Map<String, CompletableFuture<T>> futureMap = new ConcurrentHashMap<>();

    public void prepare( String correlationGuid ) {
        this.futureMap.put( correlationGuid, new CompletableFuture<>() );
    }

    public void complete( String correlationGuid, T value ) {
        CompletableFuture<T> future = this.futureMap.remove( correlationGuid );
        if ( future != null ) {
            future.complete( value );
        }
    }

    public void completeExceptionally( String correlationGuid, Throwable throwable ) {
        CompletableFuture<T> future = this.futureMap.remove( correlationGuid );
        if ( future != null ) {
            future.completeExceptionally( throwable );
        }
    }

    public void completeAllExceptionally( Throwable throwable ) {
        for ( Map.Entry<String, CompletableFuture<T>> entry : this.futureMap.entrySet() ) {
            CompletableFuture<T> future = this.futureMap.remove( entry.getKey() );
            if ( future != null ) {
                future.completeExceptionally( throwable );
            }
        }
    }

    public T await( String correlationGuid, long timeoutMillis ) throws Exception {
        CompletableFuture<T> future = this.futureMap.get( correlationGuid );
        if ( future == null ) {
            throw new IllegalStateException( "No pending gRPC device lifecycle correlation: " + correlationGuid );
        }
        try {
            return future.get( timeoutMillis, TimeUnit.MILLISECONDS );
        }
        finally {
            this.futureMap.remove( correlationGuid );
        }
    }
}
