package com.sauron.radium.system;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.system.prototype.Prototype;

public interface Saunut extends Pinenut {
    default String toJSONString() {
        return "\"[object " + this.className() +  "]\"";
    }

    default String prototypeName() {
        return this.className();
    }

    default String className() {
        return Prototype.prototypeName( this );
    }
}
