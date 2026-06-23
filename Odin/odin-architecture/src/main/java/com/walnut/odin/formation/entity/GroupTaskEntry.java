package com.walnut.odin.formation.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface GroupTaskEntry extends Pinenut {
    GUID getGuid();

    GUID getFormationGuid();

    GUID getTaskGuid();

    String getTaskName();

    String getScheduleType();

    long getSequenceNo();

    short getPriority();

    boolean isEnable();
}
