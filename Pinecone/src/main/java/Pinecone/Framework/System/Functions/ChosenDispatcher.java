package Pinecone.Framework.System.Functions;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ChosenDispatcher implements SteerableSegment {
    public static Set<String > S_RESERVED = new TreeSet<String >() { { add( "default" ); } };
    private Map<String, Executable > mInnerFunctions;
    private Map<String, Object >     mDynamicData;
    private String                   mszCurrentChosen ;
    private Invoker                  mInvoker;

    public ChosenDispatcher( Map<String, Executable > innerFns ){
        this( null, innerFns, null );
    }

    public ChosenDispatcher( Map<String, Object > dynamicData, Map<String, Executable > innerFns ){
        this( dynamicData, innerFns, null );
    }

    public ChosenDispatcher( Map<String, Object > dynamicData, Map<String, Executable > innerFns, Invoker invoker ){
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
        Object ret = this.mInvoker.invoke( this.mInnerFunctions.get( this.mszCurrentChosen ), args ) ;
        this.mszCurrentChosen = szLastName;
        return ret;
    }

    @Override
    public void dispatch( Object... args ) throws Exception {
        if( args.length <= 0 ){
            throw new IllegalArgumentException( "No chosen be committed." );
        }

        this.mszCurrentChosen = (String) args[0];
        Executable fn = this.mInnerFunctions.get( this.mszCurrentChosen );
        if( fn == null ){
            fn = this.mInnerFunctions.get( "default" );
        }
        this.mInvoker.invoke( fn, this ) ;
    }
}
