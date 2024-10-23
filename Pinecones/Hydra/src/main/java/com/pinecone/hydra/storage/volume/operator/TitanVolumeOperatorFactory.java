package com.pinecone.hydra.storage.volume.operator;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.GenericFileNode;
import com.pinecone.hydra.storage.file.entity.GenericFolder;
import com.pinecone.hydra.storage.file.operator.FileSystemOperator;
import com.pinecone.hydra.storage.file.operator.GenericFileOperator;
import com.pinecone.hydra.storage.file.operator.GenericFolderOperator;
import com.pinecone.hydra.storage.file.source.FileMasterManipulator;
import com.pinecone.hydra.storage.volume.VolumeConfig;
import com.pinecone.hydra.storage.volume.VolumeTree;
import com.pinecone.hydra.storage.volume.source.VolumeMasterManipulator;
import com.pinecone.hydra.unit.udtt.operator.TreeNodeOperator;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class TitanVolumeOperatorFactory implements VolumeOperatorFactory{
    protected  VolumeMasterManipulator          volumeMasterManipulator;
    protected  VolumeTree                       volumeTree;
    protected Map<String, TreeNodeOperator> registerer = new HashMap<>();
    protected Map<String, String >             metaTypeMap = new TreeMap<>();

    protected void registerDefaultMetaType( Class<?> genericType ) {
        this.metaTypeMap.put( genericType.getName(), genericType.getSimpleName().replace(VolumeConfig.filePrefix,"") );
    }

    protected void registerDefaultMetaTypes() {
        this.registerDefaultMetaType( GenericFolder.class );
        this.registerDefaultMetaType( GenericFileNode.class );
    }

    public TitanVolumeOperatorFactory(VolumeTree volumeTree, VolumeMasterManipulator volumeMasterManipulator ){
        this.volumeTree = volumeTree;
        this.volumeMasterManipulator = volumeMasterManipulator;

        this.registerer.put(
                DefaultSimpleVolume,
                new SimpleVolumeOperator( this )
        );


        this.registerDefaultMetaTypes();
    }
    @Override
    public void register( String typeName, TreeNodeOperator functionalNodeOperation ) {
        this.registerer.put( typeName, functionalNodeOperation );
    }

    @Override
    public void registerMetaType( Class<?> clazz, String metaType ){
        this.registerMetaType( clazz.getName(), metaType );
    }

    @Override
    public void registerMetaType( String classFullName, String metaType ){
        this.metaTypeMap.put( classFullName, metaType );
    }

    @Override
    public String getMetaType( String classFullName ) {
        return this.metaTypeMap.get( classFullName );
    }

    @Override
    public VolumeOperator getOperator(String typeName ) {
        //Debug.trace( this.registerer.toString() );
        //Debug.trace( typeName );
        return (VolumeOperator) this.registerer.get( typeName );
    }

    @Override
    public VolumeTree getVolumeTree() {
        return this.volumeTree;
    }

    @Override
    public VolumeMasterManipulator getMasterManipulator() {
        return this.volumeMasterManipulator;
    }
}
