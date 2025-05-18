package com.pinecone.hydra.system.ko.handle;

import com.pinecone.hydra.system.ko.kom.KOMInstrument;

public interface KOMMountPointHandle extends KHandle, KOMInstrument {

    KOMInstrument revealWrapped();

}
