package com.pinecone.hydra.storage.file.direct;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.system.regime.Instrument;
import com.pinecone.hydra.storage.file.entity.ExternalSymbolic;

public interface DirectFileInstrument extends Instrument {

    void insertExternalSymbolic( ExternalSymbolic externalSymbolic );

    void createExternalSymbolic( String folderPath, String externalSymbolicName,String reparsedPoint );

}
