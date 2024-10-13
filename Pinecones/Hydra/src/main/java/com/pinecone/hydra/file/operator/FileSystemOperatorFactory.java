package com.pinecone.hydra.file.operator;

import com.pinecone.hydra.file.KOMFileSystem;
import com.pinecone.hydra.file.entity.FileNode;
import com.pinecone.hydra.file.entity.Folder;
import com.pinecone.hydra.file.source.FileMasterManipulator;
import com.pinecone.hydra.unit.udtt.operator.OperatorFactory;
import com.pinecone.hydra.unit.udtt.operator.TreeNodeOperator;

public interface FileSystemOperatorFactory extends OperatorFactory {
    String DefaultFile          =   FileNode.class.getSimpleName();
    String DefaultFolder        =   Folder.class.getSimpleName();

    void register( String typeName, TreeNodeOperator functionalNodeOperation );

    void registerMetaType( Class<?> clazz, String metaType );

    void registerMetaType( String classFullName, String metaType );

    String getMetaType( String classFullName );

    FileSystemOperator getOperator(String typeName );

    KOMFileSystem getFileSystem();

    FileMasterManipulator getMasterManipulator();
}
