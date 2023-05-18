package Pinecone.Framework.System.Functions;
import java.lang.reflect.Method;

public interface Invoker {
    Object invoke ( Invokable  fn, Object...obj ) throws Exception ;

    Object invoke ( Executable fn, Object...obj ) throws Exception ;

    Object invoke ( Object that, Method fn, Object...obj ) throws Exception ;

    Object invoke ( Object that, String szFnName, Object...obj ) throws Exception ;
}
