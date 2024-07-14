package com.pinecone.ulf.util.lang;

import com.pinecone.framework.system.ProvokeHandleException;
import com.pinecone.framework.system.executum.TaskManager;
import com.pinecone.framework.util.lang.ArchDynamicFactory;
import com.pinecone.framework.util.lang.ClassScope;
import com.pinecone.framework.util.name.Name;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class ArchMultiScopeFactory extends ArchDynamicFactory implements MultiScopeFactory {
    protected TaskManager             mTaskManager      ;
    protected MultiTraitClassLoader   mTraitClassLoader ;

    protected ArchMultiScopeFactory( TaskManager taskManager, ClassLoader classLoader, MultiTraitClassLoader traitClassLoader, ClassScope classScope ) {
        super( classLoader, classScope );
        this.mTaskManager       = taskManager       ;
        this.mTraitClassLoader  = traitClassLoader  ;
    }

    @Override
    public MultiTraitClassLoader getTraitClassLoader() {
        return this.mTraitClassLoader;
    }

    @Override
    public Object newInstance ( Class<? > that, Class<?>[] stereotypes, Object[] args ) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return super.newInstance( that, stereotypes, args );
    }

    @Override
    public Object spawn (Name name, Object... args ) throws InvocationTargetException {
        return this.spawn( name, null, args );
    }

    @Override
    public Object spawn ( Name name, Class<?>[] stereotypes, Object... args ) throws InvocationTargetException {
        List<Class<? > > classes = this.mTraitClassLoader.loads( name );

        Exception lastExp = null;
        if( !classes.isEmpty() ){
            for ( Class<? > c : classes ) {
                try {
                    return this.newInstance( c, stereotypes, args );
                }
                catch ( Exception e ) {
                    lastExp = e;
                }
            }
        }

        throw new InvocationTargetException(
                lastExp, String.format( "%s::spawn, what-> Spawning in all scopes, has compromised.", this.className() )
        );
    }

    @Override
    public List popping ( Name name, Object... args ) {
        return this.popping( name, null, args );
    }

    @Override
    public List popping ( Name name, Class<?>[] stereotypes, Object... args ) {
        List<Class<? > > classes = this.mTraitClassLoader.loads( name ); // Try load by explicit name, saving times.

        List<Object > list = new ArrayList<>();

        if( !classes.isEmpty() ){
            for ( Class<? > c : classes ) {
                try {
                    Object o = this.newInstance( c, stereotypes, args );
                    if( o != null ) {
                        list.add( o ) ;
                    }
                }
                catch ( Exception e ) {
                    this.handleIgnoreException( e );
                }
            }
        }

        return list;
    }

    protected void handleIgnoreException( Exception e ) throws ProvokeHandleException {
        // Just ignore them.
        e.printStackTrace();
    }
}
