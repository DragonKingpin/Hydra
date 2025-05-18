package com.pinecone.hydra.system.ko.runtime;

import com.pinecone.framework.system.regime.Instrument;
import com.pinecone.hydra.system.ko.handle.KHandle;
import com.pinecone.hydra.system.ko.handle.ObjectTreeAddressingSectionHandle;
import com.pinecone.hydra.system.ko.kom.KOMInstrument;

public interface CentralizedRuntimeInstrument extends RuntimeInstrument {

    KOMInstrument mount( String mountPointPath, KOMInstrument that );

    KOMInstrument mount( String mountPointPath, String treeNodeName, KOMInstrument that );

    KHandle mount( String mountPointPath, ObjectTreeAddressingSectionHandle that );

    KOMInstrument getMountedInstrument ( String mountPointPath );

}
