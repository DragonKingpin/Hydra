package com.pinecone.hydra.storage.repair;

import com.pinecone.framework.system.prototype.Pinenut;

public class StorageRepairPolicy implements Pinenut {
    protected Boolean refreshVolumeUsage;
    protected Boolean moveOrphanObjects;
    protected Boolean purgeOrphanFat;

    public Boolean getRefreshVolumeUsage() {
        return this.refreshVolumeUsage;
    }

    public void setRefreshVolumeUsage( Boolean refreshVolumeUsage ) {
        this.refreshVolumeUsage = refreshVolumeUsage;
    }

    public Boolean getMoveOrphanObjects() {
        return this.moveOrphanObjects;
    }

    public void setMoveOrphanObjects( Boolean moveOrphanObjects ) {
        this.moveOrphanObjects = moveOrphanObjects;
    }

    public Boolean getPurgeOrphanFat() {
        return this.purgeOrphanFat;
    }

    public void setPurgeOrphanFat( Boolean purgeOrphanFat ) {
        this.purgeOrphanFat = purgeOrphanFat;
    }
}
