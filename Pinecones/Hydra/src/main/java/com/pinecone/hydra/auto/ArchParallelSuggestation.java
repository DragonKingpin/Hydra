package com.pinecone.hydra.auto;

import com.pinecone.framework.system.executum.Processum;

public abstract class ArchParallelSuggestation extends ArchParallelInstructation implements ParallelSuggestation {
    protected IgnoredReason mIgnoredReason;

    protected ArchParallelSuggestation( Processum parent ){
        super( parent );
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