package com.peanut.titan.system;

import com.peanut.titan.storage.TitanStorageRegiment;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.transfer.service.UofsTransferService;
import com.pinecone.hydra.storage.lifecycle.service.StorageLifecycleService;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.hydra.system.imperium.FacilityClass;
import com.pinecone.hydra.system.imperium.FacilitySynergicSystem;

public interface TitanSubsystem extends FacilitySynergicSystem {

    TitanStorageRegiment storageRegiment();

    VolumeManager volumeManager();

    KOMFileSystem fileSystem();

    StorageLifecycleService storageLifecycleService();

    UofsTransferService uofsTransferService();

    @Override
    default FacilityClass facilityClass() {
        return FacilityClass.Storage;
    }

    @Override
    default FacilityClass[] ownedClass() {
        return new FacilityClass[] { FacilityClass.Storage };
    }

}
