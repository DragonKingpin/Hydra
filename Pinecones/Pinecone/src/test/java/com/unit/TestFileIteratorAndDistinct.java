package com.unit;

import com.pinecone.Pinecone;
import com.pinecone.framework.unit.distinct.*;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.io.FileNamePathIterator;
import com.pinecone.framework.util.io.FileUtils;
import com.pinecone.framework.util.io.PathItemIterator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class TestFileIteratorAndDistinct {
    public static void testFileIterator() throws Exception {
        PathItemIterator iterator = new PathItemIterator( Path.of( "C:/Users/undefined/Desktop/wolfmc" ), false );

        while ( iterator.hasNext() ) {
            Debug.trace( iterator.next().toString() );
        }

    }

    public static void testDistinct_Simple() throws Exception {
        List<String> list1 = Arrays.asList( "t1", "t2", "t3", "t4" );
        List<String> list2 = Arrays.asList( "t1", "t3", "t5", "t6" );
        List<String> list3 = Arrays.asList( "t1", "t2", "t3", "t7", "t7" );
        List<String> list4 = Arrays.asList( "t8", "t2", "t3", "t9" );

        MegaBloomDistinctAudit<String> distinctAudit = new MegaBloomDistinctAudit<>(
                List.of( list1, list2, list3 ), DistinctType.SymmetricDistinct, new HashSet<>()
        );
        Collection<String> distinctElements = distinctAudit.audit();

        Debug.trace( distinctElements );


        Debug.trace( distinctAudit.audit( list4 ) );

        Debug.trace( distinctAudit.hasOwnElement( "t3" ) );

    }

    public static void testDistinct_Master() throws Exception {
        List<String> list1 = Arrays.asList( "t1", "t2", "t3", "t4" );
        List<String> list2 = Arrays.asList( "t1", "t3", "t5", "t6" );
        List<String> list3 = Arrays.asList( "t1", "t2", "t3", "t7", "t7" );
        List<String> list4 = Arrays.asList( "t8", "t2", "t3", "t9" );

        MegaPrototypeBloomDistinctAudit<String> distinctAudit = new MegaPrototypeBloomDistinctAudit<>(
                list1.iterator(), List.of( list2, list3 ), DistinctType.SymmetricDistinct, new HashSet<>()
        );
        Collection<String> distinctElements = distinctAudit.audit();

        Debug.trace( distinctElements );


        Debug.trace( distinctAudit.audit( list4 ) );

        Debug.trace( distinctAudit.hasOwnElement( "t3" ) );

    }

    public static void testDistinct_Tiny() throws Exception {
        List<String> list1 = Arrays.asList( "t1", "t2", "t3", "t4", "t5", "t6", "t7", "t8", "t9", "t10", "t11" );
        List<String> list2 = Arrays.asList( "t6", "t2", "t4", "t8", "t1", "t3", "t5" );
        List<String> list3 = Arrays.asList( "t1", "t2", "t3", "t7", "t7" );
        List<String> list4 = Arrays.asList( "t8", "t2", "t3", "t9" );

        GenericDistinctAudit<String> distinctAudit = new GenericDistinctAudit<>(
                List.of( list1, list2 ), DistinctType.SymmetricDistinct, new ArrayList<>()
        );
        Collection<String> distinctElements = distinctAudit.audit();

        Debug.trace( distinctElements );


//        Debug.trace( distinctAudit.audit( list4 ) );
//
//        Debug.trace( distinctAudit.hasOwnElement( "t3" ) );

    }

    public static void testDistinct_TinyMaster() throws Exception {
        List<String> list1 = Arrays.asList( "t1", "t2", "t3", "t4" );
        List<String> list2 = Arrays.asList( "t1", "t3", "t5", "t6" );
        List<String> list3 = Arrays.asList( "t1", "t2", "t3", "t7", "t7" );
        List<String> list4 = Arrays.asList( "t8", "t2", "t3", "t9" );

        GenericPrototypeDistinctAudit<String> distinctAudit = new GenericPrototypeDistinctAudit<>(
                list1.iterator(), List.of( list2, list3 ), DistinctType.SymmetricDistinct, new HashSet<>()
        );
        Collection<String> distinctElements = distinctAudit.audit();

        Debug.trace( distinctElements );


        Debug.trace( distinctAudit.audit( list4 ) );

        Debug.trace( distinctAudit.hasOwnElement( "t3" ) );

    }

    public static void testDistinct_MegaMerge() throws Exception {
        Collection<String> list1 = List.of( "t0", "t1", "t2", "t3", "t4", "t5", "t6", "t8", "t9" );
        //List<String> list2 = Arrays.asList( "t9", "t8", "t6", "t5", "t0", "t2" );
        Collection<String> list2 = List.of( "t0", "t1", "t2", "t3", "t6", "t5" );

        List<String> list3 = Arrays.asList( "t1", "t2", "t3", "t7", "t7" );
        List<String> list4 = Arrays.asList( "t8", "t2", "t3", "t9" );

        MegaMergeDistinctAudit<String> distinctAudit = new MegaMergeDistinctAudit<>(
                list1.iterator(), list2.iterator(), 2
        );
        Collection<String> distinctElements = distinctAudit.audit();

        Debug.trace( distinctElements );


        //Debug.trace( distinctAudit.audit( list4 ) );

        //Debug.trace( distinctAudit.hasOwnElement( "t3" ) );

    }

    public static void testDistinct_dir() throws Exception {
        Path desk = Path.of( "C:/Users/undefined/Desktop/wolfmc" );
        FileNamePathIterator iterator1 = new FileNamePathIterator( desk, false );
        FileNamePathIterator iterator2 = new FileNamePathIterator( Path.of( "E:/MyFiles/CodeScript/Project/Hazelnut/Sauron/Saurons/Pinecones/Hydra/src/main/java/com/pinecone/hydra/umc/wolfmc" ), false );

//        List<String > fn1 = new ArrayList<>();
//        while ( iterator1.hasNext() ) {
//            fn1.add( iterator1.next().toString() );
//        }
//
//        List<String > fn2 = new ArrayList<>();
//        while ( iterator2.hasNext() ) {
//            fn2.add( iterator2.next().toString() );
//        }
//
//        Set<String > s1 = new HashSet<>( fn2 );
//        List<String > uni = new ArrayList<>();
//        for( String s : fn1 ) {
//            if( !s1.contains( s ) ) {
//                uni.add( s );
//            }
//        }

//        GenericDistinctAudit<String> distinctAudit = new GenericDistinctAudit<>(
//                List.of( fn1, fn2 ), DistinctType.SymmetricDistinct, new ArrayList<>()
//        );

//        GenericPrototypeDistinctAudit<String> distinctAudit = new GenericPrototypeDistinctAudit<>(
//                fn2.iterator(), List.of( fn1 ), DistinctType.SymmetricDistinct, new ArrayList<>()
//        );
//
//        Collection<String > c = distinctAudit.audit();
//        //c = uni;
//        Debug.trace( c.size() );
//        for( String p : c ) {
//            Debug.trace( desk.resolve( p ).toString() );
//
//            //Files.copy( p.toAbsolutePath(), Path.of( "C:/Users/undefined/Desktop/welsir" ), StandardCopyOption.REPLACE_EXISTING );
//        }


        GenericPrototypeDistinctAudit<Path > distinctAudit2 = new GenericPrototypeDistinctAudit<>(
                iterator2, List.of( iterator1 ), new ArrayList<>(), DistinctType.SymmetricDistinct
        );

        Collection<Path > cp = distinctAudit2.audit();
        Debug.trace( cp.size() );
        for( Path p : cp ) {
            Debug.trace( desk.resolve( p ).toString() );
            Files.copy( desk.resolve( p ), Path.of( "C:/Users/undefined/Desktop/welsir/" ).resolve( p ), StandardCopyOption.REPLACE_EXISTING );
        }

    }

    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            //TestFileIteratorAndDistinct.testFileIterator();
            //TestFileIteratorAndDistinct.testDistinct_Simple();
            //TestFileIteratorAndDistinct.testDistinct_Master();
            //TestFileIteratorAndDistinct.testDistinct_Tiny();
            //TestFileIteratorAndDistinct.testDistinct_TinyMaster();
            //TestFileIteratorAndDistinct.testDistinct_MegaMerge();

            TestFileIteratorAndDistinct.testDistinct_dir();

            return 0;
        }, (Object[]) args );
    }
}
