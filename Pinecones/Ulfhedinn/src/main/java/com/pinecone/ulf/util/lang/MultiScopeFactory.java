package com.pinecone.ulf.util.lang;

import com.pinecone.framework.util.lang.ClassScope;
import com.pinecone.framework.util.lang.DynamicFactory;
import com.pinecone.framework.util.name.Name;
import com.pinecone.framework.util.name.ScopeName;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface MultiScopeFactory extends DynamicFactory {

    MultiTraitClassLoader getTraitClassLoader();

    default Object spawn( String name, Class<?>[] stereotypes, Object... args ) throws InvocationTargetException {
        return this.spawn( new ScopeName(name), stereotypes, args );
    }

    Object spawn( Name name, Class<?>[] stereotypes, Object... args ) throws InvocationTargetException;

    default Object spawn( String name, Object... args ) throws InvocationTargetException {
        return this.spawn( new ScopeName(name), args );
    }

    Object spawn( Name name, Object... args ) throws InvocationTargetException;


    default List popping( String name, Class<?>[] stereotypes, Object... args ) {
        return this.popping( new ScopeName(name), stereotypes, args );
    }

    List popping( Name name, Class<?>[] stereotypes, Object... args );

    default List popping( String name, Object... args ) {
        return this.popping( new ScopeName(name), args );
    }

    List popping( Name name, Object... args );

}