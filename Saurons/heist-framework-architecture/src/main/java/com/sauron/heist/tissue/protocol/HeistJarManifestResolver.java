package com.sauron.heist.tissue.protocol;

import com.sauron.heist.tissue.entity.HeistJarManifest;
import com.sauron.heist.tissue.system.HeistJarLoadException;

public interface HeistJarManifestResolver {
    boolean containsManifest( String szJarPath ) throws HeistJarLoadException;

    HeistJarManifest resolveManifest( String szJarPath ) throws HeistJarLoadException;
}
