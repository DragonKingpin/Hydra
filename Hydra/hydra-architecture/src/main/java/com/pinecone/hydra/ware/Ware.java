package com.pinecone.hydra.ware;

import com.pinecone.framework.system.prototype.Pinenut;

public interface Ware extends Pinenut {

    String typeName();

    String domainTypeName();

    WareDomain wareDomain();

}
