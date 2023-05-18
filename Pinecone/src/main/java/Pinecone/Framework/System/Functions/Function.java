package Pinecone.Framework.System.Functions;

public interface Function extends Executable, Invokable {
    @Override
    Object invoke( Object...obj ) throws Exception;
}
