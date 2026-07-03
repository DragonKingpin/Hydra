package com.sauron.heist.tissue.orchestration;

import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.util.lang.ClassFilter;
import com.pinecone.framework.util.name.Name;
import com.pinecone.hydra.servgram.MultiGramsLoader;
import com.pinecone.hydra.servgram.Servgram;
import com.pinecone.hydra.servgram.filters.AnnotationValueFilter;

public class CompositeHeistletLoader implements MultiGramsLoader {
    protected List<MultiGramsLoader> mLoaders;

    public CompositeHeistletLoader() {
        this.mLoaders = new ArrayList<>();
    }

    public CompositeHeistletLoader addLoader( MultiGramsLoader loader ) {
        if ( loader != null ) {
            this.mLoaders.add( loader );
        }
        return this;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Class<? extends Servgram> load( Name name ) throws ClassNotFoundException {
        List<Class<?>> classes = this.loads( name );
        if ( classes.isEmpty() ) {
            throw new ClassNotFoundException( name.getName() );
        }
        return (Class<? extends Servgram>) classes.get( 0 );
    }

    @Override
    public Class<? extends Servgram> loadByName( Name simpleName ) throws ClassNotFoundException {
        return this.load( simpleName );
    }

    @Override
    public Class<? extends Servgram> loadInClassTrait( Name simpleName ) throws ClassNotFoundException {
        return this.load( simpleName );
    }

    @Override
    public List<Class<?>> loads( Name name ) {
        List<Class<?>> classes = new ArrayList<>();
        for ( MultiGramsLoader loader : this.mLoaders ) {
            List<Class<?>> candidateClasses = loader.loads( name );
            if ( candidateClasses != null ) {
                classes.addAll( candidateClasses );
            }
        }
        return classes;
    }

    @Override
    public List<Class<?>> loadsByName( Name simpleName ) {
        return this.loads( simpleName );
    }

    @Override
    public List<Class<?>> loadsInClassTrait( Name simpleName ) {
        return this.loads( simpleName );
    }

    @Override
    public MultiGramsLoader updateScope() {
        for ( MultiGramsLoader loader : this.mLoaders ) {
            loader.updateScope();
        }
        return this;
    }

    @Override
    public void addIncludeFilter( ClassFilter includeFilter ) {
        for ( MultiGramsLoader loader : this.mLoaders ) {
            loader.addIncludeFilter( includeFilter );
        }
    }

    @Override
    public void addExcludeFilter( ClassFilter excludeFilter ) {
        for ( MultiGramsLoader loader : this.mLoaders ) {
            loader.addExcludeFilter( excludeFilter );
        }
    }

    @Override
    public void resetFilters( boolean useDefaultFilters ) {
        for ( MultiGramsLoader loader : this.mLoaders ) {
            loader.resetFilters( useDefaultFilters );
        }
    }

    @Override
    public void setAnnotationValueFilter( AnnotationValueFilter filter ) {
        for ( MultiGramsLoader loader : this.mLoaders ) {
            loader.setAnnotationValueFilter( filter );
        }
    }

    @Override
    public void clearCache() {
        for ( MultiGramsLoader loader : this.mLoaders ) {
            loader.clearCache();
        }
    }
}
