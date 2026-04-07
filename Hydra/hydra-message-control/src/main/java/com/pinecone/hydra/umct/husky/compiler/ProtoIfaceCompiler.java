package com.pinecone.hydra.umct.husky.compiler;

public interface ProtoIfaceCompiler extends IfaceCompiler {

    CompilerEncoder getCompilerEncoder();

    ClassDigest compile ( String className, boolean bAsIface, CompilerEncoder encoder );

    ClassDigest compile ( Class<? > clazz, boolean bAsIface, CompilerEncoder encoder );

    ClassDigest reinterpret ( String className, boolean bAsIface );

    ClassDigest reinterpret ( Class<? > clazz, boolean bAsIface );

}
