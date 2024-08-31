package com.pinecone.framework.util.uoi;

import com.pinecone.framework.system.NoSuchProviderException;
import com.pinecone.framework.system.prototype.Pinenut;

public interface UniformObjectLoaderFactory extends Pinenut {
    String DefaultJavaClassType = "java-class";
    UniformObjectLoaderFactory DefaultObjectLoaderFactory = new GenericUniformObjectLoaderFactory();



    Class<? > getUniformObjectLoader( String loaderName ) ;

    void register( String loaderName, Class<? > loader ) ;

    void deregister( String loaderName );

    int size();

    boolean isEmpty();

    UniformObjectLoader newLoader( String loaderName ) throws NoSuchProviderException;
}
