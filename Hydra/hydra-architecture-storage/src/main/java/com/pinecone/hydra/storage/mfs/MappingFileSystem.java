package com.pinecone.hydra.storage.mfs;

import com.pinecone.hydra.system.ko.handle.AppliableKHandle;
import com.pinecone.hydra.system.ko.handle.ObjectTreeAddressingSectionHandle;

import java.io.IOException;

public interface MappingFileSystem extends ObjectTreeAddressingSectionHandle, AppliableKHandle {

    void copy( String sourcePath, String destinationPath ) throws IOException;

}
