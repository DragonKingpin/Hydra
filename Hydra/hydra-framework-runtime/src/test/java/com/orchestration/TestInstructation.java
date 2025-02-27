package com.orchestration;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.executum.ArchProcessum;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.auto.*;

class SimpleInstruct extends ArchInstructation {
    String ss;
    public SimpleInstruct( String s ) {
        super();
        this.ss = s;
    }

    @Override
    public void execute() throws Exception {
        Debug.trace( "Hello hi, fuck "+ this.ss +" !" );

//        if( this.ss.equals( "1" ) ) {
//            throw new Exception();
//        }
    }
}

class SimpleS extends ArchSuggestation {
    String ss;
    public SimpleS( String s ) {
        super();
        this.ss = s;
    }

    @Override
    public void execute() {
        Debug.trace( "Hello hi, fuck "+ this.ss +" !" );

        if( this.ss.equals( "1" ) ) {
            throw new RuntimeException();
        }
    }
}

class SimplePI extends ArchParallelInstructation {
    String ss;
    public SimplePI( Processum parent, String s  ) {
        super( parent );
        this.ss = s;
    }

    @Override
    public void doExecute() throws Exception {
        for ( int i = 0; i < 1; i++ ) {
            Debug.trace( "Hello hi, fuck "+ this.ss +" !" );
        }


//        if( this.ss.equals( "1" ) ) {
//            throw new Exception();
//        }
    }
}

public class TestInstructation {
    public static void testMarshalling() throws Exception {
        Processum p = new ArchProcessum( "", null ) {};


        GenericMarshalling marshalling = new GenericMarshalling();
        marshalling.add( new SimpleInstruct( "0" ) );
        //marshalling.add( new SimplePI( p,"1" ) );
        marshalling.add( new SimpleS( "1" ) );

        GenericMarshalling am = new GenericMarshalling();
        am.add( new SimpleInstruct( "a0" ) );
        am.add( new SimpleInstruct( "a1" ) );


        marshalling.add( am );

        marshalling.execute();
    }

    public static void testPeriodicAutomaton() throws Exception {
        PeriodicAutomatron automatron = new PeriodicAutomaton( null, 500 );
        automatron.command( new SimpleInstruct( "0" ) );
        automatron.command( new SimpleInstruct( "1" ) );
        automatron.command( new SimpleInstruct( "2" ) );

        GenericMarshalling am = new GenericMarshalling( automatron );
        am.add( new SimpleInstruct( "a0" ) );
        am.add( new SimpleInstruct( "a1" ) );
        //am.add( new SimplePI( automatron,"p1" ) );
        automatron.command( am );

        automatron.start();
        //automatron.join();

        Debug.sleep( 10 );
        //automatron.command( new SimpleInstruct( "3" ) );

        Thread push = new Thread( ()->{
            for ( int i = 0; i < 100; i++ ) {
                Debug.sleep( 50 );
                automatron.command( new SimpleInstruct( "push" + i ) );
            }
        } );
        push.start();

        Thread push2 = new Thread( ()->{
            for ( int i = 0; i < 100; i++ ) {
                Debug.sleep( 50 );
                automatron.command( new SimpleInstruct( "2push" + i ) );
            }
        } );
        push2.start();



//        Debug.sleep( 1500 );
//        automatron.command( KernelInstructation.CONTINUE );
//        Debug.sleep( 2000 );
//        automatron.withdraw( KernelInstructation.CONTINUE );
//        automatron.terminate();

        push2.join();
        push.join();
        automatron.join();
    }

    public static void testAutomaton() throws Exception {
        LifecycleAutomaton automatron = new Automaton( null );
        automatron.setHeartbeatTimeoutMillis( 1000 );


        automatron.start();

        Thread push = new Thread( ()->{
            int i = 0;
            for ( ; i < 10; i++ ) {
                Debug.sleep( 100 );
                automatron.command( new SimpleInstruct( "push" + i ) );
            }

            //automatron.command( KernelInstructation.DIE );

            for ( ; i < 20; i++ ) {
                Debug.sleep( 100 );
                automatron.command( new SimpleInstruct( "push" + i ) );
            }
        } );
        push.start();

        Thread push2 = new Thread( ()->{
            int i = 0;
            for ( ; i < 10; i++ ) {
                Debug.sleep( 100 );
                automatron.command( new SimpleInstruct( "2push" + i ) );
            }

            automatron.command( new SimplePI( automatron,"p1" ) );
        } );
        push2.start();

//        Thread push3 = new Thread( ()->{
//            while ( true ) {
//                Debug.sleep( 1500 );
//                automatron.sendHeartbeat();
//            }
//            //Debug.sleep( 1000 );
//            //automatron.command( KernelInstructation.DIE );
//        } );
//        push3.start();

        automatron.join();
    }

    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{


            //TestInstructation.testMarshalling();
            TestInstructation.testPeriodicAutomaton();
            // TestInstructation.testAutomaton();

            return 0;
        }, (Object[]) args );
    }
}
