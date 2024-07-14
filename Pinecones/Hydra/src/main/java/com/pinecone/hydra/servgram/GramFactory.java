package com.pinecone.hydra.servgram;

import com.pinecone.framework.util.name.Name;
import com.pinecone.framework.util.name.ScopeName;
import com.pinecone.ulf.util.lang.MultiScopeFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface GramFactory extends MultiScopeFactory {
    @Override
    ClassLoader      getClassLoader();

    @Override
    GramScope        getClassScope();

    @Override
    MultiGramsLoader getTraitClassLoader();

    @Override
    default Servgram spawn( String name, Class<?>[] stereotypes, Object... args ) throws InvocationTargetException {
        return this.spawn( new ScopeName(name), stereotypes, args );
    }

    @Override
    Servgram spawn( Name name, Class<?>[] stereotypes, Object... args ) throws InvocationTargetException;

    @Override
    default Servgram spawn( String name, Object... args ) throws InvocationTargetException {
        return this.spawn( new ScopeName(name), args );
    }

    @Override
    Servgram spawn( Name name, Object... args ) throws InvocationTargetException;

    @Override
    default List<Servgram > popping( String name, Class<?>[] stereotypes, Object... args ) {
        return this.popping( new ScopeName(name), stereotypes, args );
    }

    @Override
    List<Servgram > popping( Name name, Class<?>[] stereotypes, Object... args );

    @Override
    default List<Servgram > popping( String name, Object... args ) {
        return this.popping( new ScopeName(name), args );
    }

    @Override
    List<Servgram > popping( Name name, Object... args );

}
