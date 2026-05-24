package com.auto_proc;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcessStatus;
import com.pinecone.hydra.proc.UniformProcessManager;
import com.pinecone.hydra.proc.event.ProcessEventHandler;
import com.pinecone.hydra.proc.image.ArchEntryPointRunnable;
import com.pinecone.hydra.proc.image.EntryPointRunnable;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.image.LocalHostedClassImage;
import com.pinecone.hydra.umc.wolf.client.UlfClient;
import com.pinecone.hydra.umc.wolf.client.WolfMCClient;
import com.pinecone.hydra.umc.wolf.server.WolfMCServer;
import com.walnut.archcraft.ender.EnderHydra;
import com.walnut.odin.proc.RemoteImageResolutionMode;
import com.walnut.odin.proc.RemoteVitalizationStatus;
import com.walnut.odin.proc.client.RavenRemoteProcessManagerClient;
import com.walnut.odin.proc.client.RemoteProcessManagerClient;
import com.walnut.odin.proc.entity.RemoteProcessCreationContext;
import com.walnut.odin.proc.entity.RemoteVitalizationResponse;
import com.walnut.odin.proc.server.RavenRemoteProcessManagerServer;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;
import com.walnut.odin.proc.server.transport.husky.HuskyRemoteProcessControlTransportFactory;

import java.net.BindException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BooleanSupplier;

class CodexHuskyReconnectDiceRig extends EnderHydra {

    protected static final int PORT = 5777;
    protected static final int RESTART_ROUNDS = 30;
    protected static final long READY_TIMEOUT_MILLIS = 20000L;

    protected RemoteProcessManagerClient client;
    protected long clientId;

    public CodexHuskyReconnectDiceRig( String[] args, CascadeSystem parent ) {
        super( args, "CodexHuskyReconnectDiceRig", parent );
    }

    @Override
    public void vitalize() throws Exception {
        ServerHarness server = this.startServerWithRetry( 0 );
        try {
            this.startClient();
            this.registerClientImage();

            this.awaitReady( server, 0 );
            this.assertControlRpcRoundTrip( server, 0 );

            for ( int i = 1; i <= RESTART_ROUNDS; ++i ) {
                Debug.greenfs( "[CodexHuskyReconnectDice] Restart round " + i + " <Start>" );
                this.stopServer( server );
                server = this.startServerWithRetry( i );
                this.awaitReady( server, i );
                this.assertControlRpcRoundTrip( server, i );
                Debug.greenfs( "[CodexHuskyReconnectDice] Restart round " + i + " <Done>" );
            }
        }
        finally {
            this.stopServer( server );
            if ( this.client != null ) {
                this.client.terminateService();
            }
        }
    }

    protected ServerHarness startServerWithRetry( int round ) throws Exception {
        Throwable lastFailure = null;
        long deadline = System.currentTimeMillis() + READY_TIMEOUT_MILLIS;
        while ( System.currentTimeMillis() < deadline ) {
            try {
                return this.startServer( round );
            }
            catch ( Throwable e ) {
                lastFailure = e;
                if ( !isBindFailure( e ) ) {
                    throw e;
                }
                LockSupport.parkNanos( TimeUnit.MILLISECONDS.toNanos( 100 ) );
            }
        }
        throw new IllegalStateException( "Unable to restart WolfMCServer on port " + PORT, lastFailure );
    }

    protected boolean isBindFailure( Throwable e ) {
        Throwable cursor = e;
        while ( cursor != null ) {
            if ( cursor instanceof BindException ) {
                return true;
            }
            cursor = cursor.getCause();
        }
        return false;
    }

    protected ServerHarness startServer( int round ) throws Exception {
        WolfMCServer wolfKing = new WolfMCServer( "", this, new JSONMaptron(
                "{host: \"0.0.0.0\", port: " + PORT + ", SocketTimeout: 800, KeepAliveTimeout: 3600, MaximumConnections: 1000000}"
        ) );
        RemoteProcessManagerServer server = new RavenRemoteProcessManagerServer( this.processManager() );
        server.hookTransport( HuskyRemoteProcessControlTransportFactory.create( server, wolfKing ) );
        server.startService();
        Debug.greenfs( "[CodexHuskyReconnectDice] Server round " + round + " started." );
        return new ServerHarness( wolfKing, server );
    }

    protected void stopServer( ServerHarness server ) {
        if ( server == null ) {
            return;
        }
        try {
            server.server.terminateService();
        }
        catch ( Throwable e ) {
            Debug.warn( "[CodexHuskyReconnectDice] Server terminate failed: " + e.getMessage() );
        }
    }

    protected void startClient() throws Exception {
        ProcessManager clientPM = new UniformProcessManager(
                this, null, "CodexDiceClientPM", "", null
        );
        UlfClient ulfClient = new WolfMCClient(
                this.getSystemGuidAllocator72().nextGUIDi64(),
                "",
                this,
                new JSONMaptron(
                        "{host: \"localhost\", port: " + PORT + ", SocketTimeout: 800, KeepAliveTimeout: 3600, " +
                                "ParallelChannels: 3, AutoReconnect: true}"
                )
        );
        this.client = new RavenRemoteProcessManagerClient( clientPM, ulfClient );
        this.client.startService();
        this.clientId = this.client.getClientId();
        Debug.greenfs( "[CodexHuskyReconnectDice] Client started: " + this.clientId );
    }

    protected void registerClientImage() {
        ProcessEventHandler eventHandler = new ProcessEventHandler() {
            @Override
            public void fired( EntryPointRunnable runnable, UProcessStatus event ) {
                Debug.bluef( "[CodexHuskyReconnectDice] Image event:", event );
            }
        };

        ExecutionImage image = new LocalHostedClassImage( "echo", new ArchEntryPointRunnable( eventHandler ) {
            @Override
            public int main( Map<String, String> args ) {
                Debug.greenfs( "[CodexHuskyReconnectDice] Remote image invoked: " + args );
                return 0;
            }
        }, this.client.localProcessManager() );

        this.client.registerLocalScopeExecutionImage( "codex/dice", image );
    }

    protected void awaitReady( ServerHarness server, int round ) {
        await( "client ready after restart round " + round, READY_TIMEOUT_MILLIS, () ->
                server.server.hasClient( this.clientId ) && server.server.isControlClientReady( this.clientId )
        );
        Debug.greenfs( "[CodexHuskyReconnectDice] Client ready after round " + round + "." );
    }

    protected void assertControlRpcRoundTrip( ServerHarness server, int round ) throws Exception {
        RemoteProcessManagerServer.RemoteCreationResult result = server.server.createRemoteUProcess(
                this.clientId,
                RemoteProcessCreationContext.of(
                        new URI( "uofs:///sys/public/global/exe/images/codex/dice/echo" ),
                        this.getPID(),
                        Map.of( "round", String.valueOf( round ) ),
                        Map.of( "CODEX_DICE_ROUND", String.valueOf( round ) )
                ).withImageResolutionMode( RemoteImageResolutionMode.REMOTE_CLIENT_IMAGE )
        );

        RemoteVitalizationResponse response = result.getResponse();
        if ( response == null ) {
            throw new IllegalStateException( "Round " + round + " returned null creation response." );
        }
        if ( response.getStatus() != RemoteVitalizationStatus.New.getCode() ) {
            throw new IllegalStateException( "Round " + round + " unexpected response: " + response.toJSONString() );
        }
        if ( result.getProcess() == null ) {
            throw new IllegalStateException( "Round " + round + " did not hook remote mirror: " + response.toJSONString() );
        }

        server.server.startRemoteUProcess( result.getProcess().getPID() );
        Debug.greenfs( "[CodexHuskyReconnectDice] Control RPC round trip passed: " + round );
    }

    protected static void await( String what, long timeoutMillis, BooleanSupplier condition ) {
        long deadline = System.currentTimeMillis() + timeoutMillis;
        while ( System.currentTimeMillis() < deadline ) {
            if ( condition.getAsBoolean() ) {
                return;
            }
            LockSupport.parkNanos( TimeUnit.MILLISECONDS.toNanos( 100 ) );
        }
        throw new IllegalStateException( "Timed out waiting for " + what + " after " + timeoutMillis + " ms." );
    }

    protected static class ServerHarness {
        protected final WolfMCServer wolfKing;
        protected final RemoteProcessManagerServer server;

        protected ServerHarness( WolfMCServer wolfKing, RemoteProcessManagerServer server ) {
            this.wolfKing = wolfKing;
            this.server = server;
        }
    }
}

public class CodexHuskyReconnectDiceTest {
    //@Test
    public void reconnectDice() throws Exception {
        Path workingPath = repositoryWorkingDirectory();
        if ( workingPath == null ) {
            throw new IllegalStateException( "Unable to locate repository working directory." );
        }

        List<String> command = new ArrayList<>();
        command.add( Paths.get( System.getProperty( "java.home" ), "bin", "java" ).toString() );
        command.add( "-cp" );
        command.add( testClasspath() );
        command.add( CodexHuskyReconnectDiceTest.class.getName() );
        command.add( "--workingPath=" + workingPath );
        command.add( "--config=" + workingPath.resolve( "system/setup/config.json5" ) );

        Path logPath = workingPath.resolve( "Sparta/sparta-core-console/target/codex-husky-reconnect-dice.log" );
        Process process = new ProcessBuilder( command )
                .directory( workingPath.toFile() )
                .redirectOutput( logPath.toFile() )
                .redirectErrorStream( true )
                .start();
        int exitCode = process.waitFor();
        if ( exitCode != 0 ) {
            throw new IllegalStateException( "Codex Husky reconnect dice child process exited with code " + exitCode + ". See " + logPath );
        }
    }

    public static void main( String[] args ) throws Exception {
        Path workingPath = repositoryWorkingDirectory();
        String[] startupArgs = args;
        if ( workingPath != null ) {
            System.setProperty( "user.dir", workingPath.toString() );
            if ( startupArgs == null || startupArgs.length == 0 ) {
                startupArgs = new String[] {
                        "--workingPath=" + workingPath,
                        "--config=" + workingPath.resolve( "system/setup/config.json5" )
                };
            }
        }
        final String[] rigArgs = startupArgs;
        int exitCode = Pinecone.init( ( Object... cfg ) -> {
            CodexHuskyReconnectDiceRig rig = (CodexHuskyReconnectDiceRig) Pinecone.sys().getTaskManager().add(
                    new CodexHuskyReconnectDiceRig( rigArgs, Pinecone.sys() )
            );
            rig.vitalize();
            return 0;
        }, (Object[]) rigArgs );
        System.exit( exitCode );
    }

    protected static Path repositoryWorkingDirectory() {
        Path cursor = Paths.get( System.getProperty( "user.dir" ) ).toAbsolutePath();
        while ( cursor != null ) {
            if ( Files.exists( cursor.resolve( "system/setup/config.json5" ) ) ) {
                return cursor;
            }
            cursor = cursor.getParent();
        }
        return null;
    }

    protected static String testClasspath() {
        String cp = System.getProperty( "surefire.test.class.path" );
        if ( cp == null || cp.isEmpty() ) {
            cp = System.getProperty( "java.class.path" );
        }
        return cp;
    }
}
