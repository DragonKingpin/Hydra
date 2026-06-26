package com.sauron.heist.tissue.entity;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class LoadedHeistJar implements AutoCloseable {
    private String         mszJarPath;
    private URLClassLoader mClassLoader;
    private HeistJarManifest mManifest;
    private List<String>   mScanRoots;

    public LoadedHeistJar() {
        this.mScanRoots = new ArrayList<>();
    }

    public String getJarPath() {
        return this.mszJarPath;
    }

    public void setJarPath( String szJarPath ) {
        this.mszJarPath = szJarPath;
    }

    public URLClassLoader getClassLoader() {
        return this.mClassLoader;
    }

    public void setClassLoader( URLClassLoader classLoader ) {
        this.mClassLoader = classLoader;
    }

    public HeistJarManifest getManifest() {
        return this.mManifest;
    }

    public void setManifest( HeistJarManifest manifest ) {
        this.mManifest = manifest;
    }

    public List<String> getScanRoots() {
        return this.mScanRoots;
    }

    public void setScanRoots( List<String> scanRoots ) {
        if ( scanRoots == null ) {
            this.mScanRoots = new ArrayList<>();
            return;
        }

        this.mScanRoots = new ArrayList<>( scanRoots );
    }

    @Override
    public void close() throws Exception {
        if ( this.mClassLoader != null ) {
            this.mClassLoader.close();
        }
    }
}
