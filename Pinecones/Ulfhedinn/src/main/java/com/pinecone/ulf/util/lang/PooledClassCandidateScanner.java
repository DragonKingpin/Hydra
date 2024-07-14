package com.pinecone.ulf.util.lang;

import com.pinecone.framework.util.lang.*;
import javassist.ClassPool;

import java.io.IOException;

public class PooledClassCandidateScanner extends ClassCandidateScanner {
    protected ClassPool        mClassPool;

    public PooledClassCandidateScanner     ( ClassScope searchScope, ClassLoader classLoader, NSProtocolIteratorsFactoryAdapter iteratorsFactory, ClassPool classPool ) {
        super( searchScope, classLoader, iteratorsFactory );
        this.mClassPool = classPool;
    }

    public PooledClassCandidateScanner     ( ClassScope searchScope, ClassLoader classLoader, ClassPool classPool ) {
        this( searchScope, classLoader, new GenericScopeNSProtocolIteratorsFactory( classLoader, searchScope ), classPool );
    }

    public PooledClassCandidateScanner     ( ClassScope searchScope, ClassLoader classLoader ) {
        this( searchScope, classLoader, new GenericScopeNSProtocolIteratorsFactory( classLoader, searchScope ), ClassPool.getDefault() );
    }

    public void setClassPool ( ClassPool classPool ) {
        this.mClassPool = classPool;
    }

    @Override
    protected boolean filter( String szClassName ) {
        try{
            for ( TypeFilter filter : this.mIncludeFilters ) {
                if ( filter.match( szClassName, this.mClassPool ) ) {
                    return false;
                }
            }

            for ( TypeFilter filter : this.mExcludeFilters ) {
                if ( filter.match( szClassName, this.mClassPool ) ) {
                    return true;
                }
            }
        }
        catch ( IOException e ) {
            return true;
        }

        return false;
    }
}
