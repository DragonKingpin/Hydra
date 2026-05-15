package com.pinecone.hydra.storage.mfs;

import java.net.URI;

import com.pinecone.hydra.unit.imperium.entity.EntityNode;

public interface MFile extends UFile, EntityNode {

    URI toURI();

    @Override
    String getName();

    String getPath();

    String getURI();

    boolean delete();

    Object getNativeHandle();

    boolean exists();

    boolean isAbsolute();

    boolean isDirectory();

    MFile[] listFiles();

    default String getMetaType() {
        return this.className().replace( MFile.class.getName(), "" );
    }

}
