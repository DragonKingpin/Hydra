package Pinecone.Framework.System.Functions;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class LinearDispatcher implements SteerableSegment {
    public static Set<String > S_RESERVED = new TreeSet<String >() { { add( "init" ); add( "final" ); } };
    private Map<String, Executable > mInnerFunctions;
    private Map<String, Object >     mDynamicData;
    private String                   mszCurrentChosen ;
    private Invoker                  mInvoker;

    public LinearDispatcher( Map<String, Executable > innerFns ){
        this( null, innerFns, null );
    }

    public LinearDispatcher( Map<String, Object > dynamicData, Map<String, Executable > innerFns ){
        this( dynamicData, innerFns, null );
    }

    public LinearDispatcher( Map<String, Object > dynamicData, Map<String, Executable > innerFns, Invoker invoker ){
        this.mInnerFunctions = innerFns;
        this.mDynamicData    =  dynamicData != null ? dynamicData : new LinkedHashMap<>();
        this.mInvoker        = invoker != null ? invoker : new SystemInvoker();
    }

    @Override
    public Map<String, Object > data(){
        return this.mDynamicData;
    }

    @Override
    public String name() {
        return this.mszCurrentChosen;
    }

    @Override
    public Object invoke( String fnName, Object...args ) throws Exception {
        String szLastName = this.mszCurrentChosen;
        this.mszCurrentChosen = fnName;
        Object ret = this.mInvoker.invoke( this.mInnerFunctions.get( fnName ), args ) ;
        this.mszCurrentChosen = szLastName;
        return ret;
    }

    @Override
    public void dispatch( Object... args ) throws Exception {
        boolean bNotIgnoreExp = args.length > 0 && (boolean) args[0];

        try{
            this.mszCurrentChosen = "init";
            Executable fnInit = this.mInnerFunctions.get( this.mszCurrentChosen );

            if( fnInit instanceof Function ){
                Object ret = ( ( Function ) fnInit ).invoke( this );
                if( ret instanceof Boolean && !(boolean) ret ){
                    return;
                }
            }
            else {
                this.mInvoker.invoke( this.mInnerFunctions.get( this.mszCurrentChosen ), this ) ;
            }
        }
        catch ( Exception e ){
            if( bNotIgnoreExp ){
                throw e;
            }
        }


        for( Object each : this.mInnerFunctions.entrySet() ){
            Map.Entry kv = ( Map.Entry ) each;
            this.mszCurrentChosen = (String) kv.getKey();
            if( LinearDispatcher.S_RESERVED.contains( this.mszCurrentChosen ) ){
                continue;
            }

            try {
                this.mInvoker.invoke( (Executable) kv.getValue(), this );
            }
            catch ( Exception e ){
                if( bNotIgnoreExp ){
                    throw e;
                }
            }
        }


        try {
            this.mszCurrentChosen = "final";
            this.mInvoker.invoke( this.mInnerFunctions.get( this.mszCurrentChosen ), this ) ;
        }
        catch ( Exception e ){
            if( bNotIgnoreExp ){
                throw e;
            }
        }

    }
}
