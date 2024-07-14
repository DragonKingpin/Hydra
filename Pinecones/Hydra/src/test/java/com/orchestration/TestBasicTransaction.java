package com.orchestration;

import com.pinecone.hydra.orchestration.*;
import com.pinecone.Pinecone;
import com.pinecone.framework.system.NotImplementedException;

public class TestBasicTransaction {
    public static void testSequential() {
        SequentialAction action = new SequentialAction();
        //LoopAction action = new LoopAction();
        //ParallelAction action = new ParallelAction();

        action.getSeqExceptionNeglector().add( NotImplementedException.class );

        action.add( new SimpleExertium( "Gay0" ) );
        action.add( new SimpleExertium( "Gay1" ) );

        Exertium e = new Exertium();
        //action.add( e );


        SequentialAction sa = new SequentialAction();
        sa.add( new SimpleExertium( "A0" ) );
        sa.add( new SimpleExertium( "A1" ) );
        sa.add( new SimpleExertium( "A2" ) );
        sa.add( new JumpPoint( 0 ) );
        action.add( sa );


        action.add( new SimpleExertium( "Gay2" ) );

        action.add( ProcessController.BREAK );

        action.add( new SimpleExertium( "Gay3" ) );
        action.add( new SimpleExertium( "Gay4" ) );
        //action.add( new JumpPoint(1));

        action.start();

    }

    public static void testParallel() {
        ParallelAction action = new ParallelAction();
        action.add( ( new SimpleParallelium( "P0" ) ) );
        action.add( new SimpleParallelium( "P1" ) );
        action.add( new SimpleParallelium( "P2" ) );

        action.add( new SimpleExertium( "E0" ) );
        action.add( new SimpleExertium( "E1" ) );
        action.add( new SimpleExertium( "E2" ) );
        action.add( new SimpleExertium( "E3" ) );

        action.add( new SimpleParallelium( "P3" ) );
        action.add( new SimpleParallelium( "P4" ) );
        action.add( new SimpleParallelium( "P5" ) );
        action.add( new SimpleParallelium( "P6" ) );
        action.start();

    }

    public static void testGraph() {

        Transaction a0 = new SequentialAction();
        a0.add( ( new SimpleExertium( "E0_0" ) ) );
        a0.add( ( new SimpleExertium( "E0_1" ) ) );

        ParallelAction a1 = new ParallelAction();
        a1.add( ( new SimpleParallelium( "P1_0" ) ) );
        a1.add( new SimpleParallelium( "P1_1" ) );
        a1.add( new SimpleParallelium( "P1_2" ) );

        a1.add( new SimpleExertium( "E1_0" ) );
        a1.add( new SimpleExertium( "E1_1" ) );

        a1.add( new SimpleParallelium( "P1_3" ) );
        a1.add( new SimpleParallelium( "P1_4" ) );

        SequentialAction aGroup = new SequentialAction();
        aGroup.add( ( new SimpleExertium( "EG_0" ) ) );
        aGroup.add( ( new SimpleExertium( "EG_1" ) ) );
        a1.add( aGroup );
        a1.add( ParallelAction.wrap( aGroup ) );


        a0.add( a1 );
        a0.add( ( new SimpleExertium( "E0_END" ) ) );

        a0.start();
    }


    public static void main( String[] args ) throws Exception {
        //String szJson = FileUtils.readAll("J:/120KWordsPhonetics.json5");
        Pinecone.init( (Object...cfg )->{

            //TestBasicTransaction.testSequential();
            //TestBasicTransaction.testParallel();
            TestBasicTransaction.testGraph();

            return 0;
        }, (Object[]) args );
    }
}
