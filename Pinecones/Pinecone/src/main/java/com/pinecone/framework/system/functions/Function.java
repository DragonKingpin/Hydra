package com.pinecone.framework.system.functions;

public interface Function extends Executable, Invokable {
    @Override
    Object invoke( Object...obj ) throws Exception;
}
