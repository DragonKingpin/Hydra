package com.sauron.heist.tissue.loading;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.sauron.heist.tissue.entity.HeistJarLoadRequest;
import com.sauron.heist.tissue.entity.HeistJarManifest;
import com.sauron.heist.tissue.entity.LoadedHeistJar;
import com.sauron.heist.tissue.protocol.HeistJarLoader;
import com.sauron.heist.tissue.protocol.HeistJarManifestResolver;
import com.sauron.heist.tissue.system.HeistJarLoadException;

public class GenericHeistJarLoader implements HeistJarLoader {
    protected ClassLoader              mSuperiorClassLoader;
    protected HeistJarManifestResolver mManifestResolver;

    public GenericHeistJarLoader( ClassLoader superiorClassLoader ) {
        this( superiorClassLoader, new GenericHeistJarManifestResolver() );
    }

    public GenericHeistJarLoader( ClassLoader superiorClassLoader, HeistJarManifestResolver manifestResolver ) {
        this.mSuperiorClassLoader = superiorClassLoader;
        this.mManifestResolver    = manifestResolver;
    }

    @Override
    public LoadedHeistJar load( HeistJarLoadRequest request ) throws HeistJarLoadException {
        this.verifyRequest( request );

        File jarFile = new File( request.getJarPath() );
        try {
            URL jarUrl = jarFile.toURI().toURL();
            URLClassLoader classLoader = new URLClassLoader( new URL[] { jarUrl }, this.mSuperiorClassLoader );
            HeistJarManifest manifest = this.resolveManifest( request );
            List<String> scanRoots = this.mergeScanRoots( request.getScanRoots(), manifest );

            if ( scanRoots.isEmpty() ) {
                throw new HeistJarLoadException( "HeistJar scan roots can not be empty: " + request.getJarPath() );
            }

            LoadedHeistJar loadedHeistJar = new LoadedHeistJar();
            loadedHeistJar.setJarPath( request.getJarPath() );
            loadedHeistJar.setClassLoader( classLoader );
            loadedHeistJar.setManifest( manifest );
            loadedHeistJar.setScanRoots( scanRoots );
            return loadedHeistJar;
        }
        catch ( MalformedURLException e ) {
            throw new HeistJarLoadException( "Invalid HeistJar path: " + request.getJarPath(), e );
        }
    }

    protected void verifyRequest( HeistJarLoadRequest request ) throws HeistJarLoadException {
        if ( request == null ) {
            throw new HeistJarLoadException( "HeistJar load request can not be null." );
        }

        if ( request.getJarPath() == null || request.getJarPath().trim().isEmpty() ) {
            throw new HeistJarLoadException( "HeistJar path can not be empty." );
        }

        File jarFile = new File( request.getJarPath() );
        if ( !jarFile.exists() ) {
            throw new HeistJarLoadException( "HeistJar file does not exist: " + request.getJarPath() );
        }

        if ( !jarFile.isFile() ) {
            throw new HeistJarLoadException( "HeistJar path is not a file: " + request.getJarPath() );
        }
    }

    protected HeistJarManifest resolveManifest( HeistJarLoadRequest request ) throws HeistJarLoadException {
        if ( !request.isUseManifest() ) {
            return null;
        }

        boolean bContainsManifest = this.mManifestResolver.containsManifest( request.getJarPath() );
        if ( !bContainsManifest ) {
            if ( request.isRequireManifest() ) {
                throw new HeistJarLoadException( "HeistJar manifest is required: " + request.getJarPath() );
            }
            return null;
        }

        return this.mManifestResolver.resolveManifest( request.getJarPath() );
    }

    protected List<String> mergeScanRoots( List<String> requestScanRoots, HeistJarManifest manifest ) {
        Set<String> scanRoots = new LinkedHashSet<>();
        this.addScanRoots( scanRoots, requestScanRoots );
        if ( manifest != null ) {
            this.addScanRoots( scanRoots, manifest.getScanRoots() );
        }
        return new ArrayList<>( scanRoots );
    }

    protected void addScanRoots( Set<String> scanRoots, List<String> candidates ) {
        if ( candidates == null ) {
            return;
        }

        for ( String szScanRoot : candidates ) {
            if ( szScanRoot != null && !szScanRoot.trim().isEmpty() ) {
                scanRoots.add( szScanRoot.trim() );
            }
        }
    }
}
