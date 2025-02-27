package com.pinecone.hydra.system.identifier;

import com.pinecone.framework.util.name.path.BasicPathResolver;
import com.pinecone.hydra.system.ko.KernelObjectConfig;

public class KOPathResolver extends BasicPathResolver {
    public KOPathResolver( KernelObjectConfig config ) {
        super( config.getPathNameSeparator(), config.getPathNameSepRegex() );
    }
}
