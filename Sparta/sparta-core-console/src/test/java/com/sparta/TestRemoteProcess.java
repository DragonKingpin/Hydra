package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.event.ProcessEvent;
import com.pinecone.hydra.proc.event.ProcessEventHandler;
import com.pinecone.hydra.proc.image.ArchEntryPointRunnable;
import com.pinecone.hydra.proc.image.EntryPointRunnable;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.image.LocalHostedClassImage;
import com.pinecone.ulf.util.guid.i128.GuidAllocator128V7;
import com.pinecone.ulf.util.guid.i64.GuidAllocator72V2;
import com.walnut.archcraft.ender.EnderHydra;
import com.walnut.odin.proc.client.RavenLocalProcessManagerClient;
import com.walnut.odin.proc.server.RavenRemoteProcessManagerServer;

import java.io.IOException;
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
        this.testProcess();
    }

    private void testProcess() throws IOException {
        GuidAllocator128V7 guidAllocator128V7 = new GuidAllocator128V7();
        ProcessManager manager = this.processManager();

        ProcessEventHandler eventHandler = new ProcessEventHandler() {
            @Override
            public void fired(EntryPointRunnable runnable, ProcessEvent event ) {
                Debug.bluef( runnable, event );
            }
        };

        ExecutionImage image = new LocalHostedClassImage( "gay", new ArchEntryPointRunnable( eventHandler ) {
            @Override
            public void execute() {
                Debug.greenfs( "Hello, hi, I am `" + this.ownedProcess().getName() + "`!" );
                Debug.greenfs( this.ownedProcess().getPID() );
                Debug.greenfs( this.ownedProcess().getLocalPID() );

                Debug.greenfs( this.ownedProcess().getEnvironmentVariables() );
                Debug.greenfs( this.ownedProcess().getStartupArguments() );
                Debug.bluef( this.ownedProcess().getControllableLevel() );
                Debug.bluef( this.ownedProcess().getOwnedProcessManager() );
                Debug.greenfs( this.ownedProcess().parentProcess() );
            }
        }, manager );

        //LocalUProcess process = manager.createLocalHostedProcess( image, null, Map.of( "fuck", new String[]{ "you", "she", "he", "it" } ) );

        RavenRemoteProcessManagerServer server = new RavenRemoteProcessManagerServer(this, guidAllocator128V7);

        RavenLocalProcessManagerClient client = new RavenLocalProcessManagerClient(this, manager, new GuidAllocator72V2());

        UProcess process = client.createProcess(image, null, Map.of("fuck", new String[]{"you", "she", "he", "it"}), null);

        server.start( process.getGuid() );
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
