package com.pinecone.framework.util.lang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.util.lang.iterator.NamespaceIterator;

public class ObjectCandidateScanner implements ClassScanner {
    protected ClassLoader                          mClassLoader        ;
    protected ClassScope                           mSearchScope        ;
    protected List<TypeFilter >                    mIncludeFilters     ;
    protected List<TypeFilter >                    mExcludeFilters     ;
    protected List<NamespaceIteratorPair>          mIterators          ;
    protected NSProtocolIteratorsFactoryAdapter    mIteratorsFactory   ;

    public ObjectCandidateScanner     ( ClassScope searchScope, ClassLoader classLoader, NSProtocolIteratorsFactoryAdapter iteratorsFactory ) {
        this.mSearchScope        = searchScope       ;
        this.mClassLoader        = classLoader       ;
        this.mIncludeFilters     = new ArrayList<>() ;
        this.mExcludeFilters     = new ArrayList<>() ;
        this.mIterators          = new ArrayList<>() ;
        this.mIteratorsFactory   = iteratorsFactory  ;
    }

    public ObjectCandidateScanner     ( ClassScope searchScope, ClassLoader classLoader ) {
        this( searchScope, classLoader, new ClassScopeNSProtocolIteratorsFactory( classLoader, searchScope ) );
    }

    @Override
    public void addIncludeFilter     ( TypeFilter filter          ) {
        this.mIncludeFilters.add( filter );
    }

    @Override
    public void addExcludeFilter     ( TypeFilter filter          ) {
        this.mExcludeFilters.add( filter );
    }

    @Override
    public void addIterator          ( NamespaceIterator classIter, NamespaceIterator packageIter ) {
        this.addIterator( new NamespaceIteratorPair( classIter, packageIter ) );
    }

    protected void addIterator       ( NamespaceIteratorPair iteratorPair ) {
        this.mIterators.add( iteratorPair );
    }

    @Override
    public void scan( String szNSName, boolean bCollectChildPackage, List<String > candidates ) throws IOException {
        if ( this.mIterators.isEmpty() ) {
            this.mIteratorsFactory.prepareScopeIterators( szNSName, this.mIterators );
        }

        this.scan0( this.mIterators, bCollectChildPackage, candidates );
    }


    protected void scan0( List<NamespaceIteratorPair> pairs, boolean bCollectChildPackage, List<String > candidates ) throws IOException {
        for ( NamespaceIteratorPair pair : pairs ) {
            NamespaceIterator classIter = pair.classIter;
            NamespaceIterator pkgIter   = pair.packageIter;

            while ( classIter.hasNext() ) {
                String szClassName = classIter.next();
                if ( !this.filter( szClassName ) ) {
                    candidates.add( szClassName );
                }
            }

            if ( bCollectChildPackage ) {
                while ( pkgIter.hasNext() ) {
                    String szPackageName = pkgIter.next();
                    List<NamespaceIteratorPair> chridren = new ArrayList<>();
                    this.mIteratorsFactory.prepareIterators( szPackageName, chridren );
                    if ( !chridren.isEmpty() ) {
                        this.scan0( chridren, bCollectChildPackage, candidates );
                    }
                }
            }
        }

        // Clear cache, to prevent next unexpected iteration.
        pairs.clear();
    }

    protected boolean filter( String szClassName ) {
        try {
            for ( TypeFilter filter : this.mIncludeFilters ) {
                if ( filter.match( szClassName, null ) ) {
                    return false;
                }
            }

            for ( TypeFilter filter : this.mExcludeFilters ) {
                if ( filter.match( szClassName, null ) ) {
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