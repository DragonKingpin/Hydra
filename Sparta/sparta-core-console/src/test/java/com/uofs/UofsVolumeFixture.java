package com.uofs;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

final class UofsVolumeFixture {
    final String caseName;
    final GUID volumeGuid;
    final UniformVolumeManager volumeManager;
    final List<File> physicalFiles;
    final File objectRoot;

    UofsVolumeFixture( String caseName, GUID volumeGuid, UniformVolumeManager volumeManager, File objectRoot ) {
        this.caseName = caseName;
        this.volumeGuid = volumeGuid;
        this.volumeManager = volumeManager;
        this.objectRoot = objectRoot;
        this.physicalFiles = new ArrayList<>();
    }

    void addPhysicalFile( File file ) {
        this.physicalFiles.add( file );
    }
}
