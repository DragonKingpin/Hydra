package com.sauron.heist.tissue.registry;

import java.util.ArrayList;
import java.util.List;

import com.sauron.heist.heistron.Heistum;
import com.sauron.heist.tissue.entity.HeistletDescriptor;
import com.sauron.heist.tissue.entity.LoadedHeistJar;
import com.sauron.heist.tissue.protocol.HeistJarRegistry;

public class ResidentHeistJarRegistry implements HeistJarRegistry {
    protected List<LoadedHeistJar>     mLoadedJars;
    protected List<HeistletDescriptor> mDescriptors;

    public ResidentHeistJarRegistry() {
        this.mLoadedJars  = new ArrayList<>();
        this.mDescriptors = new ArrayList<>();
    }

    @Override
    public synchronized void register( LoadedHeistJar loadedHeistJar, List<HeistletDescriptor> descriptors ) {
        this.mLoadedJars.add( loadedHeistJar );
        if ( descriptors != null ) {
            this.mDescriptors.addAll( descriptors );
        }
    }

    @Override
    public synchronized List<LoadedHeistJar> loadedJars() {
        return new ArrayList<>( this.mLoadedJars );
    }

    @Override
    public synchronized List<HeistletDescriptor> descriptors() {
        return new ArrayList<>( this.mDescriptors );
    }

    @Override
    public synchronized List<Class<? extends Heistum>> queryHeistlets( String szName ) {
        List<Class<? extends Heistum>> classes = new ArrayList<>();
        for ( HeistletDescriptor descriptor : this.mDescriptors ) {
            if ( descriptor.getName() != null && descriptor.getName().equals( szName ) ) {
                classes.add( descriptor.getHeistletClass() );
            }
        }
        return classes;
    }
}
