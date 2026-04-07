package com.sparta;

import java.util.Map;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.event.ProcessEvent;
import com.pinecone.hydra.proc.event.ProcessEventHandler;
import com.pinecone.hydra.proc.event.ProcessLifecycleHandler;
import com.pinecone.hydra.proc.image.ArchEntryPointRunnable;
import com.pinecone.hydra.proc.image.EntryPointRunnable;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.image.LocalHostedClassImage;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.GenericTaskElement;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;
import com.pinecone.hydra.task.kom.marshaling.TaskJSONDecoder;
import com.pinecone.hydra.umc.wolf.client.UlfClient;
import com.pinecone.hydra.umc.wolf.client.WolfMCClient;
import com.pinecone.hydra.umc.wolf.server.WolfMCServer;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnut.archcraft.ender.EnderHydra;
import com.walnut.odin.conduct.CollectiveTaskRegiment;
import com.walnut.odin.conduct.CollectiveTaskLegionary;
import com.walnut.odin.conduct.RavenCollectiveTaskRegiment;
import com.walnut.odin.conduct.RavenCollectiveTaskLegionary;
import com.walnut.odin.conduct.entity.LaunchedContext;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.GenericRavenTaskConfig;
import com.walnut.odin.task.RavenTaskInstrument;
import com.walnut.odin.task.dto.CategoryTag;
import com.walnut.odin.task.dto.GenericCategoryTag;
import com.walnut.odin.task.mapper.OdinUniformTaskMappingDriver;
import com.walnut.odin.task.service.CategoryService;
import com.walnut.odin.task.troll.LaunchFeature;


class Randy extends EnderHydra {
    public Randy( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Randy( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {
        OdinUniformTaskMappingDriver categoryMappingDriver = new OdinUniformTaskMappingDriver(
                this, (IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );
        RavenTaskInstrument ravenTaskInstrument = new RavenTaskInstrument( categoryMappingDriver, new GenericRavenTaskConfig() );

        //this.testCategory( ravenTaskInstrument );

        //this.testInsert( ravenTaskInstrument );
        //this.testGet( ravenTaskInstrument );
        //this.testDelete( instrument );

        //this.testInstance( ravenTaskInstrument );

        //CollectiveTaskRegiment taskRegiment = new RavenCollectiveTaskRegiment( this, ravenTaskInstrument );
        //this.testTaskRegimentBase( taskRegiment ,ravenTaskInstrument);


        this.testInstanceLaunch( ravenTaskInstrument );
    }

    private void testTaskRegimentBase( CollectiveTaskRegiment regiment ,  RavenTaskInstrument instrument) {
/*      TaskElement taskElement = new GenericTaskElement();
        taskElement.setName("spartaTest00058");
        taskElement.setType("sparta");
        taskElement.setResourceType("spartaTest00058");
        taskElement.setImagePath("spartaTest0005");
        taskElement.setDeploymentMethod("spartaTest0017");
        taskElement.setPriority(1);
        taskElement.setActuallyPriority(1);
        taskElement.setDryRun(true);
         RavenTask task = regiment.createTask(  taskElement, taskElement.getGuid());
          Debug.trace(task);
      RavenTaskInstance instance = task.createInstance();
      Debug.trace(instance);
      Debug.trace(instrument.queryElement("spartaTest00058"));*/
        TaskElement  taskElement = (TaskElement) instrument.queryElement("spartaTest00058");
        Debug.trace(taskElement);
        taskElement.setName("spartaTest00059855");
        Debug.trace(taskElement);
        Debug.trace(instrument.getPath( GUIDs.GUID128("01977911-62b1-70f3-bd4f-060e889c088e")));
     regiment.affirmTask("spartaTest00058", GUIDs.GUID128("01977911-62b1-70f3-bd4f-060e889c088e"), taskElement);
//regiment.purgeTask(GUIDs.GUID128("019776f2-e80a-7675-ba4b-7d1d415d8088"));
    }

    private void testCategory( RavenTaskInstrument instrument ) {
        CategoryService categoryService = instrument.getCategoryService();

        CategoryTag tag = new GenericCategoryTag();
        tag.setCategoryName( "Data" );
        tag.setCategoryType( "System" );
        Debug.greenfs( categoryService.setCategoryTag( "root/test/job/task", tag ) );
    }

    private void testInsert( RavenTaskInstrument instrument ) {
//        GenericNamespace namespace = new GenericNamespace();
//        namespace.setName( "Test1" );
//        instrument.put( namespace );

        //Debug.trace( instrument.get( GUIDs.GUID72("03c2f90-000133-000 0-44") ) );


//        GenericApplicationElement applicationNode = new GenericApplicationElement(
//                new JSONMaptron( "{ name:specialApp, alias:jesus, deploymentMethod:Container, path:'/xxx/xxx/ggg', resourceType:human," +
//                        "type:Social, description: 'This is jesus', extraInformation: 'more', level:'L1', primaryImplLang: java, scenario:'/scenario/dragon/king'  }" )
//        );
//
//        applicationNode.apply( new JSONMaptron( "{ name:specialApp2, deploymentMethod:VM }" ) );
//        instrument.put( applicationNode );
        for( int i = 1; i <=12; i++ ) {
            GenericTaskElement taskElement = new GenericTaskElement(
                    new JSONMaptron( "{ name:'测试服务"+i+"', alias:jesus, serviceType:System, resourceType:human," +
                            "type:Social, description: 'This is special', extraInformation: 'more', level:'L1', primaryImplLang: java, scenario:'/scenario/dragon/king'  }" )
            );
            instrument.put( taskElement );
        }



          //instrument.affirmOwnedNode( GUIDs.GUID128("01972f5a-7edc-79bd-9655-ea50ae5b0887"),GUIDs.GUID128("01972f5b-4d56-7d1c-811c-b855bfdb5dcb") );
        //instrument.newHardLink( GUIDs.GUID128("01972f5a-7edc-79bd-9655-ea50ae5b0887"), GUIDs.GUID128("01972f59-4049-77fc-827f-a9976425c01c") );
    }

    private void testGet( TaskInstrument instrument ){
        //Debug.trace( instrument.queryGUIDByPath( "规则1/很好的服务/我的世界" ) );
        //Debug.trace( instrument.getPath(GUIDs.GUID72( "03c4a36-000381-0000-48" ) ) );
        //Debug.trace( instrument.get( GUIDs.GUID72("03e60e8-0000ae-0000-20") ) );
        //Debug.trace( instrument.get( GUIDs.GUID72("03e60e8-0000c5-0000-48") ) );
        //Debug.trace( instrument.get( GUIDs.GUID72("03e60e8-000117-0000-18") ) );
//        Debug.trace( instrument.get( GUIDs.GUID72( "02be396-0001e9-0000-e4" ) ) );
        //Debug.trace( instrument.affirmApplication( "Test1/App1" ) );

//        Debug.trace( instrument.affirmService( "root/特殊服务" ) );
//        Debug.trace( instrument.affirmApplication( "root/species/orc" ) );
//        Debug.trace( instrument.affirmNamespace("root/species") );
//        Debug.trace( instrument.affirmNamespace( "root" ).fetchChildren() );
//
//        instrument.affirmApplication( "root/species/orc" ).addChild( new GenericServiceElement( new JSONMaptron( "{ name: slaughter }" ) ) );
//
//        Debug.trace( instrument.affirmApplication( "root/species/orc" ).fetchChildren() );
//
//        Debug.trace( instrument.queryElement( "root/species/orc/slaughter" ).toJSONObject() );
//
//        instrument.affirmNamespace( "root" ).addChild( new GenericNamespace( new JSONMaptron( "{ name: weapon, scenario: s1, description: d1, level:L1, primaryImplLang:Java }" ) ) );
//
//        Debug.fmp( 2, instrument.queryElement( "root/weapon" ).evinceNamespace().toJSONDetails() );




        TaskJSONDecoder decoder = new TaskJSONDecoder( instrument );
        decoder.decode( new JSONMaptron( "{ root: { test: { job: { metaType: AppElement, type:SysJob, tasks: { task: { metaType: TaskElement, type: SparkTask } } } } } }" ) );

        Debug.fmp( 2, instrument.queryElement( "root" ).toJSONObject() );
//        GUID128 guid128 = GUIDs.GUID128("019714af-e0ec-7f2a-94a3-cd740efccb6c");
//        Debug.trace(instrument.getPath( GUIDs.GUID128("019714af-e0ec-7f2a-94a3-cd740efccb6c") ));
    }

    private void testDelete( TaskInstrument instrument ) {
        instrument.remove( GUIDs.GUID128("181e9e4-000395-0000-d4") );
    }

    private void testInstance( TaskInstrument instrument ) {
        InstanceInstrument instanceInstrument = instrument.getInstanceInstrument();

        GUID taskGuid = instrument.queryGUIDByPath( "root/test/job/task" );
        //InstanceEntry instanceEntry = new GenericInstanceEntry( new JSONMaptron( "{priority:456, taskType: Spark, instanceName: test123}" ) );

        //instanceInstrument.addInstance( taskGuid, instanceEntry );

        //Debug.fmp( 2, instanceEntry );

//        TaskElement taskElement = (TaskElement) instrument.queryElement( "root/test/job/task" );
//        taskElement.setImagePath( "uofs:///sys/public/global/exe/images/hola/senorita/image_c" );
//        instrument.update( taskElement );

        InstanceEntry instanceEntry = instanceInstrument.makeInstanceEntry( taskGuid );
        Debug.fmp( 2, instanceEntry );
    }





    private void testInstanceLaunch( TaskInstrument instrument ) throws Exception {
        WolfMCServer wolfKing = new WolfMCServer( "", this, new JSONMaptron("{host: \"0.0.0.0\",\n" +
                "port: 5777, SocketTimeout: 800, KeepAliveTimeout: 3600, MaximumConnections: 1e6}") );
        CollectiveTaskRegiment regiment = new RavenCollectiveTaskRegiment( this, (CentralizedTaskInstrument) instrument, wolfKing );
        regiment.startRemoteProcessServer();



        UlfClient ulfClient = new WolfMCClient(
                this.getSystemGuidAllocator72().nextGUIDi64(), "", this, this.getMiddlewareDirector().getMiddlewareConfig().queryJSONObject( "Messagers.Messagers.WolfMCKingpin" )
        );
        CollectiveTaskLegionary regimentClient = new RavenCollectiveTaskLegionary( "jesus", this, ulfClient );
        regimentClient.startService();
        regimentClient.joinRegiment();

        regimentClient.remoteProcessManagerClient().addProcessLifecycleHandler(new ProcessLifecycleHandler() {
            @Override
            public void fired( String imageAddress, EntryPointRunnable runnable, ProcessEvent event ) {
                Debug.greenfs( imageAddress, event );
            }
        });






//        TaskExecutionLauncher launcher = regiment.taskExecutionLauncher();
//        GUID taskGuid = instrument.queryGUIDByPath( "root/test/job/task" );
//        RavenTask task = regiment.getTaskByGuid( taskGuid );
//        RavenTaskInstance instance = task.createInstance();


        //ProcessManager manager = this.processManager();
        ProcessManager manager = regimentClient.processManager();
        ProcessEventHandler eventHandler = new ProcessEventHandler() {
            @Override
            public void fired(EntryPointRunnable runnable, ProcessEvent event ) {
                Debug.bluef( runnable, event );
            }
        };

        ExecutionImage image = new LocalHostedClassImage( "image_c", new ArchEntryPointRunnable( eventHandler ) {
            @Override
            public int main( Map<String, String[]> args ) {
                Debug.greenfs( "Hello, hi, I am `" + this.ownedProcess().getName() + "`!" );
                Debug.sleep( 1000 );
                Debug.greenfs( "Miao~" );

                //throw new IrrationalProvokedException();
                return 1984;
            }
        }, manager );
        manager.getImageLoader().registerLocalScopeExecutionImage( "hola/senorita", image );



        LaunchFeature feature = new LaunchFeature();
        //UProcess uProcess = launcher.createLocally( instance, feature );
        //UProcess uProcess = launcher.createRemotely( instance, client.getClientId(), feature );

        //uProcess.start();

        //launcher.launchRemotely( instance, client.getClientId(), feature );
        //launcher.launchLocally( instance, feature );




//        // Test processor
//        GenericTaskProcessorEntity processorEntity = new GenericTaskProcessorEntity(
//                new JSONMaptron("{name:r1, clusterPath:'/r1', clusterName: 'r1', local: false, priority: 100, queueMeta: {" +
//                        "name: r1_q, maxCapacity: 100, minCapacity: 100, runtimeInstanceCapacity: 50}}" +
//                        "}}")
//        );
//        processorEntity.setControlClientId( client.getClientId() );
//
//        TaskExecutionProcessor processor = new RavenTaskExecutionProcessor(processorEntity, launcher);
//        processor.pipeLaunch( List.of( TaskLaunchContext.of( feature, instance ) ) );



        //TaskDispatcher taskDispatcher = regiment.taskDispatcher();
        //UProcess process = taskDispatcher.create( instance, feature );
        LaunchedContext context = regiment.create( "root/test/job/task", feature );
        Debug.greenfs( context.getProcess().getPID() );

    }





}

public class TestTaskTree {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            Randy Jesse = (Randy) Pinecone.sys().getTaskManager().add( new Randy( args, Pinecone.sys() ) );
            Jesse.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
