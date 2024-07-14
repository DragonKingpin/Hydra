package com.pinecone.framework.util.name;

import com.pinecone.framework.system.prototype.Pinenut;

public interface Name extends Pinenut {
    String      getName();

    String      getFullName();

    String      getDomain();

    void        setName( String szName );

    void        asStandardizedName( String szStandardizedName );

    default boolean isStandardizedName() {
        return this.getName().equals( this.getFullName() );
    }
}
