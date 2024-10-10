package com.pinecone.hydra.file.source;

import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;

public interface FileMasterManipulator extends KOIMasterManipulator {
    FileSystemAttributeManipulator getAttributeManipulator();
    FileManipulator             getFileManipulator();
    FileMetaManipulator         getFileMetaManipulator();
    FolderManipulator           getFolderManipulator();
    FolderMetaManipulator       getFolderMetaManipulator();
    LocalFrameManipulator       getLocalFrameManipulator();
    RemoteFrameManipulator      getRemoteFrameManipulator();
    SymbolicManipulator         getSymbolicManipulator();
    SymbolicMetaManipulator     getSymbolicMetaManipulator();
}
