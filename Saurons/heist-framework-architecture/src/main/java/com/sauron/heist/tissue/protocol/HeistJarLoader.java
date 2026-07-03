package com.sauron.heist.tissue.protocol;

import com.sauron.heist.tissue.entity.HeistJarLoadRequest;
import com.sauron.heist.tissue.entity.LoadedHeistJar;
import com.sauron.heist.tissue.system.HeistJarLoadException;

public interface HeistJarLoader {
    LoadedHeistJar load( HeistJarLoadRequest request ) throws HeistJarLoadException;
}
