package com.pinecone.hydra.proc.image;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.architecture.CascadeComponent;
import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.system.HyComponent;
import com.pinecone.hydra.system.Hydrogen;
import com.pinecone.hydra.system.centrum.UniformCentralSystem;

public class UniformMultiScopeImageLoader extends ArchImageLoader implements UniformImageLoader {

    protected Map<String, ImageLoader> mLoaderScope;

    protected ImageLoader mLocalMappingImageLoader;

    public UniformMultiScopeImageLoader( Namespace name, Hydrogen system, HyComponent parent, ClassLoader classLoader, @Nullable ImageLoader localMappingImageLoader ) {
        super( name, system, parent, classLoader );

        this.mLoaderScope             = new ConcurrentHashMap<>();
        this.mLocalMappingImageLoader = localMappingImageLoader;

        if ( this.mLocalMappingImageLoader == null ) {
            if ( system instanceof UniformCentralSystem ) {
                UniformImageLoader pl = (UniformImageLoader) ( (UniformCentralSystem) system ).imageLoader();
                this.mLocalMappingImageLoader = pl.localMappingImageLoader();
            }
            else {
                throw new IllegalArgumentException( "`UniformMultiScopeImageLoader` must includes at least one `localMappingImageLoader`." );
            }
        }

        this.mLoaderScope.put( "uofs", this.mLocalMappingImageLoader );
    }

    public UniformMultiScopeImageLoader( Hydrogen system, HyComponent parent, @Nullable ImageLoader localMappingImageLoader ) {
        this( null, system, parent, system.getGlobalClassLoader(), localMappingImageLoader );
    }

    public UniformMultiScopeImageLoader( Hydrogen system, HyComponent parent ) {
        this( null, system, parent, system.getGlobalClassLoader(), null );
    }


    public UniformMultiScopeImageLoader( Hydrogen system, @Nullable ImageLoader localMappingImageLoader ) {
        this( system, null, localMappingImageLoader );
    }

    @Override
    public ImageLoader localMappingImageLoader() {
        return this.mLocalMappingImageLoader;
    }

    @Override
    public void addScope( String protocol, ImageLoader imageLoader ) {
        this.mLoaderScope.put( protocol.toLowerCase(), imageLoader );
    }

    @Override
    public ImageLoader getScope( String protocol ) {
        return this.mLoaderScope.get( protocol.toLowerCase() );
    }

    @Override
    public ExecutionImage queryExecutionImage( URI uri ) {
        if ( uri == null || uri.getScheme() == null ) {
            return null;
        }
        String p = uri.getScheme().toLowerCase();
        ImageLoader imageLoader = this.mLoaderScope.get( p );
        if ( imageLoader == null ) {
            return null;
        }

        if ( imageLoader instanceof URLImageLoader ) {
            URLImageLoader urlImageLoader = (URLImageLoader) imageLoader;
            return urlImageLoader.queryExecutionImage( uri );
        }

        ExecutionImage image = imageLoader.queryExecutionImage( uri.getPath() );
        if ( image != null ) {
            return image;
        }

        CascadeComponent component = this.parent();
        if ( component instanceof URLImageLoader ) {
            image = ( (URLImageLoader) component ).queryExecutionImage( uri );
            if ( image != null ) {
                return image;
            }
        }
        return null;
    }

    @Override
    public ExecutionImage queryExecutionImage( String path ) {
        if ( path == null ) {
            return null;
        }
        ExecutionImage image = this.mLocalMappingImageLoader.queryExecutionImage( path );
        if ( image != null ) {
            return image;
        }

        CascadeComponent component = this.parent();
        if ( component instanceof ImageLoader ) {
            image = ( (ImageLoader) component ).queryExecutionImage( path );
            if ( image != null ) {
                return image;
            }
        }
        return null;
    }

    @Override
    public void registerLocalScopeExecutionImage( String dirPath, ExecutionImage image ) {
        this.mLocalMappingImageLoader.registerLocalScopeExecutionImage( dirPath, image );
    }
}
