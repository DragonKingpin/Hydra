package com.walnut.odin.proc.server.transport.grpc;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.pinecone.framework.system.prototype.Pinenut;

public class GrpcCorrelationWaiter<T> implements Pinenut {

    protected Map<String, CompletableFuture<T>> mFutureMap;

    public GrpcCorrelationWaiter() {
        this.mFutureMap = new ConcurrentHashMap<>();
    }

    public CompletableFuture<T> prepare( String szCorrelationGuid ) {
        CompletableFuture<T> future = new CompletableFuture<>();
        this.mFutureMap.put( szCorrelationGuid, future );
        return future;
    }

    public T await( String szCorrelationGuid, long timeoutMillis ) throws Exception {
        CompletableFuture<T> future = this.mFutureMap.get( szCorrelationGuid );
        if ( future == null ) {
            return null;
        }

        try {
            return future.get( timeoutMillis, TimeUnit.MILLISECONDS );
        }
        finally {
            this.mFutureMap.remove( szCorrelationGuid );
        }
    }

    public void complete( String szCorrelationGuid, T result ) {
        CompletableFuture<T> future = this.mFutureMap.remove( szCorrelationGuid );
        if ( future != null ) {
            future.complete( result );
        }
    }

    public void completeExceptionally( String szCorrelationGuid, Throwable throwable ) {
        CompletableFuture<T> future = this.mFutureMap.remove( szCorrelationGuid );
        if ( future != null ) {
            future.completeExceptionally( throwable );
        }
    }

}
