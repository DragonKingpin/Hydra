package com.sauron.radium.messagron;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.messagram.CentralMessagram;

import java.util.Map;

public class Messagron extends CentralMessagram {
    public Messagron( String szName, Processum parent, Map<String, Object > config ) {
        super( szName, parent, config );
    }

    @Override
    public String getLetsNamespace() {
        return this.getClass().getPackageName() + ".";
    }

}
