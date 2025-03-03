package com.pinecone.hydra.storage.file.builder;

import com.pinecone.framework.unit.BitSet64;
import com.pinecone.hydra.storage.file.FileSystemConfig;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.UniformObjectFileSystem;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;

public class ComponentUOFSBuilder implements UOFSBuilder {
    public static long DEFAULT_GENERATE_FEATURE = 0L;

    protected UOFSComponentor[]  mComponentorIndex = new UOFSComponentor[ Feature.featuresSize() ];

    protected KOIMappingDriver   mKOIMappingDriver;

    protected long               mFeatureValues = DEFAULT_GENERATE_FEATURE;

    protected FileSystemConfig   mFileSystemConfig;

    public ComponentUOFSBuilder ( KOIMappingDriver driver, FileSystemConfig config ) {
        this.mKOIMappingDriver = driver;
        this.mFileSystemConfig = config;
    }




    @Override
    public UOFSBuilder registerComponentor( UOFSComponentor componentor ) {
        int i = componentor.getFeature().ordinal();
        this.mComponentorIndex[ i ] = componentor;
        this.mFeatureValues = BitSet64.setBit( this.mFeatureValues, i );
        return this;
    }

    @Override
    public KOMFileSystem buildByRegistered() {
        return this.build( this.mFeatureValues );
    }

    @Override
    public KOMFileSystem build( Feature... features ) {
        long featureValues = DEFAULT_GENERATE_FEATURE;

        for ( int i = 0; i < features.length; ++i ) {
            Feature feature = features[ i ];
            featureValues = Feature.config( featureValues, feature, true );
        }

        return this.build( featureValues );
    }

    @Override
    public KOMFileSystem build( long featureValues ) {
        KOMFileSystem fs = new UniformObjectFileSystem( this.mKOIMappingDriver, this.mFileSystemConfig );

        for ( int i = 0; i < Feature.featuresSize(); ++i ) {
            if ( ( featureValues & (1L << i) ) != 0 ) {
                this.mComponentorIndex[ i ].apply( fs );
            }
        }

        return fs;
    }

}
