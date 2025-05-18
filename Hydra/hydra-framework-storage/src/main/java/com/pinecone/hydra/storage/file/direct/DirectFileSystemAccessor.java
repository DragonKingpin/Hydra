package com.pinecone.hydra.storage.file.direct;

import com.pinecone.framework.system.regime.Instrument;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.ExternalSymbolic;
import com.pinecone.hydra.system.ko.handle.ObjectTreeAddressingSectionHandle;

import java.io.IOException;

public interface DirectFileSystemAccessor extends ObjectTreeAddressingSectionHandle {

    ElementNode queryElement( String path );

    void copy( String sourcePath, String destinationPath ) throws IOException;

}
