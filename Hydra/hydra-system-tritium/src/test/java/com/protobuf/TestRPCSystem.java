package com.protobuf;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.mc.JesusChrist;
import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.lang.DynamicFactory;
import com.pinecone.framework.util.lang.GenericDynamicFactory;
import com.pinecone.hydra.umc.wolf.client.WolfMCClient;
import com.pinecone.hydra.umc.wolf.server.WolfMCServer;
import com.pinecone.hydra.uma.HuskyDuplexExpress;
import com.pinecone.hydra.uma.wolf.WolfAppointClient;
import com.pinecone.hydra.uma.wolf.WolfAppointServer;
import com.pinecone.hydra.uma.wolf.WolvesAppointClient;
import com.pinecone.hydra.uma.wolf.WolvesAppointServer;
import com.pinecone.hydra.umct.husky.compiler.ClassDigest;
import com.pinecone.hydra.umct.husky.machinery.HuskyMappingLoader;
import com.pinecone.hydra.umct.husky.machinery.MultiMappingLoader;
import com.pinecone.hydra.umct.mapping.BytecodeControllerInspector;
import com.pinecone.hydra.umct.mapping.MappingDigest;
import com.pinecone.hydra.umct.husky.compiler.BytecodeIfaceCompiler;
import com.pinecone.hydra.umct.husky.compiler.DynamicMethodPrototype;
import com.pinecone.hydra.umct.husky.compiler.MethodDigest;
import com.pinecone.tritium.messagron.Messagron;

import javassist.ClassPool;

class Jeff extends JesusChrist {
    public Jeff( String[] args, CascadeSystem parent ) {
        this(args, null, parent);
    }

    public Jeff( String[] args, String szName, CascadeSystem parent ) {
        super(args, szName, parent);
    }

    @Override
    public void vitalize () throws Exception {
        //this.testProtoRPCServer();

        //this.testProtoRPCClient();

        //this.testIfaceProxy();

        //this.testController();

        //this.testProtoRPCServerController();

        //this.testClassScanner();

        this.testDuplex();

    }

    private void testProtoRPCServer() throws Exception {
        Messagron messagron = new Messagron( "", this, new JSONMaptron() );

        WolfMCServer wolf1 = new WolfMCServer( "", this, new JSONMaptron("{host: \"0.0.0.0\",\n" +
                "port: 5777, SocketTimeout: 800, KeepAliveTimeout: 3600, MaximumConnections: 1e6}") );
        WolfAppointServer wolf = new WolfAppointServer( wolf1 );


        BytecodeIfaceCompiler inspector = new BytecodeIfaceCompiler( ClassPool.getDefault() );
        List<MethodDigest> digests = inspector.compile( Raccoon.class, false ).getMethodDigests();
        MethodDigest digest = digests.get( 0 );
        DynamicMethodPrototype prototype = (DynamicMethodPrototype) digest;
//
//
//        wolf.getDefaultDeliver().registerController("com.protobuf.Raccoon.scratch", new MessageHandler() {
//            @Override
//            public String getAddressMapping() {
//                return null;
//            }
//
//            @Override
//            public Object invoke( Object... args ) throws Exception {
//                Debug.purplef( args );
//
//                return "miaomiao";
//            }
//
//            @Override
//            public List<String> getArgumentsKey() {
//                return null;
//            }
//
//            @Override
//            public Object getReturnDescriptor() {
//                return prototype.getReturnDescriptor();
//            }
//
//            @Override
//            public Object getArgumentsDescriptor() {
//                return prototype.getArgumentsDescriptor();
//            }
//        });


        RaccoonKing raccoonKing = new RaccoonKing();
        wolf.registerInstance( raccoonKing, Raccoon.class );

        wolf.execute();

        this.getTaskManager().add( wolf );
        //this.getTaskManager().syncWaitingTerminated();
    }

    private void testProtoRPCClient() throws Exception {
        WolfAppointClient wolf = new WolfAppointClient( new WolfMCClient( 2048, "", this, this.getMiddlewareDirector().getMiddlewareConfig().queryJSONObject( "Messagers.Messagers.WolfMCKingpin" ) ) );
        wolf.execute();

        wolf.compile( Raccoon.class, false );
        DynamicMethodPrototype digest = (DynamicMethodPrototype)wolf.queryMethodDigest( "com.protobuf.Raccoon.scratch" );
        Debug.sleep( 500 );

//        wolf.invokeInformAsyn(digest, new Object[]{"fuck you", 2024}, new AsynReturnHandler() {
//            @Override
//            public void onSuccessfulReturn( Object ret ) throws Exception {
//                Debug.greenf( ret );
//            }
//
//            @Override
//            public void onErrorMsgReceived( UMCMessage msg ) throws Exception {
//
//            }
//        });

        Debug.greenf( wolf.invokeInform(digest, "a", 0 ) );


        boolean testParallel = true;
        if ( testParallel ) {
            final AtomicInteger ai = new AtomicInteger();

            for ( int j = 0; j < 10; ++j ) {
                final int id = j;
                Thread thread = new Thread(()->{
                    for ( int i = 0; i < 1e3; ++i ) {
                        try {
                            Debug.greenfs( wolf.invokeInform(digest, "afd", id + 7700 ), ai.getAndIncrement() );
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        }

        //long s = System.currentTimeMillis();
        for ( int i = 0; i < 1e2; ++i ) {
            Debug.greenf( wolf.invokeInform(digest, "afd", i ) );
        }
        //Debug.redfs( System.currentTimeMillis() - s );

        Debug.sleep( 1000000 );

        this.getTaskManager().add( wolf );
        this.getTaskManager().syncWaitingTerminated();
    }

    protected void testIfaceProxy() throws Exception {
        WolfAppointClient wolf = new WolfAppointClient( new WolfMCClient( "", this, this.getMiddlewareDirector().getMiddlewareConfig().queryJSONObject( "Messagers.Messagers.WolfMCKingpin" ) ) );
        wolf.execute();

        wolf.compile( Raccoon.class, false );

        Raccoon pRaccoon = wolf.getIface( Raccoon.class );
        Debug.trace( pRaccoon.scratch( "tree ", 9133 ) );
        Debug.trace( pRaccoon.scratch( "tref ", 9132 ) );
        Debug.trace( pRaccoon.scratch( "treg ", 9131 ) );
        Debug.trace( pRaccoon.scratch( "treh ", 9130 ) );

        this.getTaskManager().add( wolf );
        this.getTaskManager().syncWaitingTerminated();

    }

    protected void testController() throws Exception {
        BytecodeControllerInspector inspector = new BytecodeControllerInspector( ClassPool.getDefault() );

        List<MappingDigest > digests = inspector.characterize( RaccoonController.class );
        Debug.greenf( digests );
    }

    private void testProtoRPCServerController() throws Exception {
        Messagron messagron = new Messagron( "", this, new JSONMaptron() );

        WolfMCServer wolf1 = new WolfMCServer( "", this, new JSONMaptron("{host: \"0.0.0.0\",\n" +
                "port: 5777, SocketTimeout: 800, KeepAliveTimeout: 3600, MaximumConnections: 1e6}") );
        WolfAppointServer wolf = new WolfAppointServer( wolf1 );

        RaccoonController controller  = new RaccoonController();

        wolf.registerController( controller );

        wolf.execute();

        this.getTaskManager().add( wolf );

        this.testProtoRPCClient();
        //this.testIfaceProxy();
    }

    private void testClassScanner() throws Exception {
        DynamicFactory factory = new GenericDynamicFactory();
        WolfMCServer wolf1 = new WolfMCServer( "", this, new JSONMaptron("{host: \"0.0.0.0\",\n" +
                "port: 5777, SocketTimeout: 800, KeepAliveTimeout: 3600, MaximumConnections: 1e6}") );
        WolfAppointServer wolf = new WolfAppointServer( wolf1 );

        factory.getClassScope().addScope( "com.protobuf" );
        MultiMappingLoader mappingLoader = new HuskyMappingLoader( factory, wolf.getMCTTransformer() );
        mappingLoader.updateScope();

        Debug.trace( wolf );
    }

    private void testDuplex() throws Exception {
        Messagron messagron = new Messagron( "", this, new JSONMaptron() );

        WolfMCServer wolfKing = new WolfMCServer( "", this, new JSONMaptron("{host: \"0.0.0.0\",\n" +
                "port: 5777, SocketTimeout: 800, KeepAliveTimeout: 3600, MaximumConnections: 1e6}") );


        WolvesAppointServer wolf = new WolvesAppointServer(wolfKing, HuskyDuplexExpress.class);

//        wolfKing.registerChannelInactiveHandler(new ChannelInactiveHandler() {
//            @Override
//            public boolean afterChannelInactive( ChannelControlBlock ccb ) throws ChannelHandleException {
//                Debug.bluefs( ccb.getChannel().getChannelID(), ccb.getChannel().getIdentityID() );
//                ChannelPool pool = wolf.getUMCTExpress().getPoolByClientId( ccb.getChannel().getIdentityID() );
//                if ( pool != null ) {
//                    Debug.redfs( pool.isEmpty() );
//                }
//                return false;
//            }
//        });

        RaccoonController controller  = new RaccoonController();

        wolf.registerController( controller );

        wolf.execute();


        this.testDuplexClient();


        Debug.sleep( 100 );

        ClassDigest digest = wolf.compile( Raccoon.class, false );

//        for ( int i = 0; i < 2; i++ ) {
//            wolf.invokeInformAsyn(2048, "com.protobuf.Raccoon.scratch", new Object[]{"shit", 123}, new AsynReturnHandler() {
//                @Override
//                public void onSuccessfulReturn( Object ret ) throws Exception {
//                    Debug.redfs( ret );
//                }
//
//                @Override
//                public void onErrorMsgReceived( UMCMessage msg ) throws Exception {
//                    Debug.redfs( msg );
//                }
//            });
//        }


        //Debug.greenf( wolf.invokeInform( 2048, "com.protobuf.Raccoon.scratch", "fuck you", 2025 ) );
        String[] ss = new String[] { "abc", "efg" };
        //Debug.greenf( wolf.invokeInform( 2048, "com.protobuf.Raccoon.scratchS", "fuck you", 2025, ss ) );

        Rabbit rabbit = new Rabbit();
        rabbit.name = "rabbit";
        rabbit.bool = true;
        rabbit.bytes = new byte[] { 1,2,3 };
        Monkey monkey = new Monkey();
        monkey.name = "monkey";
        rabbit.setMonkey( monkey );
        rabbit.setMonkeys( new Monkey[] { monkey, monkey } );

        Rabbit sub = new Rabbit();
        sub.setName( "haha" );
        rabbit.setSub( sub );

        //Rabbit[] args = new Rabbit[] { rabbit };
        List<Rabbit> args = List.of(rabbit);
        //Debug.greenf( wolf.invokeInform( 2048, "com.protobuf.Raccoon.scratchA", "fuck you", 2025, rabbit ) );

        //Debug.greenf( wolf.invokeInform( 2048, "com.protobuf.Raccoon.scratchC", "fuck you", 2025, args ) );

        Debug.greenf( wolf.invokeInform( 2048, "com.protobuf.Raccoon.scratchList", "fuck you", 2025, args ) );
        //Debug.greenf( wolf.invokeInform( 2048, "com.protobuf.Raccoon.scratchVoid" ) );

        //Debug.greenf( wolf.invokeInform( 2048, "com.protobuf.Raccoon.scratchPrime", "fuck you", 12025 ) );




        //Debug.sleep( 3000 );

        //Raccoon raccoon = wolf.getIface( 2048, Raccoon.class );
        //Debug.greenf( raccoon.scratch( "fuck you", 202510 ) );


        this.getTaskManager().add( wolf );

        this.getTaskManager().syncWaitingTerminated();

    }


    private void testDuplexClient() throws Exception {
        WolvesAppointClient wolf = new WolvesAppointClient(
                new WolfMCClient( 2048, "", this, this.getMiddlewareDirector().getMiddlewareConfig().queryJSONObject( "Messagers.Messagers.WolfMCKingpin" ) )
        );
        wolf.execute();

        wolf.compile( Raccoon.class, false );
        DynamicMethodPrototype digest = (DynamicMethodPrototype)wolf.queryMethodDigest( "com.protobuf.Raccoon.scratch" );

        RaccoonController controller  = new RaccoonController();
        wolf.getRouteDispatcher().registerController( controller );

        Debug.sleep( 200 );

        wolf.embraces(2);

        Rabbit rabbit = new Rabbit();
        rabbit.name = "rabbit";
        rabbit.bytes = new byte[] { 1,2,3 };
        Monkey monkey = new Monkey();
        monkey.name = "monkey";
        rabbit.setMonkey( monkey );
        //Debug.bluef( wolf.invokeInform( "com.protobuf.Raccoon.scratchA", "DP you!", 5202123, rabbit ) );

        Debug.bluef( wolf.invokeInform( "com.protobuf.Raccoon.scratch", "DP you!", 5202 ) );

//        Debug.sleep( 3500 );
//        Debug.bluef( wolf.invokeInform( "com.protobuf.Raccoon.scratch", "DP you!", 5201 ) );

        //Debug.greenf( wolf.invokeInform(digest, "fuck you", 2024 ) );

        this.getTaskManager().add( wolf );
    }
}

public class TestRPCSystem {
    public static void main( String[] args ) throws Exception {
        //String[] as = args;
        String[] as = new String[]{ "TestWolfMCClient=true" };
        Pinecone.init( (Object...cfg )->{
            Jeff jeff = (Jeff) Pinecone.sys().getTaskManager().add( new Jeff( as, Pinecone.sys() ) );
            jeff.vitalize();
            return 0;
        }, (Object[]) as );
    }
}
