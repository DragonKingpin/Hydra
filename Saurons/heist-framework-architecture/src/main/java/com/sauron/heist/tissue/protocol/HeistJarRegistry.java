package com.sauron.heist.tissue.protocol;

import java.util.List;

import com.sauron.heist.heistron.Heistum;
import com.sauron.heist.tissue.entity.HeistletDescriptor;
import com.sauron.heist.tissue.entity.LoadedHeistJar;

public interface HeistJarRegistry {
    void register( LoadedHeistJar loadedHeistJar, List<HeistletDescriptor> descriptors );

    List<LoadedHeistJar> loadedJars();

    List<HeistletDescriptor> descriptors();

    List<Class<? extends Heistum>> queryHeistlets( String szName );
}
