package com.pinecone.hydra.unit.vgraph.layer;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.system.identifier.KOPathResolver;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.system.ko.kom.ArchKOMTree;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerMasterManipulator;
import com.pinecone.ulf.util.guid.GenericGuidAllocator;

public class VLayerManager extends ArchKOMTree implements LayerManager {
    protected LayerMasterManipulator mLayerMasterManipulator;

    public VLayerManager(Processum superiorProcess, KOIMasterManipulator masterManipulator, LayerManager parent, String name, LayerConfig config ) {
        super( superiorProcess, masterManipulator, config, parent, name );
        this.mLayerMasterManipulator = (LayerMasterManipulator) masterManipulator;
        this.pathResolver = new KOPathResolver( this.kernelObjectConfig );
        this.guidAllocator = new GenericGuidAllocator();

//        this.pathSelector = new SimplePathSelector(
//                this.pathResolver, this.imperialTree, this.
//        )
    }

    public VLayerManager(Processum superiorProcess, KOIMasterManipulator masterManipulator, LayerConfig config ) {
        this( superiorProcess, masterManipulator, null, LayerConfig.class.getSimpleName(), config );
    }

    public VLayerManager(KOIMappingDriver driver, LayerManager parent, String name, LayerConfig config ) {
        this(driver.getSuperiorProcess(), driver.getMasterManipulator(), parent, name, config);
    }

    public VLayerManager(KOIMappingDriver driver, LayerConfig config ) {
        this(driver.getSuperiorProcess(), driver.getMasterManipulator(), config);
    }

    @Override
    public Object queryEntityHandleByNS(String path, String szBadSep, String szTargetSep) {
        return null;
    }
}
