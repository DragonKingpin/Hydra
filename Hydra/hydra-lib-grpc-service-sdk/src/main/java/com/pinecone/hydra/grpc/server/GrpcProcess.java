package com.pinecone.hydra.grpc.server;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.system.ProvokeHandleException;
import com.pinecone.framework.system.executum.ArchProcessum;
import com.pinecone.framework.system.executum.Processum;

public class GrpcProcess extends ArchProcessum {

    protected Logger log = LoggerFactory.getLogger( this.getClass() );

    protected Thread affinityThread;

    protected GrpcAppointServer grpcAppointServer;

    public GrpcProcess( GrpcAppointServer server, Processum parent ) {
        super( server.getName(), parent);
        this.grpcAppointServer = server;
    }

    @Override
    public void start() {
        if ( this.affinityThread != null ) {
            throw new IllegalStateException( "[GrpcAppointServer] Process has already started." );
        }

        CompletableFuture<Object> future = new CompletableFuture<>();

        this.affinityThread = new Thread(()->{
            try {
                this.grpcAppointServer.startGrpcServerOnly();
                log.info( "[GrpcAppointServer] Process has started. <Start>" );
                future.complete(null);
                this.grpcAppointServer.awaitTermination();
                log.info( "[GrpcAppointServer] Process has terminated. <Done>" );
            }
            catch ( Exception e ) {
                future.completeExceptionally( e );
            }
        });

        this.affinityThread.setName( ( this.getName() + "-main-" + this.affinityThread.getName() ).toLowerCase() );
        this.affinityThread.setDaemon( false );
        this.setThreadAffinity( this.affinityThread );
        this.affinityThread.start();


        try {
            Object e = future.get();
            log.info( "[GrpcAppointServer] Process redirect to parent thread. <Done>" );
            if ( future.isCompletedExceptionally() ) {
                if ( e instanceof Exception ) {
                    throw new ProvokeHandleException( ((Exception)e).getCause() );
                }
            }
        }
        catch ( InterruptedException e ) {
            Thread.currentThread().interrupt();
        }
        catch ( ExecutionException e ) {
            throw new ProvokeHandleException( e.getCause() );
        }
    }

}
