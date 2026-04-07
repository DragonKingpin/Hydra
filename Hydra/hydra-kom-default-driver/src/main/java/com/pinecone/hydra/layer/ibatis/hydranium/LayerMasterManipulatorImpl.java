package com.pinecone.hydra.layer.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.layer.ibatis.LayerHandleMapper;
import com.pinecone.hydra.layer.ibatis.LayerMapper;
import com.pinecone.hydra.layer.ibatis.NamespaceMapper;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerHandleManipulator;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerManipulator;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerMasterManipulator;
import com.pinecone.hydra.unit.vgraph.layer.source.NamespaceManipulator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class LayerMasterManipulatorImpl implements LayerMasterManipulator {
    @Resource
    @Structure( type = LayerMasterTreeManipulatorImpl.class )
    KOISkeletonMasterManipulator skeletonMasterManipulator;

    @Resource
    @Structure( type = NamespaceMapper.class )
    NamespaceManipulator namespaceManipulator;

    @Resource
    @Structure( type = LayerMapper.class )
    LayerManipulator layerManipulator;

    @Resource
    @Structure( type = LayerHandleMapper.class )
    LayerHandleManipulator layerHandleManipulator;

    public LayerMasterManipulatorImpl() {

    }

    public LayerMasterManipulatorImpl(KOIMappingDriver driver ) {
        driver.autoConstruct( LayerMasterManipulatorImpl.class, Map.of(), this );
        this.skeletonMasterManipulator = new LayerMasterTreeManipulatorImpl( driver );
    }

    @Override
    public KOISkeletonMasterManipulator getSkeletonMasterManipulator() {
        return this.skeletonMasterManipulator;
    }

    @Override
    public LayerManipulator getLayerManipulator() {
        return this.layerManipulator;
    }

    @Override
    public NamespaceManipulator getNamespaceManipulator() {
        return this.namespaceManipulator;
    }

    @Override
    public LayerHandleManipulator getLayerHandleManipulator() {
        return this.layerHandleManipulator;
    }
}
