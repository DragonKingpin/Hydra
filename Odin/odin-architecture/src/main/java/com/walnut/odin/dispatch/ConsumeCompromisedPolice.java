package com.walnut.odin.dispatch;

import com.pinecone.framework.system.prototype.Pinenut;

public enum ConsumeCompromisedPolice implements Pinenut {

    EvictionIgnore("EvictionIgnore"),
    EvictionException("EvictionException"),
    BreakException("BreakException"),

    ;

    private final String value;

    ConsumeCompromisedPolice( String value ){
        this.value = value;
    }

    public String getName(){
        return this.value;
    }
}
