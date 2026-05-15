package com.pinecone.hydra.storage.file.entity;

import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.source.FileMasterManipulator;
import com.pinecone.framework.util.id.GuidAllocator;

public class GenericFSNodeAllotment implements FSNodeAllotment {
    private FileMasterManipulator       fileMasterManipulator;
    private KOMFileSystem               fileSystem;
    private GuidAllocator               guidAllocator;

    public GenericFSNodeAllotment(FileMasterManipulator fileMasterManipulator, KOMFileSystem fileSystem){
        this.fileMasterManipulator = fileMasterManipulator;
        this.fileSystem = fileSystem;
        this.guidAllocator   = fileSystem.getGuidAllocator();
    }
    @Override
    public Folder newFolder(){
        GenericFolder folder = new GenericFolder(fileSystem, fileMasterManipulator.getFolderManipulator());
        folder.setGuid( guidAllocator.nextGUID() );
        return folder;
    }



    @Override
    public Folder newFolder(String name) {
        GenericFolder folder = new GenericFolder(fileSystem, fileMasterManipulator.getFolderManipulator());
        folder.setName( name );
        folder.setGuid( guidAllocator.nextGUID() );
        return folder;
    }

    @Override
    public FileNode newFileNode(){
        GenericFileNode fileNode = new GenericFileNode(fileSystem, fileMasterManipulator.getFileManipulator());
        fileNode.setGuid( guidAllocator.nextGUID() );
        return fileNode;
    }
    @Override
    public FileNode newFileNode(String name, long definitionSize, boolean crc32Xor, boolean integrityCheckEnable, boolean disableChunk) {
        GenericFileNode fileNode = new GenericFileNode(fileSystem, fileMasterManipulator.getFileManipulator());
        fileNode.setGuid( guidAllocator.nextGUID() );
        fileNode.setName( name );
        //fileNode.setCrc32Xor( crc32Xor );
        fileNode.setDefinitionSize( definitionSize );
        fileNode.setIntegrityCheckEnable( integrityCheckEnable );
        fileNode.setDisableChunk( disableChunk );
        return fileNode;
    }

    @Override
    public FileNode newFileNode(String name, long definitionSize) {
        GenericFileNode fileNode = new GenericFileNode(fileSystem, fileMasterManipulator.getFileManipulator());
        fileNode.setName( name );
        fileNode.setDefinitionSize( definitionSize );
        fileNode.setGuid( guidAllocator.nextGUID() );
        return fileNode;
    }

    @Override
    public FileNode newFileNode(String name, boolean crc32Xor, boolean integrityCheckEnable, boolean disableChunk) {
        GenericFileNode fileNode = new GenericFileNode(fileSystem, fileMasterManipulator.getFileManipulator());
        fileNode.setName( name );
        //fileNode.setCrc32Xor( crc32Xor );
        fileNode.setDisableChunk( disableChunk );
        fileNode.setIntegrityCheckEnable( integrityCheckEnable );
        return fileNode;
    }
    @Override
    public Symbolic newSymbolic() {
        return new GenericSymbolic(this.fileMasterManipulator.getSymbolicManipulator());
    }

}
