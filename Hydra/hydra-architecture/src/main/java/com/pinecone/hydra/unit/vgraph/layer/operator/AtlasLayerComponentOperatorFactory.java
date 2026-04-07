package com.pinecone.hydra.unit.vgraph.layer.operator;

import com.pinecone.hydra.unit.imperium.operator.TreeNodeOperator;
import com.pinecone.hydra.unit.vgraph.layer.AtlasLayer;
import com.pinecone.hydra.unit.vgraph.layer.AtlasLayerNamespace;
import com.pinecone.hydra.unit.vgraph.layer.LayerInstrument;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerMasterManipulator;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class AtlasLayerComponentOperatorFactory implements LayerComponentOperatorFactory {
    protected LayerMasterManipulator                mLayerMasterManipulator;

    protected LayerInstrument mLayerInstrument;

    protected Map<String, TreeNodeOperator>   registerer = new HashMap<>();

    protected Map<String, String>                   metaTypeMap = new TreeMap<>();

    protected void registerDefaultMetaType( Class<?> genericType ) {
        this.metaTypeMap.put( genericType.getName(), genericType.getSimpleName().replace(
                "Atlas",""
        ));
    }

    protected void registerDefaultMetaTypes() {
        this.registerDefaultMetaType( AtlasLayer.class );
        this.registerDefaultMetaType( AtlasLayerNamespace.class );
    }

    public AtlasLayerComponentOperatorFactory(LayerInstrument layerInstrument, LayerMasterManipulator layerMasterManipulator ) {
        this.mLayerInstrument = layerInstrument;
        this.mLayerMasterManipulator = layerMasterManipulator;

        this.registerer.put(
                DefaultLayer,
                new LayerOperator( this )
        );
        this.registerer.put(
                DefaultNamespace,
                new LayerNamespaceOperator( this )
        );
    }



    @Override
    public void register(String typeName, TreeNodeOperator functionalNodeOperation) {
        this.registerer.put( typeName, functionalNodeOperation );
    }

    @Override
    public void registerMetaType(Class<?> clazz, String metaType) {
        this.registerMetaType( clazz.getName(), metaType );
    }

    @Override
    public void registerMetaType(String classFullName, String metaType) {
        this.metaTypeMap.put( classFullName, metaType );
    }

    @Override
    public String getMetaType(String classFullName) {
        return this.metaTypeMap.get( classFullName );
    }

    @Override
    public LayerComponentOperator getOperator(String typeName) {
        return (LayerComponentOperator) this.registerer.get( typeName );
    }

    @Override
    public LayerInstrument getLayerManager() {
        return this.mLayerInstrument;
    }

    @Override
    public LayerMasterManipulator getMasterManipulator() {
        return this.mLayerMasterManipulator;
    }
}
