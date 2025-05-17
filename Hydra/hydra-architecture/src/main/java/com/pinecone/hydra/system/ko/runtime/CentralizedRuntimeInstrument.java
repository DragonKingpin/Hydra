package com.pinecone.hydra.system.ko.runtime;

import com.pinecone.hydra.system.ko.kom.KOMInstrument;

public interface CentralizedRuntimeInstrument extends RuntimeInstrument {

    KOMInstrument mount( String mountPointPath, KOMInstrument that );

    KOMInstrument mount( String mountPointPath, String treeNodeName, KOMInstrument that );

}
