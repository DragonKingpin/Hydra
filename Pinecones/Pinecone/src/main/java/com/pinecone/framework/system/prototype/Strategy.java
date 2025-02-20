package com.pinecone.framework.system.prototype;

public interface Strategy extends Pinenut, Cloneable {
    boolean matched( Object condition );
}
