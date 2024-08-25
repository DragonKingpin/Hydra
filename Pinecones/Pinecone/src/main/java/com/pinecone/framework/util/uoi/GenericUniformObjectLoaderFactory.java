package com.pinecone.framework.util.uoi;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.pinecone.framework.system.NoSuchProviderException;
import com.pinecone.framework.util.lang.DynamicFactory;
import com.pinecone.framework.util.lang.GenericDynamicFactory;

public class GenericUniformObjectLoaderFactory implements UniformObjectLoaderFactory {
    protected Map<String, Class<? > >   mUOLRegister = new HashMap<>();
    protected ClassLoader               mClassLoader;
    protected DynamicFactory            mDynamicFactory;

    public GenericUniformObjectLoaderFactory( ClassLoader classLoader, DynamicFactory dynamicFactory ) {
        this.mClassLoader    = classLoader;
        this.mDynamicFactory = dynamicFactory;

        this.mUOLRegister.put( UniformObjectLoaderFactory.DefaultJavaClassType, LocalUOIJavaClassProvider.class );
    }

    public GenericUniformObjectLoaderFactory( ClassLoader classLoader ) {
        this( classLoader, new GenericDynamicFactory( classLoader ));
    }

    public GenericUniformObjectLoaderFactory() {
        this( Thread.currentThread().getContextClassLoader() );
    }


    @Override
    public Class<? > getUniformObjectLoader( String loaderName ) {
        return this.mUOLRegister.get( loaderName );
    }

    @Override
    public void register( String loaderName, Class<? > loader ) {
        this.mUOLRegister.put( loaderName, loader );
    }

    @Override
    public void deregister( String loaderName ) {
        this.mUOLRegister.remove( loaderName );
    }

    @Override
    public int size() {
        return this.mUOLRegister.size();
    }

    @Override
    public boolean isEmpty() {
        return this.mUOLRegister.isEmpty();
    }

    @Override
    public UniformObjectLoader newLoader( String loaderName ) throws NoSuchProviderException {
        Class<? > clazz = this.getUniformObjectLoader( loaderName );
        if( clazz == null ) {
            throw new NoSuchProviderException( loaderName );
        }

        try{
            Constructor constructor = clazz.getConstructor( ClassLoader.class, DynamicFactory.class );
            return (UniformObjectLoader) constructor.newInstance( this.mClassLoader, this.mDynamicFactory );
        }
        catch ( NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e ) {
            return null;
        }
    }

}
