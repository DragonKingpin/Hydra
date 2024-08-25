package com.pinecone.framework.util.uoi;

import com.pinecone.framework.util.StringUtils;
import com.pinecone.framework.util.lang.DynamicFactory;
import com.pinecone.framework.util.lang.GenericDynamicFactory;

public class LocalUOIJavaClassProvider implements UniformObjectLoader {
    protected ClassLoader      mClassLoader;
    protected DynamicFactory   mDynamicFactory;

    public LocalUOIJavaClassProvider( ClassLoader classLoader, DynamicFactory dynamicFactory ) {
        this.mClassLoader    = classLoader;
        this.mDynamicFactory = dynamicFactory;
    }

    public LocalUOIJavaClassProvider( ClassLoader classLoader ) {
        this( classLoader, new GenericDynamicFactory( classLoader ));
    }

    public LocalUOIJavaClassProvider() {
        this( Thread.currentThread().getContextClassLoader() );
    }


    @Override
    public Class<? > toClass( UOI uoi ) throws IllegalArgumentException {
        if( !StringUtils.isEmpty( uoi.getHost() ) ) {
            throw new IllegalArgumentException( "Remote host [" + uoi.getHost() + "] is not supported." );
        }

        try{
            return this.mClassLoader.loadClass( uoi.getObjectName() );
        }
        catch ( ClassNotFoundException e ) {
            return null;
        }
    }

    @Override
    public Object newInstance( UOI uoi, Class<?>[] paramTypes, Object... args ) {
        return this.mDynamicFactory.optNewInstance( this.toClass(uoi), paramTypes, args );
    }

    @Override
    public Object newInstance( UOI uoi, Object... args ) {
        return this.mDynamicFactory.optNewInstance( this.toClass(uoi), null, args );
    }
}
