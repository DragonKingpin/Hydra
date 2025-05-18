package com.pinecone.hydra.system.ko.handle;

import com.pinecone.hydra.system.ko.kom.KOMInstrument;

public interface KOMMountPointHandle extends ObjectTreeAddressingSectionHandle, KOMInstrument {

    KOMInstrument revealWrapped();

}
