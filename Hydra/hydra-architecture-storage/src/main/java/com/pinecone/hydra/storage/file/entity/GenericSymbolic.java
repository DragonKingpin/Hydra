package com.pinecone.hydra.storage.file.entity;

import com.pinecone.hydra.storage.file.source.SymbolicManipulator;

public class GenericSymbolic extends ArchReparseSemanticNode implements Symbolic {
    private SymbolicManipulator     symbolicManipulator;

    public GenericSymbolic() {
        super();
    }

    public GenericSymbolic( SymbolicManipulator symbolicManipulator ) {
        this.symbolicManipulator = symbolicManipulator;
    }

    @Override
    public void create() {
        this.symbolicManipulator.insert(this);
    }

    @Override
    public void remove() {
        this.symbolicManipulator.remove(this.guid);
    }

    public void apply( SymbolicManipulator symbolicManipulator ) {
        this.symbolicManipulator = symbolicManipulator;
    }
}
