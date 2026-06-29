package com.task;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.UProcessStatus;
import com.pinecone.hydra.proc.UniformProcessManager;
import com.pinecone.hydra.proc.event.ProcessEventHandler;
import com.pinecone.hydra.proc.image.ArchEntryPointRunnable;
import com.pinecone.hydra.proc.image.EntryPointRunnable;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.image.LocalHostedClassImage;
import com.pinecone.hydra.proc.image.kom.VirtualExeImageInstrument;
import com.pinecone.hydra.proc.image.kom.VirtualMappingExeImageInstrument;
import com.pinecone.hydra.umc.wolf.client.UlfClient;
import com.pinecone.hydra.umc.wolf.client.WolfMCClient;
import com.pinecone.hydra.umc.wolf.server.WolfMCServer;
import com.walnut.archcraft.ender.EnderHydra;
import com.walnut.odin.proc.client.RavenRemoteProcessManagerClient;
import com.walnut.odin.proc.client.RemoteProcessManagerClient;
import com.walnut.odin.proc.entity.RemoteVitalizationResponse;
import com.walnut.odin.proc.entity.UProcessRuntimeMeta;
import com.walnut.odin.proc.server.RavenRemoteProcessManagerServer;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;
import com.walnut.odin.proc.server.transport.husky.HuskyRemoteProcessControlTransportFactory;

import java.net.URI;
import java.util.Collection;
import java.util.Map;

class Dante extends EnderHydra {
    public Dante( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Dante( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {
        WolfMCServer wolfKing = new WolfMCServer( "", this, new JSONMaptron("{host: \"0.0.0.0\",\n" +
                "port: 5777, SocketTimeout: 800, KeepAliveTimeout: 3600, MaximumConnections: 1e6}") );
        RemoteProcessManagerServer server = new RavenRemoteProcessManagerServer( this.processManager() );
        server.hookTransport( HuskyRemoteProcessControlTransportFactory.create( server, wolfKing ) );
        // Single machine test, need to open the following code.
        //server.startService();



        ProcessManager clientPM = new UniformProcessManager(
                this, null, "Miao", "", null
        );
        UlfClient ulfClient = new WolfMCClient(
                this.getSystemGuidAllocator72().nextGUIDi64(), "", this, this.getMiddlewareDirector().getMiddlewareConfig().queryJSONObject( "Messagers.Messagers.WolfMCKingpin" )
        );
        RemoteProcessManagerClient client = new RavenRemoteProcessManagerClient( clientPM, ulfClient );
        client.startService();


        //this.testClientProactiveCreation( server, client );
        this.testServerProactiveCreation( server, client );

        //this.testImageInstrument();
    }

    private void testImageInstrument() {
        VirtualExeImageInstrument imageInstrument = new VirtualMappingExeImageInstrument( this, "" );

        ProcessManager manager = this.processManager();
        ProcessEventHandler eventHandler = new ProcessEventHandler() {
            @Override
            public void fired(EntryPointRunnable runnable, UProcessStatus event ) {
                Debug.bluef( runnable, event );
            }
        };

        ExecutionImage image = new LocalHostedClassImage( "image1", () -> new ArchEntryPointRunnable( eventHandler ) {
            @Override
            public int main( Map<String, String> args ) {
                Debug.greenfs( "Hello, hi, I am `" + this.ownedProcess().getName() + "`!" );
                return 0;
            }
        }, manager );


        imageInstrument.mount( "hola/senorita", image );

        Debug.greenfs( imageInstrument.queryImage( "hola/senorita/image1" ).getName() );
    }

    private void testClientProactiveCreation( RemoteProcessManagerServer server, RemoteProcessManagerClient client ) throws Exception {
        ProcessManager manager = this.processManager();

        ProcessEventHandler eventHandler = new ProcessEventHandler() {
            @Override
            public void fired( EntryPointRunnable runnable, UProcessStatus event ) {
                Debug.bluef( runnable, event );
            }
        };

        ExecutionImage image = new LocalHostedClassImage( "gay", () -> new ArchEntryPointRunnable( eventHandler ) {
            @Override
            public int main( Map<String, String> args ) {
                Debug.greenfs( "Hello, hi, I am `" + this.ownedProcess().getName() + "`!" );
                Debug.greenfs( this.ownedProcess().getPID() );
                Debug.greenfs( this.ownedProcess().getLocalPID() );

                Debug.greenfs( this.ownedProcess().getEnvironmentVariables() );
                Debug.greenfs( this.ownedProcess().getStartupArguments() );
                Debug.bluef( this.ownedProcess().getControllableLevel() );
                Debug.bluef( this.ownedProcess().getOwnedProcessManager() );
                Debug.greenfs( this.ownedProcess().parentProcess() );

                return 0;
            }
        }, manager );

        //LocalUProcess process = manager.createLocalHostedProcess( image, null, Map.of( "fuck", "you,she,he,it" ) );


        UProcess process = client.createLocalUProcess(image, null, Map.of("fuck", "you,she,he,it"), null);
        server.startRemoteUProcess( process.getGuid() );
    }

    private void testServerProactiveCreation( RemoteProcessManagerServer server, RemoteProcessManagerClient client ) throws Exception {
        ProcessManager manager = this.processManager();
        ProcessEventHandler eventHandler = new ProcessEventHandler() {
            @Override
            public void fired(EntryPointRunnable runnable, UProcessStatus event ) {
                Debug.bluef( runnable, event );
            }
        };

        ExecutionImage image = new LocalHostedClassImage( "image_c", () -> new ArchEntryPointRunnable( eventHandler ) {
            @Override
            public int main( Map<String, String> args ) {
                Debug.greenfs( "Hello, hi, I am `" + this.ownedProcess().getName() + "`!" );
                Debug.sleep( 1000 );
                Debug.greenfs( "Miao~" );
                return 1984;
            }
        }, manager );




        client.registerLocalScopeExecutionImage( "hola/senorita", image );

        ExecutionImage ic = client.queryExecutionImage( "hola/senorita/image_c" );
        ExecutionImage ig = client.queryExecutionImage( "/sys/public/global/exe/images/hola/senorita/image_c" );

        Debug.redfs( ic, ig );

        ic = client.queryExecutionImage( new URI("uofs:///hola/senorita/image_c") );
        ig = client.queryExecutionImage( new URI("uofs:///sys/public/global/exe/images/hola/senorita/image_c") );

        Debug.redfs( ic, ig );

        // Single machine test, need to open the following code.
//        RemoteVitalizationResponse response = server.vitalizeRemoteUProcess(
//                client.getClientId(), new URI("uofs:///sys/public/global/exe/images/hola/senorita/image_c"), this.getPID(),
//                Map.of("fuck", "you,she,he,it"), Map.of("kill", "you,she,he,it")
//        );
//
//        Collection<UProcess> ps = server.searchProcessesByName( "image_c" );
//        UProcess proc = ps.iterator().next();
//        Debug.greenfs( proc.getName() );
//
//        UProcessRuntimeMeta meta = server.queryProcessRuntimeMeta( proc.getPID() );
//        Debug.warn( meta.getName() );
    }

}
public class TestRemoteProcess {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            Dante dante = (Dante) Pinecone.sys().getTaskManager().add( new Dante( args, Pinecone.sys() ) );
            dante.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
