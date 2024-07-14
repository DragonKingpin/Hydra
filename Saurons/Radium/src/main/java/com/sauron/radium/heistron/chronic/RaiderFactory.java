package com.sauron.radium.heistron.chronic;

import com.pinecone.framework.util.lang.ClassScope;
import com.pinecone.framework.util.name.Name;
import com.pinecone.framework.util.name.ScopeName;
import com.pinecone.ulf.util.lang.MultiScopeFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface RaiderFactory extends MultiScopeFactory {
    @Override
    ClassLoader       getClassLoader();

    @Override
    ClassScope        getClassScope();

    @Override
    MultiRaiderLoader getTraitClassLoader();

    @Override
    default Raider spawn( String name, Class<?>[] stereotypes, Object... args ) throws InvocationTargetException {
        return this.spawn( new ScopeName(name), stereotypes, args );
    }

    @Override
    Raider spawn( Name name, Class<?>[] stereotypes, Object... args ) throws InvocationTargetException;

    @Override
    default Raider spawn( String name, Object... args ) throws InvocationTargetException {
        return this.spawn( new ScopeName(name), args );
    }

    @Override
    Raider spawn( Name name, Object... args ) throws InvocationTargetException;

    @Override
    default List<Raider > popping( String name, Class<?>[] stereotypes, Object... args ) {
        return this.popping( new ScopeName(name), stereotypes, args );
    }

    @Override
    List<Raider > popping( Name name, Class<?>[] stereotypes, Object... args );

    @Override
    default List<Raider > popping( String name, Object... args ) {
        return this.popping( new ScopeName(name), args );
    }

    @Override
    List<Raider > popping( Name name, Object... args );

}