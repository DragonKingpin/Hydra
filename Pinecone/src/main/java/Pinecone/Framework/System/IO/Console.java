package Pinecone.Framework.System.IO;

import Pinecone.Framework.System.Prototype.PinenutTraits;
import Pinecone.Framework.Util.JSON.JSONSerializer;

import java.io.PrintStream;

public class Console {
    protected PrintStream out = System.out;

    protected PrintStream err = System.err;

    public String OUT_PUT_SPILT = " ";

    public Console() {

    }

    public Console( PrintStream out,  PrintStream err ) {
        this.out = out;
        this.err = err;
    }

    protected String stringify( Object data ){
        try {
            return PinenutTraits.invokeToJSONString( data );
        }
        catch ( Exception e1 ){
            return JSONSerializer.stringify( data );
        }
    }

    public PrintStream getOut() {
        return this.out;
    }

    public PrintStream getErr() {
        return this.err;
    }



    public Console echo( Object data, Object...objects ) {
        this.out.print( data );
        for ( Object row : objects ) {
            this.out.print( row );
        }
        return this;
    }

    public Console cerr( Object data, Object...objects ) {
        this.err.print( data );
        for ( Object row : objects ) {
            this.err.print( row );
        }
        return this;
    }



    public Console info( Object that ){
        this.out.print( this.stringify( that ) );
        return this;
    }

    public Console info( Object Anything, Object...objects ){
        this.info( Anything );
        for ( Object row : objects ) {
            this.out.print( this.OUT_PUT_SPILT );
            this.info( row );
        }
        return this;
    }

    public Console log( Object that ){
        this.info( that );
        this.out.println();
        return this;
    }

    public Console log( Object Anything, Object...objects ){
        this.info( Anything,objects );
        this.out.println();
        return this;
    }

    public Console error( Object Anything, Object...objects ){
        this.error( Anything );
        for ( Object row : objects ) {
            this.err.print( this.OUT_PUT_SPILT );
            this.error( row );
        }
        return this;
    }

    public Console error ( Object that ){
        this.err.print( this.stringify( that ) );
        return this;
    }

    protected void printTraceInfo( StackTraceElement[] elements ){
        this.err.println( this.getClass().getName() + ": Call Trace Info:");
        if( elements != null ){
            for( int i = 0; i < elements.length; i++ ){
                if( i == 0 && elements[0].getClassName().equals( "java.lang.Thread" ) ){
                    continue;
                }
                this.err.println( "\tat " + elements[i] );
            }
        }
    }

    public Console trace() {
        this.printTraceInfo( Thread.currentThread().getStackTrace() );
        return this;
    }

    public Console trace( Object Anything, Object...objects ) {
        this.info( Anything, objects );
        this.out.println();
        this.printTraceInfo( Thread.currentThread().getStackTrace() );
        return this;
    }

}
