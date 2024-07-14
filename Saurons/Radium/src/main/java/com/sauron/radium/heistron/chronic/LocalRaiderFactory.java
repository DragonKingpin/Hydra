package com.sauron.radium.heistron.chronic;

import com.pinecone.framework.system.executum.TaskManager;
import com.pinecone.framework.util.lang.ClassScope;
import com.pinecone.framework.util.name.Name;
import com.pinecone.ulf.util.lang.ArchMultiScopeFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class LocalRaiderFactory extends ArchMultiScopeFactory implements RaiderFactory {
    public LocalRaiderFactory( TaskManager taskManager, ClassLoader classLoader, MultiRaiderLoader raiderLoader, ClassScope classScope ) {
        super( taskManager, classLoader, raiderLoader, classScope );
    }

    public LocalRaiderFactory( TaskManager taskManager ) {
        this( taskManager, taskManager.getClassLoader(), null, null );

        this.mClassScope        = new LocalRaiderScopeSet( this );
        this.mTraitClassLoader  = new LocalMultiRaiderLoader( this );
    }

    public LocalRaiderFactory( TaskManager taskManager, ClassScope classScope ) {
        this( taskManager, taskManager.getClassLoader(), null, classScope );

        this.mTraitClassLoader = new LocalMultiRaiderLoader( this );
    }

    @Override
    public MultiRaiderLoader getTraitClassLoader() {
        return (MultiRaiderLoader) super.getTraitClassLoader();
    }

    @Override
    public Raider newInstance ( Class<? > that, Class<?>[] stereotypes, Object[] args ) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return (Raider) super.newInstance( that, stereotypes, args );
    }

    @Override
    public Raider spawn ( Name name, Object... args ) throws InvocationTargetException {
        return this.spawn( name, null, args );
    }

    @Override
    public Raider spawn ( Name name, Class<?>[] stereotypes, Object... args ) throws InvocationTargetException {
        return (Raider) super.spawn( name, stereotypes, args );
    }

    @Override
    public List<Raider > popping ( Name name, Object... args ) {
        return this.popping( name, null, args );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public List<Raider > popping ( Name name, Class<?>[] stereotypes, Object... args ) {
        return (List<Raider >) super.popping( name, stereotypes, args );
    }
}
