package com.pinecone.hydra.auto;

public abstract class ArchSuggestation extends ArchInstructation implements Suggestation {
    protected IgnoredReason mIgnoredReason;

    protected ArchSuggestation() {
        super();
    }

    @Override
    public IgnoredReason getIgnoredReason() {
        return this.mIgnoredReason;
    }

    @Override
    public void setIgnoredReason( IgnoredReason ignoredReason ) {
        this.mIgnoredReason = ignoredReason;
    }

    @Override
    public abstract void execute();
}
