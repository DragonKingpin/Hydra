package com.walnut.odin.formation.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface RunEntry extends Pinenut {
    GUID getGuid();

    GUID getFormationGuid();

    String getStrategyType();

    String getRunStatus();

    long getPageSize();

    long getFrameSize();

    long getWindowSize();

    long getInflightLimit();

    long getTotalCount();

    long getSubmittedCount();

    long getCompletedCount();

    long getFailedCount();

    long getSuspendedCount();
}
