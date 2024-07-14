package com.servgram;

import com.pinecone.Pinecone;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSON;
import com.pinecone.framework.util.lang.*;
import com.pinecone.ulf.util.lang.PooledClassCandidateScanner;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

import java.util.ArrayList;
import java.util.List;

public class TestServgram {
    public static void testPackageCollector() throws Exception {
        Debug.trace( Thread.currentThread().getContextClassLoader().getResource("com/mysql/jdbc") );
        //Debug.trace( Thread.currentThread().getContextClassLoader().getResource("com/pinecone/hydra") );

        NamespaceCollector collector = new ClassNameFetcher();
        //NamespaceCollector collector = new PackageNameFetcher();
        //Debug.echo( JSON.stringify( collector.fetch( "com.mysql", true ), 2 ) );
        Debug.echo( JSON.stringify( collector.fetch( "com.pinecone.hydra", true ), 2 ) );
        Debug.echo( collector.fetchFirst( "com.pinecone.hydra" ) );

        //TestServgram.class.getClassLoader().loadClass()
        //Debug.trace( Package.getPackage( "com.pinecone.hydra.servgram" ) );
    }

    public static void testPackageScope() throws Exception {
        ScopedPackage scopedPackage = new LazyScopedPackage( "com.pinecone.hydra" );

        Debug.trace( scopedPackage.children().get( 3 ).fetchChildrenClassNames() );

        Debug.trace( scopedPackage.getPackage() );
        Debug.trace( scopedPackage.tryLoad() );
        Debug.trace( scopedPackage.getPackage(), scopedPackage.hasLoaded() );
        Debug.trace( scopedPackage.fetchFirstClassName() );

        //ClassPathScanningCandidateComponentProvider
    }

    public static void testIterator() throws Exception {
        //NamespaceIterator iterator = new DirectoryClassIterator( "/E:/MyFiles/CodeScript/Project/Hazelnut/Sauron/Saurons/Hydra/target/classes/com/pinecone/hydra/umc/wolfmc", "com.pinecone.hydra.umc.wolfmc" );
        //NamespaceIterator iterator = new DirectoryPackageIterator( "/E:/MyFiles/CodeScript/Project/Hazelnut/Sauron/Saurons/Hydra/target/classes/com/pinecone/hydra/umc", "com.pinecone.hydra.umc" );
        //NamespaceIterator iterator = new JarClassIterator( "jar:file:/C:/Users/undefined/.m2/repository/mysql/mysql-connector-java/8.0.23/mysql-connector-java-8.0.23.jar!/com/mysql/jdbc" );
        NamespaceIterator iterator = new JarPackageIterator( "jar:file:/C:/Users/undefined/.m2/repository/mysql/mysql-connector-java/8.0.23/mysql-connector-java-8.0.23.jar!/com/mysql" );

        while ( iterator.hasNext() ) {
            Debug.trace( iterator.next() );
        }
    }

    public static void testScanner() throws Exception {
        ClassScanner scanner = new PooledClassCandidateScanner( null, Thread.currentThread().getContextClassLoader() );
        PooledClassCandidateScanner scanner1 = (PooledClassCandidateScanner) scanner;

        List<String > list = new ArrayList<>();
        scanner1.scan( "com.pinecone.hydra.umc", true, list );
        //scanner1.scan( "com.mysql.jdbc", true, list );
        //scanner1.scan( "com.mysql.jdbc", false, list );

        Debug.echo( JSON.stringify( list, 2 ) );
    }

    public static void main( String[] args ) throws Exception {
        //String szJson = FileUtils.readAll("J:/120KWordsPhonetics.json5");
        Pinecone.init( (Object...cfg )->{

            //TestServgram.testPackageCollector();
            //TestServgram.testPackageScope();
            //TestServgram.testIterator();
            TestServgram.testScanner();


            return 0;
        }, (Object[]) args );
    }
}
