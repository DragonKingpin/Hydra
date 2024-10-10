package com.pinecone.hydra.file.operator;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.file.KOMFileSystem;
import com.pinecone.hydra.file.entity.GenericFileNode;
import com.pinecone.hydra.file.entity.GenericFolder;
import com.pinecone.hydra.file.source.FileMasterManipulator;
import com.pinecone.hydra.registry.KOMRegistry;
import com.pinecone.hydra.registry.entity.GenericNamespace;
import com.pinecone.hydra.registry.entity.GenericProperties;
import com.pinecone.hydra.registry.entity.GenericTextFile;
import com.pinecone.hydra.registry.operator.NamespaceNodeOperator;
import com.pinecone.hydra.registry.operator.PropertiesOperator;
import com.pinecone.hydra.registry.operator.RegistryNodeOperator;
import com.pinecone.hydra.registry.operator.RegistryOperatorFactory;
import com.pinecone.hydra.registry.operator.TextValueNodeOperator;
import com.pinecone.hydra.registry.source.RegistryMasterManipulator;
import com.pinecone.hydra.unit.udtt.operator.TreeNodeOperator;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class GenericFileSystemOperatorFactory implements FileSystemOperatorFactory{
    protected FileMasterManipulator         fileMasterManipulator;
    protected KOMFileSystem                 fileSystem;

    protected Map<String, TreeNodeOperator> registerer = new HashMap<>();

    protected Map<String, String >             metaTypeMap = new TreeMap<>();

    protected void registerDefaultMetaType( Class<?> genericType ) {
        this.metaTypeMap.put( genericType.getName(), genericType.getSimpleName().replace("Generic","") );
    }

    protected void registerDefaultMetaTypes() {
        this.registerDefaultMetaType( GenericFolder.class );
        this.registerDefaultMetaType( GenericFileNode.class );
    }

    public GenericFileSystemOperatorFactory(KOMFileSystem fileSystem, FileMasterManipulator fileMasterManipulator ){
        this.fileSystem = fileSystem;
        this.fileMasterManipulator = fileMasterManipulator;

        this.registerer.put(
                FileSystemOperatorFactory.DefaultFile,
                new GenericFileOperator( this )
        );

        this.registerer.put(
                FileSystemOperatorFactory.DefaultFolder,
                new GenericFolderOperator(this)
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
    public FileSystemOperator getOperator(String typeName ) {
        //Debug.trace( this.registerer.toString() );
        return (FileSystemOperator) this.registerer.get( typeName );
    }

    @Override
    public KOMFileSystem getFileSystem() {
        return this.fileSystem;
    }

    @Override
    public FileMasterManipulator getMasterManipulator() {
        return this.fileMasterManipulator;
    }
}
