package com.pinecone.hydra.system.component.infra;

import com.pinecone.framework.system.prototype.Pinenut;

public interface Middleware extends Pinenut {

    String typeName();

    String domainTypeName();

    WareDomain wareDomain();

}
