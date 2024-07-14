package com.system;

import com.pinecone.Pinecone;
import com.pinecone.framework.util.Debug;

public class TestComponent {
    public static void testAdd() throws Exception {
        SimpleCascadeComponentManager manager = new SimpleCascadeComponentManager();

        SimpleCascadeComponent A = new SimpleCascadeComponent( "A", manager );
        SimpleCascadeComponent B = new SimpleCascadeComponent( "B", manager );
        SimpleCascadeComponent C = new SimpleCascadeComponent( "C", manager );
        manager.addComponent( A );
        manager.addComponent( B );
        manager.addComponent( C );



        SimpleCascadeComponent a1 = new SimpleCascadeComponent( "a1", manager );
        SimpleCascadeComponent a2 = new SimpleCascadeComponent( "a2", manager );
        A.addChildComponent( a1 );
        A.addChildComponent( a2 );

        SimpleCascadeComponent b1 = new SimpleCascadeComponent( "b1", manager );
        SimpleCascadeComponent b2 = new SimpleCascadeComponent( "b2", manager );
        B.addChildComponent( b1 );
        B.addChildComponent( b2 );

        SimpleCascadeComponent c1 = new SimpleCascadeComponent( "c1", manager );
        C.addChildComponent( c1 );


        Debug.fmt( 2,manager.getComponents() );
        Debug.fmt( 2, manager.getComponentsRegisterList() );
    }

    public static void testRefer() throws Exception {
        SimpleCascadeComponentManager manager = new SimpleCascadeComponentManager();

        SimpleCascadeComponent A = new SimpleCascadeComponent( "A", manager );
        SimpleCascadeComponent B = new SimpleCascadeComponent( "B", manager );
        SimpleCascadeComponent C = new SimpleCascadeComponent( "C", manager );
        manager.addComponent( A );
        manager.addComponent( B );
        manager.addComponent( C );



        SimpleCascadeComponent a1 = new SimpleCascadeComponent( "a1", manager );
        SimpleCascadeComponent a2 = new SimpleCascadeComponent( "a2", manager );
        A.addChildComponent( a1 );
        A.addChildComponent( a2 );

        SimpleCascadeComponent b1 = new SimpleCascadeComponent( "b1", manager );
        SimpleCascadeComponent b2 = new SimpleCascadeComponent( "b2", manager );
        B.addChildComponent( b1 );
        B.addChildComponent( b2 );
        B.referChildComponent( a2 );

        SimpleCascadeComponent c1 = new SimpleCascadeComponent( "c1", manager );
        C.addChildComponent( c1 );
        C.referChildComponent( b2 );

        Debug.fmt( 2,manager.getComponents() );
        Debug.fmt( 2, manager.getComponentsRegisterList() );

        Debug.fmt( 2, C.children() );
    }

    public static void testCascadeRemove() throws Exception {
        SimpleCascadeComponentManager manager = new SimpleCascadeComponentManager();

        SimpleCascadeComponent A = new SimpleCascadeComponent( "A", manager );
        SimpleCascadeComponent B = new SimpleCascadeComponent( "B", manager );
        SimpleCascadeComponent C = new SimpleCascadeComponent( "C", manager );
        manager.addComponent( A );
        manager.addComponent( B );
        manager.addComponent( C );



        SimpleCascadeComponent a1 = new SimpleCascadeComponent( "a1", manager );
        SimpleCascadeComponent a2 = new SimpleCascadeComponent( "a2", manager );
        A.addChildComponent( a1 );
        A.addChildComponent( a2 );

        SimpleCascadeComponent b1 = new SimpleCascadeComponent( "b1", manager );
        SimpleCascadeComponent b2 = new SimpleCascadeComponent( "b2", manager );
        SimpleCascadeComponent b3 = new SimpleCascadeComponent( "b3", manager );

        SimpleCascadeComponent b3_1 = new SimpleCascadeComponent( "b3_1", manager );
        b3.addChildComponent( b3_1 );
        b3.referChildComponent( a1 );

        B.addChildComponent( b1 );
        B.addChildComponent( b2 );
        B.addChildComponent( b3 );
        B.referChildComponent( a2 );


        SimpleCascadeComponent c1 = new SimpleCascadeComponent( "c1", manager );
        C.addChildComponent( c1 );
        C.referChildComponent( a1 );
        C.referChildComponent( b2 );

        Debug.fmt( 2,manager.getComponents() );
        Debug.fmt( 2, manager.getComponentsRegisterList() );
        Debug.fmt( 2, C.children() );



        // Test omega remove child
//        manager.removeComponent( a1 );
//        Debug.fmt( 2,manager.getComponents() );
//        Debug.fmt( 2, manager.getComponentsRegisterList() );
//        Debug.fmt( 2, C.children() );


        // Test omega remove parent
        manager.removeComponent( A );  // Diane has been erased from every universe across infinity.
        Debug.fmt( 2,manager.getComponents() );
        Debug.fmt( 2, manager.getComponentsRegisterList() );
        Debug.fmt( 2, C.children() );
        Debug.fmt( 2, b3.children() );

        // Test self-destruction
//        A.purge();
//        Debug.fmt( 2,manager.getComponents() );
//        Debug.fmt( 2, manager.getComponentsRegisterList() );
//        Debug.fmt( 2, C.children() );
//        Debug.fmt( 2, b3.children() );

        // Test others
//        a1.independent( "cyc" );
//        Debug.fmt( 2,manager.getComponents() );
//        Debug.fmt( 2, manager.getComponentsRegisterList() );
//        Debug.fmt( 2, C.children() );
//        Debug.fmt( 2, b3.children() );
    }

    public static void main( String[] args ) throws Exception {
        //String szJson = FileUtils.readAll("J:/120KWordsPhonetics.json5");
        Pinecone.init( (Object...cfg )->{

            //TestComponent.testAdd();
            //TestComponent.testRefer();
            TestComponent.testCascadeRemove();


            return 0;
        }, (Object[]) args );
    }
}