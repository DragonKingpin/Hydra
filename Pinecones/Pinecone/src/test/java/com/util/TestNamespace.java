package com.util;

import com.pinecone.Pinecone;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.name.*;

import java.util.List;

public class TestNamespace {
    public static void testNS() throws Exception {
        GenericNamespaceParser parser = new GenericNamespaceParser();
        Namespace parsedNamespace = parser.parse( "x1::x2/x3\\x4->x5.x6.x7->x8.x9.x10.x11::x12.x13.x14", List.of( "::", ".", "->", "\\", "/" ) );
        Debug.trace(  parsedNamespace.getFullName() );

        parsedNamespace = parser.parse( "x1::x2/x3\\x4->x5.x6.x7->x8.x9.x10.x11::x12.x13.x14", "::|\\.|->|\\\\|/" );
        Debug.trace(  parsedNamespace.getFullName(), parsedNamespace.getSimpleName(), parsedNamespace );


        Namespace namespace = new UniNamespace( "Jesus", new UniNamespace( "this" ) );
        Debug.trace( namespace.getFullName(), namespace.parent().getSimpleName(), parsedNamespace.root() );
    }

    public static void testMultiNS() throws Exception {
        MultiNamespace root = new GenericMultiNamespace( "root" );


        MultiNamespace namespace = new GenericMultiNamespace( "x2" );
        namespace.addParent( new GenericMultiNamespace( "x1_0", root ) );
        namespace.addParent( new GenericMultiNamespace( "x1_1", root ) );

        Debug.trace( namespace.getFullNames(), namespace.hasOwnParentNS( "x1_0" ) );
        Debug.trace( namespace.hasOwnParent( new GenericMultiNamespace( "x1_0", root ) ) );  // root.x1_0
        Debug.trace( namespace.hasOwnParent( new GenericMultiNamespace( "x1_0" ) ) );  // x1_0

        Debug.trace( namespace.getParentByNS( "x1_0" ).getFullName(), namespace.getParents(), namespace.getDomain() );


        GenericNamespaceParser parser = new GenericNamespaceParser( GenericMultiNamespace.class );
        Namespace parsedNamespace = parser.parse( "x1::x2/x3\\x4->x5.x6.x7->x8.x9.x10.x11::x12.x13.x14", List.of( "::", ".", "->", "\\", "/" ) );
        Debug.trace(  parsedNamespace.getFullName(), parsedNamespace.getDomain() );
    }

    public static void main( String[] args ) throws Exception {
        //String szJson = FileUtils.readAll("J:/120KWordsPhonetics.json5");
        Pinecone.init( (Object...cfg )->{


            TestNamespace.testNS();
            //TestNamespace.testMultiNS();


            return 0;
        }, (Object[]) args );
    }
}