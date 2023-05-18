package Pinecone.Framework.Debug;

import Pinecone.Framework.System.IO.Console;

public class Debug {
    private final static Console console = new Console();

    public static Console console() {
        return Debug.console;
    }

    public static Console probe(){
        System.err.println("\n\rFuck is here !\n\r");
        return Debug.console;
    }

    public static Console trace( Object Anything, Object...objects ){
        return Debug.console.log( Anything, objects );
    }

    public static Console hhf(){
        Debug.console.getOut().println();
        return Debug.console;
    }


    public static Console echo( Object data, Object...objects ) {
        return Debug.console.echo( data, objects );
    }

    public static Console cerr( Object data, Object...objects ) {
        return Debug.console.cerr( data, objects );
    }

}
