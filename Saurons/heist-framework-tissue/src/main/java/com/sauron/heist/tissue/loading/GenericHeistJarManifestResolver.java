package com.sauron.heist.tissue.loading;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.pinecone.framework.util.json.JSONArray;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;
import com.sauron.heist.tissue.entity.HeistJarManifest;
import com.sauron.heist.tissue.protocol.HeistJarManifestResolver;
import com.sauron.heist.tissue.system.HeistJarLoadException;

public class GenericHeistJarManifestResolver implements HeistJarManifestResolver {
    public static final String SZ_DEFAULT_MANIFEST_PATH = "META-INF/heist-tissue.json";

    protected String mszManifestPath;

    public GenericHeistJarManifestResolver() {
        this( GenericHeistJarManifestResolver.SZ_DEFAULT_MANIFEST_PATH );
    }

    public GenericHeistJarManifestResolver( String szManifestPath ) {
        this.mszManifestPath = szManifestPath;
    }

    @Override
    public boolean containsManifest( String szJarPath ) throws HeistJarLoadException {
        try ( JarFile jarFile = new JarFile( szJarPath ) ) {
            return jarFile.getJarEntry( this.mszManifestPath ) != null;
        }
        catch ( IOException e ) {
            throw new HeistJarLoadException( "Failed to inspect HeistJar manifest: " + szJarPath, e );
        }
    }

    @Override
    public HeistJarManifest resolveManifest( String szJarPath ) throws HeistJarLoadException {
        try ( JarFile jarFile = new JarFile( szJarPath ) ) {
            JarEntry jarEntry = jarFile.getJarEntry( this.mszManifestPath );
            if ( jarEntry == null ) {
                return null;
            }

            try ( InputStream inputStream = jarFile.getInputStream( jarEntry ) ) {
                byte[] bytes = inputStream.readAllBytes();
                return this.parseCurrentJarManifest( new String( bytes, StandardCharsets.UTF_8 ) );
            }
        }
        catch ( IOException e ) {
            throw new HeistJarLoadException( "Failed to read HeistJar manifest: " + szJarPath, e );
        }
    }

    protected HeistJarManifest parseCurrentJarManifest( String szManifestRaw ) throws HeistJarLoadException {
        try {
            JSONObject manifestObject = new JSONMaptron().jsonDecode( szManifestRaw );
            HeistJarManifest manifest = new HeistJarManifest();
            manifest.setFormatVersion( manifestObject.optString( "formatVersion" ) );
            manifest.setTissueType( manifestObject.optString( "tissueType" ) );
            manifest.setScanRoots( this.parseScanRoots( manifestObject.optJSONArray( "scanRoots" ) ) );
            return manifest;
        }
        catch ( Exception e ) {
            throw new HeistJarLoadException( "Failed to parse HeistJar manifest.", e );
        }
    }

    protected List<String> parseScanRoots( JSONArray scanRootsArray ) {
        List<String> scanRoots = new ArrayList<>();
        if ( scanRootsArray == null ) {
            return scanRoots;
        }

        for ( Object item : scanRootsArray ) {
            if ( item != null && !item.toString().trim().isEmpty() ) {
                scanRoots.add( item.toString() );
            }
        }
        return scanRoots;
    }
}
