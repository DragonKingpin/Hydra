package com.pinecone.hydra.storage.fsck;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;

public class StorageFsckReport implements Pinenut {
    protected String domain;
    protected LocalDateTime generatedAt;
    protected long scannedFiles;
    protected long scannedChunks;
    protected long scannedLocations;
    protected long scannedVolumes;
    protected long scannedFreeIntents;
    protected long totalIssues;
    protected long errorIssues;
    protected long warningIssues;
    protected List<StorageFsckIssue> issues = new ArrayList<>();

    public String getDomain() {
        return this.domain;
    }

    public void setDomain( String domain ) {
        this.domain = domain;
    }

    public LocalDateTime getGeneratedAt() {
        return this.generatedAt;
    }

    public void setGeneratedAt( LocalDateTime generatedAt ) {
        this.generatedAt = generatedAt;
    }

    public long getScannedFiles() {
        return this.scannedFiles;
    }

    public void setScannedFiles( long scannedFiles ) {
        this.scannedFiles = scannedFiles;
    }

    public long getScannedChunks() {
        return this.scannedChunks;
    }

    public void setScannedChunks( long scannedChunks ) {
        this.scannedChunks = scannedChunks;
    }

    public long getScannedLocations() {
        return this.scannedLocations;
    }

    public void setScannedLocations( long scannedLocations ) {
        this.scannedLocations = scannedLocations;
    }

    public long getScannedVolumes() {
        return this.scannedVolumes;
    }

    public void setScannedVolumes( long scannedVolumes ) {
        this.scannedVolumes = scannedVolumes;
    }

    public long getScannedFreeIntents() {
        return this.scannedFreeIntents;
    }

    public void setScannedFreeIntents( long scannedFreeIntents ) {
        this.scannedFreeIntents = scannedFreeIntents;
    }

    public long getTotalIssues() {
        return this.totalIssues;
    }

    public void setTotalIssues( long totalIssues ) {
        this.totalIssues = totalIssues;
    }

    public long getErrorIssues() {
        return this.errorIssues;
    }

    public void setErrorIssues( long errorIssues ) {
        this.errorIssues = errorIssues;
    }

    public long getWarningIssues() {
        return this.warningIssues;
    }

    public void setWarningIssues( long warningIssues ) {
        this.warningIssues = warningIssues;
    }

    public List<StorageFsckIssue> getIssues() {
        return this.issues;
    }

    public void setIssues( List<StorageFsckIssue> issues ) {
        this.issues = issues == null ? new ArrayList<>() : issues;
    }
}
