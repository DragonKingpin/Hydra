package com.walnut.odin.formation.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface GroupEntry extends Pinenut {
    GUID getGuid();

    String getIdentifier();

    String getTitle();

    GUID getProjectGuid();

    String getStrategyType();

    long getPageSize();

    long getFrameSize();

    long getWindowSize();

    long getInflightLimit();

    boolean isEnable();

    String getDescription();
}
