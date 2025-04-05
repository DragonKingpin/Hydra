package com.pinecone.hydra.unit.vgraph;

import com.pinecone.framework.system.prototype.Pinenut;

public interface VectorGraphConfig extends Pinenut {
    String getPathNameSeparator();

    String getFullNameSeparator();

    String getPathNameSepRegex();

    String getFullNameSepRegex();

    int getShortPathLength();
}
