package Pinecone.Framework.System;

import Pinecone.Framework.System.Functions.Function;
import Pinecone.Pinecone;

public final class Framework {
    private static long gnBootTime;

    private static Function gfnAfterGlobalExpCaught = new Function() {
        @Override
        public Object invoke( Object... obj ) throws Exception {
            System.err.println( "Unhandled Exception in \"main()\" : \n" );
            ( ( Throwable ) obj[0] ).printStackTrace();
            return null;
        }
    };

    public static void registerPineExpCatcher( Function fn ){
        Framework.gfnAfterGlobalExpCaught = fn;
    }

    public static long getBootTime(){
        return Framework.gnBootTime;
    }

    public static long getRunTime(){ //This function is using to calculate program run time
        return System.currentTimeMillis() - Framework.gnBootTime;
    }

    public static void traceRunTime() {
        System.out.print( String.format(
                "\n%s Runtime : %d /ms !\n", Pinecone.MY_PROGRAM_NAME, Framework.getRunTime()
        ) );
    }

    private static void initCommit() throws Throwable {
        Framework.gnBootTime = System.currentTimeMillis();
    }

    public static int inti ( Function hfnInlet, Object...args ) throws Exception {
        int nRetNum = 0;

        try {
            Framework.initCommit();
            nRetNum = (int) hfnInlet.invoke( args );

            if( Pinecone.S_DEBUG_MODE ){
                Framework.traceRunTime();
            }
        }
        catch ( Throwable throwable ){
            Framework.gfnAfterGlobalExpCaught.invoke( throwable );
            nRetNum = -1;
        }

        return nRetNum;
    }
}
