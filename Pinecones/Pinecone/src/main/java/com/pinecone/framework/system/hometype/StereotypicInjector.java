package com.pinecone.framework.system.hometype;

public interface StereotypicInjector extends Injector {
    Class<?> getStereotype();

    void     setStereotype( Class<?> stereotype );
}
