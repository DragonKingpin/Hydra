package com.pinecone.framework.system.construction.eval;

import com.pinecone.framework.system.prototype.Pinenut;

public interface StructureValueResolver extends Pinenut {
    StructureValueResolver DefaultResolver = new GenericStructureValueResolver();

    Object resolve( Object mapLiked, String szFieldKey, String szRawKey );
}
