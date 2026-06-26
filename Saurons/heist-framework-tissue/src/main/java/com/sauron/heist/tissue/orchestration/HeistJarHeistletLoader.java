package com.sauron.heist.tissue.orchestration;

import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.util.lang.ClassFilter;
import com.pinecone.framework.util.name.Name;
import com.pinecone.hydra.servgram.MultiGramsLoader;
import com.pinecone.hydra.servgram.Servgram;
import com.pinecone.hydra.servgram.filters.AnnotationValueFilter;
import com.sauron.heist.tissue.protocol.HeistJarRegistry;

public class HeistJarHeistletLoader implements MultiGramsLoader {
    protected HeistJarRegistry mRegistry;

    public HeistJarHeistletLoader( HeistJarRegistry registry ) {
        this.mRegistry = registry;
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
        return new ArrayList<>( this.mRegistry.queryHeistlets( name.getName() ) );
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
        return this;
    }

    @Override
    public void addIncludeFilter( ClassFilter includeFilter ) {
    }

    @Override
    public void addExcludeFilter( ClassFilter excludeFilter ) {
    }

    @Override
    public void resetFilters( boolean useDefaultFilters ) {
    }

    @Override
    public void setAnnotationValueFilter( AnnotationValueFilter filter ) {
    }

    @Override
    public void clearCache() {
    }
}
