package com.pinecone.hydra.umc.msg;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.system.Hydrogen;

public interface MessageNode extends Processum, MessageNodus {

    @Override
    Hydrogen parentSystem();

}
