package com.sauron.heist.tissue.scanning;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.sauron.heist.heistron.Heistum;
import com.sauron.heist.heistron.orchestration.Heistlet;
import com.sauron.heist.tissue.entity.HeistletDescriptor;
import com.sauron.heist.tissue.entity.LoadedHeistJar;
import com.sauron.heist.tissue.protocol.HeistJarScanner;
import com.sauron.heist.tissue.system.HeistJarScanException;

public class GenericHeistJarScanner implements HeistJarScanner {
    @Override
    @SuppressWarnings( "unchecked" )
    public List<HeistletDescriptor> scan( LoadedHeistJar loadedHeistJar ) throws HeistJarScanException {
        try ( JarFile jarFile = new JarFile( loadedHeistJar.getJarPath() ) ) {
            List<HeistletDescriptor> descriptors = new ArrayList<>();
            Enumeration<JarEntry> entries = jarFile.entries();

            while ( entries.hasMoreElements() ) {
                JarEntry entry = entries.nextElement();
                String szClassName = this.resolveClassName( entry );
                if ( szClassName == null || !this.isInScanRoots( szClassName, loadedHeistJar.getScanRoots() ) ) {
                    continue;
                }

                this.tryAddHeistletDescriptor( descriptors, loadedHeistJar, szClassName );
            }
            return descriptors;
        }
        catch ( Exception e ) {
            throw new HeistJarScanException( "Failed to scan HeistJar: " + loadedHeistJar.getJarPath(), e );
        }
    }

    protected String resolveClassName( JarEntry entry ) {
        if ( entry.isDirectory() ) {
            return null;
        }

        String szEntryName = entry.getName();
        if ( !szEntryName.endsWith( ".class" ) || szEntryName.contains( "$" ) ) {
            return null;
        }

        String szClassName = szEntryName.substring( 0, szEntryName.length() - ".class".length() );
        return szClassName.replace( '/', '.' );
    }

    protected boolean isInScanRoots( String szClassName, List<String> scanRoots ) {
        for ( String szScanRoot : scanRoots ) {
            if ( szClassName.equals( szScanRoot ) || szClassName.startsWith( szScanRoot + "." ) ) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings( "unchecked" )
    protected void tryAddHeistletDescriptor( List<HeistletDescriptor> descriptors, LoadedHeistJar loadedHeistJar, String szClassName ) {
        try {
            Class<?> clazz = loadedHeistJar.getClassLoader().loadClass( szClassName );
            if ( !Heistum.class.isAssignableFrom( clazz ) ) {
                return;
            }

            Heistlet heistlet = clazz.getAnnotation( Heistlet.class );
            if ( heistlet == null ) {
                return;
            }

            HeistletDescriptor descriptor = new HeistletDescriptor();
            descriptor.setName( heistlet.value() );
            descriptor.setClassName( clazz.getName() );
            descriptor.setHeistletClass( (Class<? extends Heistum>) clazz );
            descriptor.setLoadedHeistJar( loadedHeistJar );
            descriptors.add( descriptor );
        }
        catch ( Throwable ignored ) {
        }
    }
}
