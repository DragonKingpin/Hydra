package com.pinecone.hydra.unit.vgraph.layer.operator;

import com.pinecone.hydra.unit.imperium.operator.OperatorFactory;
import com.pinecone.hydra.unit.vgraph.layer.Layer;
import com.pinecone.hydra.unit.vgraph.layer.LayerInstrument;
import com.pinecone.hydra.unit.vgraph.layer.LayerNamespace;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerMasterManipulator;

public interface LayerComponentOperatorFactory extends OperatorFactory {
    String DefaultLayer         = Layer.class.getSimpleName();

    String DefaultNamespace     = LayerNamespace.class.getSimpleName();

    void registerMetaType( Class<?> clazz, String metaType );

    void registerMetaType( String classFullName, String metaType );

    String getMetaType( String classFullName );

    LayerComponentOperator getOperator(String typeName );

    LayerInstrument getLayerManager();

    LayerMasterManipulator getMasterManipulator();
}
