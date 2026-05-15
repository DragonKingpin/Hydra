package com.pinecone.hydra.storage.file.entity;

import com.pinecone.framework.system.prototype.Pinenut;

public interface FSNodeAllotment extends Pinenut {
     Folder              newFolder();
     Folder              newFolder( String name );

     FileNode            newFileNode();
     FileNode            newFileNode( String name, long definitionSize, boolean crc32Xor, boolean integrityCheckEnable, boolean disableChunk);
     FileNode            newFileNode( String name, long definitionSize );
     FileNode            newFileNode( String name, boolean crc32Xor, boolean integrityCheckEnable, boolean disableChunk);

     Symbolic            newSymbolic();
}
