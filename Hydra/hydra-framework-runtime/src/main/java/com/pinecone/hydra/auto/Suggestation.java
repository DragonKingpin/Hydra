package com.pinecone.hydra.auto;

public interface Suggestation extends Instructation {
    @Override
    void execute() ; // No exceptions

    default boolean hasIgnored() {
        return this.getIgnoredReason() != null;
    }

    default boolean hasAccepted() {
        return this.getIgnoredReason() == null;
    }

    IgnoredReason getIgnoredReason();

    void setIgnoredReason( IgnoredReason ignoredReason );

    Exception lastException();

    void setLastException( Exception e );
}
