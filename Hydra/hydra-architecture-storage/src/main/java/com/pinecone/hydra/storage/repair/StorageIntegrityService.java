package com.pinecone.hydra.storage.repair;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.fsck.StorageFsckReport;

public interface StorageIntegrityService extends Pinenut {
    StorageFsckReport uofsFsck();

    StorageFsckReport volumeFsck();

    StorageRepairReport dryRun( StorageRepairPolicy policy );

    StorageRepairReport apply( StorageRepairPolicy policy );
}
