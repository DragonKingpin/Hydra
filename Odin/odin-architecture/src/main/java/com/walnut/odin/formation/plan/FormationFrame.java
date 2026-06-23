package com.walnut.odin.formation.plan;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.chunk.Frame;

public interface FormationFrame extends FormationProduct, Frame {
    GUID getGuid();

    GUID getRunGuid();

    GUID formationGuid();

    GUID getFormationGuid();

    GUID groupGuid();

    GUID getGroupGuid();

    GUID taskGuid();

    GUID getTaskGuid();

    String getTaskName();

    long pageNo();

    long getPageNo();

    long frameNo();

    long getFrameNo();

    String getQueueType();

    String getFrameStatus();

    short getPriority();

    GUID getInstanceGuid();

    java.time.LocalDateTime getSubmitTime();

    java.time.LocalDateTime getFinishTime();

    int getAttemptCount();

    String getErrorCause();
}
