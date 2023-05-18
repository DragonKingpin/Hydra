package Pinecone.Framework.System.Functions;
import java.lang.reflect.Method;

public class SystemInvoker implements Invoker {
    @Override
    public Object invoke( Invokable  fn, Object... obj ) throws Exception {
        return FunctionTraits.invoke( fn, obj );
    }

    @Override
    public Object invoke( Executable fn, Object... obj ) throws Exception {
        return FunctionTraits.invoke( fn, obj );
    }

    @Override
    public Object invoke( Object that, Method fn, Object...obj ) throws Exception {
        return FunctionTraits.invoke( that, fn, obj );
    }

    @Override
    public Object invoke( Object that, String szFnName, Object... obj ) throws Exception {
        return FunctionTraits.invoke( that, szFnName, obj );
    }
}
