package com.pinecone.hydra.storage.volume.operator;

import com.pinecone.hydra.storage.volume.VolumeTree;
import com.pinecone.hydra.storage.volume.entity.SimpleVolume;
import com.pinecone.hydra.storage.volume.entity.local.LocalSimpleVolume;
import com.pinecone.hydra.storage.volume.source.VolumeMasterManipulator;
import com.pinecone.hydra.unit.udtt.operator.OperatorFactory;
import com.pinecone.hydra.unit.udtt.operator.TreeNodeOperator;

public interface VolumeOperatorFactory extends OperatorFactory {
    String DefaultSimpleVolume         = LocalSimpleVolume.class.getSimpleName();

    void register( String typeName, TreeNodeOperator functionalNodeOperation );

    void registerMetaType( Class<?> clazz, String metaType );

    void registerMetaType( String classFullName, String metaType );

    String getMetaType( String classFullName );

    VolumeOperator getOperator(String typeName );

    VolumeTree getVolumeTree();

    VolumeMasterManipulator getMasterManipulator();
}
