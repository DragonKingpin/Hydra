package com.walnut.odin.formation.plan;

import java.util.Collection;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.chunk.Page;

public interface FormationPage extends FormationProduct, Page {
    GUID getRunGuid();

    GUID getFormationGuid();

    long pageNo();

    long getPageNo();

    String getPageStatus();

    long getFrameStart();

    long getFrameEnd();

    long getTotalCount();

    long getCompletedCount();

    Collection<FormationFrame> frames();
}
