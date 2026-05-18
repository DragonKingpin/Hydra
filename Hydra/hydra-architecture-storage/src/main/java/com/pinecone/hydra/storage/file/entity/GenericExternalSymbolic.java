package com.pinecone.hydra.storage.file.entity;

import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.source.ExternalSymbolicManipulator;

public class GenericExternalSymbolic extends ArchReparseSemanticNode implements ExternalSymbolic {
    private ExternalSymbolicManipulator     externalSymbolicManipulator;


    public GenericExternalSymbolic() {
        super();
    }

    public GenericExternalSymbolic( KOMFileSystem fileSystem ) {
        super( fileSystem );
    }

    @Override
    public void create() {
        this.externalSymbolicManipulator.insert( this );
    }

    @Override
    public void remove() {
        this.externalSymbolicManipulator.remove( this.guid );
    }

    @Override
    public void apply(ExternalSymbolicManipulator externalSymbolicManipulator) {
        this.externalSymbolicManipulator = externalSymbolicManipulator;
    }
    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}
