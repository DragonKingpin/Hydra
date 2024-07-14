package com.pinecone.hydra.express;

import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.framework.system.prototype.Pinenut;

public interface Deliver extends Pinenut {
    Hydrarum getSystem();

    String   getName();

    Express  getExpress();

    void toDispatch( Package that ) throws Exception;
}
