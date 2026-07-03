package com.sauron.heist.tissue.entity;

import java.util.ArrayList;
import java.util.List;

public class HeistJarLoadRequest {
    // Host-side artifact path. Manifest describes current jar capabilities and must never override this path.
    private String       mszJarPath;
    private List<String> mScanRoots;
    private boolean      mbUseManifest;
    private boolean      mbRequireManifest;

    public HeistJarLoadRequest() {
        this.mScanRoots        = new ArrayList<>();
        this.mbUseManifest     = true;
        this.mbRequireManifest = false;
    }

    public String getJarPath() {
        return this.mszJarPath;
    }

    public void setJarPath( String szJarPath ) {
        this.mszJarPath = szJarPath;
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

    public void addScanRoot( String szScanRoot ) {
        if ( szScanRoot == null || szScanRoot.trim().isEmpty() ) {
            return;
        }

        this.mScanRoots.add( szScanRoot );
    }

    public boolean isUseManifest() {
        return this.mbUseManifest;
    }

    public void setUseManifest( boolean bUseManifest ) {
        this.mbUseManifest = bUseManifest;
    }

    public boolean isRequireManifest() {
        return this.mbRequireManifest;
    }

    public void setRequireManifest( boolean bRequireManifest ) {
        this.mbRequireManifest = bRequireManifest;
    }
}
