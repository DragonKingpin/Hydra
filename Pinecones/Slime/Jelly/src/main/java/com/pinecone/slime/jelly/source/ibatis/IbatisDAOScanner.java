package com.pinecone.slime.jelly.source.ibatis;

import com.pinecone.framework.util.lang.ClassScope;
import com.pinecone.framework.util.lang.GenericScopeNSProtocolIteratorsFactory;
import com.pinecone.framework.util.lang.NSProtocolIteratorsFactoryAdapter;
import com.pinecone.slime.source.DAOScanner;
import com.pinecone.ulf.util.lang.GenericPreloadClassInspector;
import com.pinecone.ulf.util.lang.HierarchyClassInspector;
import com.pinecone.ulf.util.lang.PooledClassCandidateScanner;
import com.pinecone.ulf.util.lang.SimpleAnnotationExcludeFilter;
import javassist.ClassPool;

public class IbatisDAOScanner extends PooledClassCandidateScanner implements DAOScanner {
    protected HierarchyClassInspector mClassInspector     ;

    public IbatisDAOScanner     ( ClassScope searchScope, ClassLoader classLoader, NSProtocolIteratorsFactoryAdapter iteratorsFactory, ClassPool classPool ) {
        super( searchScope, classLoader, iteratorsFactory, classPool );

        this.mClassInspector = new GenericPreloadClassInspector( this.mClassPool );
        this.addExcludeFilter( new SimpleAnnotationExcludeFilter( this.mClassInspector, IbatisDataAccessObject.class ) );
    }

    public IbatisDAOScanner     ( ClassScope searchScope, ClassLoader classLoader, ClassPool classPool ) {
        this( searchScope, classLoader, new GenericScopeNSProtocolIteratorsFactory( classLoader, searchScope ), classPool );
    }

    public IbatisDAOScanner     ( ClassScope searchScope, ClassLoader classLoader ) {
        this( searchScope, classLoader, new GenericScopeNSProtocolIteratorsFactory( classLoader, searchScope ), ClassPool.getDefault() );
    }
}
