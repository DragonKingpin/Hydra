package com.sauron.heist.tissue.protocol;

import java.util.List;

import com.sauron.heist.tissue.entity.HeistletDescriptor;
import com.sauron.heist.tissue.entity.LoadedHeistJar;
import com.sauron.heist.tissue.system.HeistJarScanException;

public interface HeistJarScanner {
    List<HeistletDescriptor> scan( LoadedHeistJar loadedHeistJar ) throws HeistJarScanException;
}
