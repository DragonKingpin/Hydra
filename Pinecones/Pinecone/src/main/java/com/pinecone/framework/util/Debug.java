package com.pinecone.framework.util;

import com.pinecone.framework.system.InstantKillError;
import com.pinecone.framework.util.io.Tracer;
import com.pinecone.framework.util.io.Tracerson;
import com.pinecone.framework.util.json.JSON;

public class Debug {
    private final static Tracer console = new Tracerson();

    public static Tracer console() {
        return Debug.console;
    }

    public static Tracer probe(){
        System.err.println("\n\rFuck is here !\n\r");
        return Debug.console;
    }

    public static Tracer fmt( int nIndentFactor, Object Anything, Object...objects ){
        Debug.console.getOut().print( JSON.stringify( Anything, nIndentFactor ) );
        for ( Object row : objects ) {
            Debug.console.getOut().print( JSON.stringify( row, nIndentFactor ) );
        }
        return Debug.console;
    }

    public static Tracer trace( Object Anything, Object...objects ){
        return Debug.console.log( Anything, objects );
    }

    public synchronized static Tracer traceSyn( Object Anything, Object...objects ){
        return Debug.console.log( Anything, objects );
    }

    public static Tracer info ( Object Anything, Object...objects ){
        return Debug.console.info( Anything, objects );
    }

    public static Tracer warn ( Object Anything, Object...objects ){
        return Debug.console.warn( Anything, objects );
    }

    public static Tracer hhf(){
        Debug.console.getOut().println();
        return Debug.console;
    }


    public static Tracer echo(Object data, Object...objects ) {
        return Debug.console.echo( data, objects );
    }

    public static Tracer cerr(Object data, Object...objects ) {
        return Debug.console.cerr( data, objects );
    }


    public static void sleep( long millis ) {
        try {
            Thread.sleep( millis );
        }
        catch ( InterruptedException e ) {
            Debug.cerr( e.getMessage() );
        }
    }

    public static void stop() {
        throw new InstantKillError( "Invoked at [ Debug::stop() ]" );
    }

    public static void exit() {
        System.exit( -666 );
    }

}
