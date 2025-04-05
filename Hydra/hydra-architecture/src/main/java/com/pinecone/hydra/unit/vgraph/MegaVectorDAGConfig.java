package com.pinecone.hydra.unit.vgraph;

public class MegaVectorDAGConfig implements VectorGraphConfig{


    @Override
    public String getPathNameSeparator() {
        return null;
    }

    @Override
    public String getFullNameSeparator() {
        return null;
    }

    @Override
    public String getPathNameSepRegex() {
        return null;
    }

    @Override
    public String getFullNameSepRegex() {
        return null;
    }

    @Override
    public int getShortPathLength() {
        return 0;
    }
}
