package com.sauron.heist.tissue.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Manifest describes current jar capabilities only.
 * Loader path and dependency redirection are host-side concerns.
 */
public class HeistJarManifest {
    private String       mszFormatVersion;
    private String       mszTissueType;
    private List<String> mScanRoots;

    public HeistJarManifest() {
        this.mScanRoots = new ArrayList<>();
    }

    public String getFormatVersion() {
        return this.mszFormatVersion;
    }

    public void setFormatVersion( String szFormatVersion ) {
        this.mszFormatVersion = szFormatVersion;
    }

    public String getTissueType() {
        return this.mszTissueType;
    }

    public void setTissueType( String szTissueType ) {
        this.mszTissueType = szTissueType;
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
}
