package com.task;

import java.util.Map;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.system.regime.arch.Lord;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcessStatus;
import com.pinecone.hydra.proc.event.ProcessEventHandler;
import com.pinecone.hydra.proc.event.ProcessLifecycleHandler;
import com.pinecone.hydra.proc.image.ArchEntryPointRunnable;
import com.pinecone.hydra.proc.image.EntryPointRunnable;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.image.LocalHostedClassImage;
import com.pinecone.hydra.task.kom.entity.ElementNode;
import com.pinecone.hydra.umc.wolf.client.UlfClient;
import com.pinecone.hydra.umc.wolf.client.WolfMCClient;
import com.pinecone.hydra.umc.wolf.server.WolfMCServer;
import com.walnut.archcraft.ender.EnderHydra;
import com.walnut.odin.conduct.CollectiveTaskLegionary;
import com.walnut.odin.conduct.RavenCollectiveTaskLegionary;
import com.walnut.odin.conduct.schedule.RavenTaskScheduler;
import com.walnut.odin.system.Odin;

class Rick extends EnderHydra {
    public Rick( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Rick( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {

        WolfMCServer wolfKing = new WolfMCServer( "", this, new JSONMaptron("{host: \"0.0.0.0\",\n" +
                "port: 5777, SocketTimeout: 800, KeepAliveTimeout: 3600, MaximumConnections: 1e6}") );
        this.getDispenserCenter().getInstanceDispenser().registerInstance( "WolfKing", wolfKing );

        Lord lord = this.getLordFederation().instantiate( "KernelOdinLord", "./system/setup/lords/odin.json5" );

        Odin odin = (Odin) lord;
        odin.vitalize();


        this.testOrchestrator( odin );
    }

    public void testOrchestrator( Odin odin ) throws Exception {
        odin.taskRegiment().startRemoteProcessServer();

        UlfClient ulfClient = new WolfMCClient(
                this.getSystemGuidAllocator72().nextGUIDi64(), "", this, this.getMiddlewareDirector().getMiddlewareConfig().queryJSONObject( "Messagers.Messagers.WolfMCKingpin" )
        );
        CollectiveTaskLegionary regimentClient = new RavenCollectiveTaskLegionary( "jesus", this, ulfClient );
        regimentClient.startService();
        regimentClient.joinRegiment();

        regimentClient.remoteProcessManagerClient().addProcessLifecycleHandler(new ProcessLifecycleHandler() {
            @Override
            public void fired(String imageAddress, EntryPointRunnable runnable, UProcessStatus event ) {
                Debug.greenfs( imageAddress, event );
            }
        });


        ProcessManager manager = regimentClient.processManager();
        ProcessEventHandler eventHandler = new ProcessEventHandler() {
            @Override
            public void fired(EntryPointRunnable runnable, UProcessStatus event ) {
                Debug.bluef( runnable, event );
            }
        };

        ExecutionImage image = new LocalHostedClassImage( "image_c", new ArchEntryPointRunnable( eventHandler ) {
            @Override
            public int main( Map<String, String> args ) {
                Debug.greenfs( "Hello, hi, I am `" + this.ownedProcess().getName() + "`!" );
                Debug.sleep( 1000 );
                Debug.greenfs( "Miao~" );

                //throw new IrrationalProvokedException();
                return 1984;
            }
        }, manager );
        manager.getImageLoader().registerLocalScopeExecutionImage( "hola/senorita", image );



        ElementNode n = odin.taskRegiment().taskInstrument().queryElement( "@root" );
        Debug.fmp( 2, n.toJSONObject() );


        RavenTaskScheduler scheduler = (RavenTaskScheduler) odin.taskScheduler();
        scheduler.pulseSchedule();
    }


}
public class TestRuntime {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            Rick rick = (Rick) Pinecone.sys().getTaskManager().add( new Rick( args, Pinecone.sys() ) );
            rick.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
