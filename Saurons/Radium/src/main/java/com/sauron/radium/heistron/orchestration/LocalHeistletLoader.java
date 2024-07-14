package com.sauron.radium.heistron.orchestration;

import com.pinecone.hydra.servgram.ArchGramLoader;
import com.pinecone.hydra.servgram.GramFactory;
import com.pinecone.hydra.servgram.GramScope;
import com.pinecone.hydra.servgram.filters.AnnotationValueFilter;
import com.sauron.radium.heistron.Heistum;
import javassist.ClassPool;
import javassist.bytecode.annotation.Annotation;

public class LocalHeistletLoader extends ArchGramLoader {
    protected AnnotationValueFilter mAnnoValueFilter    ;

    public LocalHeistletLoader( GramScope classScope, ClassLoader classLoader, ClassPool classPool ) {
        super( classScope, classLoader, classPool );

        this.mClassScanner.addExcludeFilter( new ExcludeHeistletFilters( this.mClassInspector ) );
        this.setAnnotationValueFilter( new HeistletAnnotationValueFilter() );
    }

    public LocalHeistletLoader( GramScope classScope, ClassLoader classLoader ) {
        this( classScope, classLoader, ClassPool.getDefault() );
    }

    public LocalHeistletLoader( GramFactory factory ) {
        this( factory.getClassScope(), factory.getClassLoader() );
    }


    public void setAnnotationValueFilter( AnnotationValueFilter filter ) {
        this.mAnnoValueFilter = filter;
    }

    @Override
    protected boolean isAnnotationQualified( Annotation that, String szName ) {
        return !this.mAnnoValueFilter.match( that, szName );
    }

    @Override
    protected Class<? extends Heistum > loadSingleByFullClassName( String szFullClassName ) {
        try {
            Class<?> clazz = this.mClassLoader.loadClass( szFullClassName );
            if( this.filter( clazz ) ) {
                return null;
            }
            if ( Heistum.class.isAssignableFrom( clazz ) ) {
                return clazz.asSubclass( Heistum.class );
            }
        }
        catch ( ClassNotFoundException e ) {
            return null;
        }

        return null;
    }
}
