package com.task;

import java.net.URI;
import java.util.Map;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.hydra.grpc.server.GrpcAppointServer;
import com.pinecone.hydra.grpc.server.GrpcServerConfig;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcessStatus;
import com.pinecone.hydra.proc.event.ProcessEventHandler;
import com.pinecone.hydra.proc.image.ArchEntryPointRunnable;
import com.pinecone.hydra.proc.image.EntryPointRunnable;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.image.LocalHostedClassImage;
import com.walnut.archcraft.ender.EnderHydra;
import com.walnut.odin.proc.RemoteImageResolutionMode;
import com.walnut.odin.proc.entity.RemoteProcessCreationContext;
import com.walnut.odin.proc.entity.RemoteVitalizationResponse;
import com.walnut.odin.proc.entity.UProcessRuntimeMeta;
import com.walnut.odin.proc.server.RavenRemoteProcessManagerServer;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;
import com.walnut.odin.proc.server.transport.GenericRemoteProcessControlEventHooker;
import com.walnut.odin.proc.server.transport.grpc.GrpcRemoteProcessControlTransportFactory;

class Thor extends EnderHydra implements Pinenut {

    protected static final long GLADIATOR_CLIENT_ID = 10001L;

    public Thor(String[] args, CascadeSystem parent ) {
        super( args, null, parent );
    }

    @Override
    public void vitalize() throws Exception {
        RemoteProcessManagerServer server = this.createGrpcRemoteProcessServer();

        Debug.greenfs( "[GladiatorTest] Odin gRPC control server is listening on 5888." );
        Debug.greenfs( "[GladiatorTest] Start Knight/gladiator now, then wait for clientId " + GLADIATOR_CLIENT_ID + "." );

        this.awaitGladiator( server );
        this.createStartAndQuery( server );

        Debug.greenfs( "[GladiatorTest] Smoke commands finished. Keep JVM alive for stream observation." );
        Debug.sleep( 300000L );
    }

    protected RemoteProcessManagerServer createGrpcRemoteProcessServer() throws Exception {
        GrpcServerConfig config = new GrpcServerConfig(
                new JSONMaptron( "{ host: \"0.0.0.0\", port: 5888, permitKeepAliveWithoutCalls: true }" )
        );
        GrpcAppointServer grpcAppointServer = new GrpcAppointServer( "gladiator-control", 5888L, config, this );
        RemoteProcessManagerServer server = new RavenRemoteProcessManagerServer( this.processManager() );
        //this.mountGladiatorDemoImage( server );

        server.hookTransport(
                new GrpcRemoteProcessControlTransportFactory().create(
                        server,
                        grpcAppointServer,
                        new GenericRemoteProcessControlEventHooker( server.transportRegistry() )
                )
        );
        server.startService();

        return server;
    }

    protected void mountGladiatorDemoImage( RemoteProcessManagerServer server ) throws Exception {
        ProcessManager manager = this.processManager();
        ProcessEventHandler eventHandler = new ProcessEventHandler() {
            @Override
            public void fired( EntryPointRunnable runnable, UProcessStatus event ) {
                Debug.bluef( "[GladiatorTest] Fake image event:", event );
            }
        };

        ExecutionImage image = new LocalHostedClassImage( "echo", new ArchEntryPointRunnable( eventHandler ) {
            @Override
            public int main( Map<String, String> args ) {
                Debug.greenfs( "[GladiatorTest] Fake image invoked: " + args );
                return 0;
            }
        }, manager );

        server.registerLocalScopeExecutionImage( "gladiator/demo", image );
    }

    protected void awaitGladiator( RemoteProcessManagerServer server ) throws InterruptedException {
        long deadline = System.currentTimeMillis() + 120000L;
        while ( System.currentTimeMillis() < deadline ) {
            if ( server.hasClient( GLADIATOR_CLIENT_ID ) ) {
                Debug.greenfs( "[GladiatorTest] Gladiator client attached: " + GLADIATOR_CLIENT_ID );
                return;
            }
            Thread.sleep( 1000L );
        }
        throw new IllegalStateException( "Gladiator client did not attach before timeout: " + GLADIATOR_CLIENT_ID );
    }

    protected void createStartAndQuery( RemoteProcessManagerServer server ) throws Exception {
        RemoteProcessManagerServer.RemoteCreationResult creationResult = server.createRemoteUProcess(
                GLADIATOR_CLIENT_ID,
                RemoteProcessCreationContext.of(
                        new URI( "uofs:///gladiator/demo/echo" ),
                        this.getPID(),
                        Map.of( "message", "hello from odin" ),
                        Map.of( "GLADIATOR_MODE", "console" )
                ).withImageResolutionMode( RemoteImageResolutionMode.REMOTE_CLIENT_IMAGE )
        );

        Debug.greenfs( "[GladiatorTest] Create response: " + creationResult.getResponse().toJSONString() );

        if ( creationResult.getProcess() != null ) {
            server.startRemoteUProcess( creationResult.getProcess().getPID() );
            UProcessRuntimeMeta meta = server.queryProcessRuntimeMeta( creationResult.getProcess().getPID() );
            Debug.greenfs( "[GladiatorTest] Runtime meta: " + ( meta == null ? "null" : meta.toJSONString() ) );
        }

        RemoteVitalizationResponse vitalizeResponse = server.vitalizeRemoteUProcess(
                GLADIATOR_CLIENT_ID,
                RemoteProcessCreationContext.of(
                        "gladiator://console/direct-vitalize",
                        false,
                        this.getPID(),
                        Map.of( "direct", "true" ),
                        Map.of()
                ).withImageResolutionMode( RemoteImageResolutionMode.REMOTE_CLIENT_IMAGE )
        );
        Debug.greenfs( "[GladiatorTest] Vitalize response: " + BeanJSONEncoder.BasicEncoder.encode( vitalizeResponse ) );
    }

}

public class TestGladiator implements Pinenut {

    public static void main( String[] args ) throws Exception {
        Pinecone.init( ( Object... cfg ) -> {
            Thor dante = (Thor) Pinecone.sys().getTaskManager().add(
                    new Thor( args, Pinecone.sys() )
            );
            dante.vitalize();
            return 0;
        }, (Object[]) args );
    }

}
