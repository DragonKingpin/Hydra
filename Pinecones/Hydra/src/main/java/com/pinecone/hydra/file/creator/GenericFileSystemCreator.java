package com.pinecone.hydra.file.creator;

import com.pinecone.hydra.file.KOMFileSystem;
import com.pinecone.hydra.file.entity.FileNode;
import com.pinecone.hydra.file.entity.Folder;
import com.pinecone.hydra.file.entity.GenericFileNode;
import com.pinecone.hydra.file.entity.GenericFolder;
import com.pinecone.hydra.file.entity.GenericLocalFrame;
import com.pinecone.hydra.file.entity.GenericRemoteFrame;
import com.pinecone.hydra.file.entity.GenericSymbolic;
import com.pinecone.hydra.file.entity.GenericSymbolicMeta;
import com.pinecone.hydra.file.entity.LocalFrame;
import com.pinecone.hydra.file.entity.RemoteFrame;
import com.pinecone.hydra.file.entity.Symbolic;
import com.pinecone.hydra.file.entity.SymbolicMeta;
import com.pinecone.hydra.file.source.FileMasterManipulator;

public class GenericFileSystemCreator implements FileSystemCreator {
    private FileMasterManipulator       fileMasterManipulator;
    private KOMFileSystem               fileSystem;

    public GenericFileSystemCreator ( FileMasterManipulator fileMasterManipulator, KOMFileSystem fileSystem){
        this.fileMasterManipulator = fileMasterManipulator;
        this.fileSystem = fileSystem;
    }
    @Override
    public Folder dummyFolder(){
        return new GenericFolder(fileSystem,fileMasterManipulator.getFolderManipulator());
    }

    @Override
    public FileNode dummyFileNode(){
        return new GenericFileNode(fileSystem,fileMasterManipulator.getFileManipulator());
    }

    @Override
    public LocalFrame dummyLocalFrame(){
        return new GenericLocalFrame(fileMasterManipulator.getLocalFrameManipulator());
    }

    @Override
    public RemoteFrame dummyRemoteFrame(){
        return new GenericRemoteFrame(fileMasterManipulator.getRemoteFrameManipulator());
    }

    @Override
    public Symbolic dummySymbolic() {
        return new GenericSymbolic(this.fileMasterManipulator.getSymbolicManipulator());
    }

    @Override
    public SymbolicMeta dummySymbolicMeta() {
        return new GenericSymbolicMeta(this.fileMasterManipulator.getSymbolicMetaManipulator());
    }


}
