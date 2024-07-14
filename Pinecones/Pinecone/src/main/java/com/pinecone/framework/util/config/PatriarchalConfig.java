package com.pinecone.framework.util.config;

import com.pinecone.framework.system.prototype.FamilyContext;

import java.io.IOException;
import java.nio.file.Path;

public interface PatriarchalConfig extends Config {
    PatriarchalConfig parent();

    default PatriarchalConfig root() {
        PatriarchalConfig p = this.parent();
        if( p == null ) {
            return this;
        }

        return p.root();
    }

    PatriarchalConfig getChild  ( Object key );

    PatriarchalConfig setParent ( Object parent );

    Path[] getParentPaths();

    PatriarchalConfig setParentPaths( Path[] path );

    PatriarchalConfig inherit( PatriarchalConfig parent ) ;

    FamilyContext getContext();

    PatriarchalConfig getChildFromPath( Path path ) throws IOException;
}
