package com.pinecone.hydra.file.creator;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.file.entity.FileNode;
import com.pinecone.hydra.file.entity.Folder;
import com.pinecone.hydra.file.entity.LocalFrame;
import com.pinecone.hydra.file.entity.RemoteFrame;
import com.pinecone.hydra.file.entity.Symbolic;
import com.pinecone.hydra.file.entity.SymbolicMeta;

public interface FileSystemCreator extends Pinenut {
     Folder              dummyFolder();

     FileNode            dummyFileNode();

     LocalFrame          dummyLocalFrame();

     RemoteFrame         dummyRemoteFrame();
     Symbolic            dummySymbolic();
     SymbolicMeta        dummySymbolicMeta();
}
