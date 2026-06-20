package com.pinecone.hydra.storage.repair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.fsck.StorageFsckReport;

public class StorageRepairReport implements Pinenut {
    protected boolean dryRun;
    protected LocalDateTime generatedAt;
    protected List<StorageFsckReport> reports = new ArrayList<>();
    protected List<StorageRepairAction> actions = new ArrayList<>();

    public boolean isDryRun() {
        return this.dryRun;
    }

    public void setDryRun( boolean dryRun ) {
        this.dryRun = dryRun;
    }

    public LocalDateTime getGeneratedAt() {
        return this.generatedAt;
    }

    public void setGeneratedAt( LocalDateTime generatedAt ) {
        this.generatedAt = generatedAt;
    }

    public List<StorageFsckReport> getReports() {
        return this.reports;
    }

    public void setReports( List<StorageFsckReport> reports ) {
        this.reports = reports == null ? new ArrayList<>() : reports;
    }

    public List<StorageRepairAction> getActions() {
        return this.actions;
    }

    public void setActions( List<StorageRepairAction> actions ) {
        this.actions = actions == null ? new ArrayList<>() : actions;
    }
}
