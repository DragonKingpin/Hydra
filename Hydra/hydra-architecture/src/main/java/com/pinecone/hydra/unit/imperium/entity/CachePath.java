package com.pinecone.hydra.unit.imperium.entity;

import com.pinecone.framework.system.prototype.Pinenut;

public interface CachePath extends Pinenut {
    String getPath();

    String getLongPath();

    default String getResolvedPath() {
        if ( this.getPath() == null ) {
            return null;
        }

        if ( this.getLongPath() == null ) {
            return this.getPath();
        }

        return this.getLongPath();
    }
}
