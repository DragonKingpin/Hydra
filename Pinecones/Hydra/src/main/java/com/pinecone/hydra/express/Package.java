package com.pinecone.hydra.express;

import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.framework.system.prototype.Pinenut;

public interface Package extends Pinenut {
    Hydrarum getSystem();

    Deliver getDeliver();

    String  getConsignee();

    Package entrust( Deliver deliver );
}
