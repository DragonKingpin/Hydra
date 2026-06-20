package com.pinecone.hydra.storage.file.source;

import com.pinecone.hydra.storage.bucket.source.BucketManipulator;
import com.pinecone.hydra.storage.file.fat.source.FileChunkLocationManipulator;
import com.pinecone.hydra.storage.file.fat.source.FileChunkManipulator;
import com.pinecone.hydra.storage.file.journal.source.JournalItemManipulator;
import com.pinecone.hydra.storage.file.journal.source.JournalManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;

public interface FileMasterManipulator extends KOIMasterManipulator {

    FileManipulator                     getFileManipulator();

    FolderManipulator                   getFolderManipulator();

    FileChunkManipulator                getFileChunkManipulator();

    FileChunkLocationManipulator        getFileChunkLocationManipulator();

    FileChildManipulator                getFileChildManipulator();

    JournalManipulator                  getJournalManipulator();

    JournalItemManipulator              getJournalItemManipulator();

    BucketManipulator                   getBucketManipulator();

    SymbolicManipulator                 getSymbolicManipulator();

    ExternalSymbolicManipulator         getExternalSymbolicManipulator();

}
