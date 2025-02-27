package com.pinecone.hydra.storage.file.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface FSNodeAllotment extends Pinenut {
     Folder              newFolder();
     Folder              newFolder( String name );

     FileNode            newFileNode();
     FileNode            newFileNode( String name, long definitionSize, boolean crc32Xor, boolean integrityCheckEnable, boolean disableCluster);
     FileNode            newFileNode( String name, long definitionSize );
     FileNode            newFileNode( String name, boolean crc32Xor, boolean integrityCheckEnable, boolean disableCluster);


     LocalCluster newLocalCluster();
     LocalCluster newLocalCluster(GUID fileGuid, int segId, String sourceName, long crc32, long size, long fileStartOffset );
     LocalCluster newLocalCluster(GUID fileGuid, int segId, String sourceName );

     RemoteCluster newRemoteCluster();
     RemoteCluster newRemoteCluster(GUID fileGuid, int segId, long crc32, long size );
     RemoteCluster newRemoteCluster(GUID fileGuid, int segId );
     Symbolic            newSymbolic();
     SymbolicMeta        newSymbolicMeta();
}
