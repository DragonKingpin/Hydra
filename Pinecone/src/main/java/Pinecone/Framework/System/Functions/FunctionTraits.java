package Pinecone.Framework.System.Functions;

import Pinecone.Framework.System.util.ReflectionUtils;

import java.lang.reflect.Method;

public abstract class FunctionTraits {
    public static String thisName(){
        return FunctionTraits.thatName( 3 );
    }

    public static String thatName( int level ){
        return Thread.currentThread().getStackTrace()[ level ].getMethodName();
    }

    public static Object invoke ( Invokable fn, Object... obj ) throws Exception {
        return fn.invoke( obj );
    }

    public static Object invoke ( Executable fn, Object... obj ) throws Exception {
        if( fn instanceof Function ){
            return FunctionTraits.invoke( (Invokable) fn , obj );
        }
        else if ( fn instanceof Executor ){
            ( (Executor) fn ).execute();
            return null;
        }
        throw new IllegalArgumentException( "Not executable." );
    }

    public static Object invoke ( Object that, Method fn, Object...obj ) throws Exception {
        ReflectionUtils.makeAccessible( fn );
        try {
            return fn.invoke( that, obj );
        }
        catch ( IllegalArgumentException e ){
            return fn.invoke( that, new Object[]{ obj } );
        }
    }

    public static Object invoke ( Object that, String szFnName, Object... obj ) throws Exception {
        try { //Most likely...
            Method fn = that.getClass().getDeclaredMethod( szFnName, Object[].class );
            return FunctionTraits.invoke( that, fn, obj );
        }
        catch ( NoSuchMethodException nsm ){ // Try this...
            Class[] protoArgs = new Class[ obj.length ];
            int i = 0;
            for ( Object arg : obj ) {
                protoArgs [ i++ ] = arg.getClass();
            }

            try {
                Method fn = that.getClass().getDeclaredMethod( szFnName, protoArgs );
                return FunctionTraits.invoke( that, fn, obj );
            }
            catch ( NoSuchMethodException e ){ // Let's do savage way...
                Method[] fns = that.getClass().getDeclaredMethods();
                for( Method fn : fns ){
                    if( fn.getName().equals(szFnName) ){
                        try{
                            return FunctionTraits.invoke( that, fn, obj );
                        }
                        catch ( NoSuchMethodException | IllegalArgumentException againAndAgain ){ }
                    }
                }
                throw new NoSuchMethodException( "Exhaustively trialed, but still undefined founded." );
            }
        }
    }

}
