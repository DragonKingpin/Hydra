package com.pinecone.hydra.storage.mfs;

import java.io.File;

public interface NativeMFile extends MFile {

    String MetaType = NativeMFile.class.getSimpleName();

    @Override
    File getNativeHandle();

    @Override
    default String getMetaType() {
        return MetaType;
    }

}
