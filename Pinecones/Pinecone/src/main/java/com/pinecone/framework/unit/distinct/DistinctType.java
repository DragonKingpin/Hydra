package com.pinecone.framework.unit.distinct;

public enum DistinctType {
    SymmetricDistinct       ( "SymmetricDistinct"    ),
    SymmetricHomogeneity    ( "SymmetricHomogeneity" );

    private final String value;
    DistinctType( String value ){
        this.value = value;
    }

    public String getName(){
        return this.value;
    }
}
