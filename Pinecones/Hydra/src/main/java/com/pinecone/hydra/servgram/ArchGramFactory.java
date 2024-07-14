package com.pinecone.hydra.servgram;

import com.pinecone.framework.system.ProvokeHandleException;
import com.pinecone.framework.system.executum.TaskManager;
import com.pinecone.framework.util.name.Name;
import com.pinecone.framework.util.name.ScopeName;
import com.pinecone.ulf.util.lang.ArchMultiScopeFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class ArchGramFactory extends ArchMultiScopeFactory implements GramFactory {
    protected ArchGramFactory( TaskManager taskManager, ClassLoader classLoader, MultiGramsLoader gramLoader, GramScope gramScope ) {
        super( taskManager, classLoader, gramLoader, gramScope );
    }

    @Override
    public ClassLoader getClassLoader() {
        return this.mClassLoader;
    }

    @Override
    public GramScope getClassScope() {
        return (GramScope) super.getClassScope();
    }

    @Override
    public MultiGramsLoader getTraitClassLoader() {
        return (MultiGramsLoader) super.getTraitClassLoader();
    }

    public Servgram newInstance ( Class<? > that, Class<?>[] stereotypes, Object[] args ) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return (Servgram) super.newInstance( that, stereotypes, args );
    }

    @Override
    public Servgram spawn ( Name name, Object... args ) throws InvocationTargetException {
        return this.spawn( name, null, args );
    }

    @Override
    public Servgram spawn ( Name name, Class<?>[] stereotypes, Object... args ) throws InvocationTargetException {
        return (Servgram) super.spawn( name, stereotypes, args );
    }

    @Override
    public List<Servgram > popping ( Name name, Object... args ) {
        return this.popping( name, null, args );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public List<Servgram > popping ( Name name, Class<?>[] stereotypes, Object... args ) {
        return (List<Servgram >) super.popping( name, stereotypes, args );
    }
}
