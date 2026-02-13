package com.pinecone.hydra.umct.husky.compiler;

public interface IfaceCompiler extends IfaceInspector {

    ClassDigest compile ( String className, boolean bAsIface );

    ClassDigest compile ( Class<? > clazz, boolean bAsIface );

}
