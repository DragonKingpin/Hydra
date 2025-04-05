package com.pinecone.hydra.atlas;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.unit.vgraph.ArchAtlasInstrument;
import com.pinecone.hydra.unit.vgraph.AtlasInstrument;
import com.pinecone.hydra.unit.vgraph.VectorGraphConfig;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphMasterManipulator;

public class UniformRuntimeAtlas extends ArchAtlasInstrument implements RuntimeAtlas {


    public UniformRuntimeAtlas(Processum superiorProcess, VectorGraphMasterManipulator masterManipulator, VectorGraphConfig vectorGraphConfig, AtlasInstrument parent, String name) {
        super(superiorProcess, masterManipulator, vectorGraphConfig, parent, name);
    }
}
