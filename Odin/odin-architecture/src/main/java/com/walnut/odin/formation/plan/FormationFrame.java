package com.walnut.odin.formation.plan;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.chunk.Frame;

public interface FormationFrame extends FormationProduct, Frame {
    GUID formationGuid();

    GUID groupGuid();

    GUID taskGuid();

    long pageNo();

    long frameNo();
}
