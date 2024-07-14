package com.pinecone.framework.system.prototype;

public interface Strategy extends Pinenut, Comparable<Strategy >, Cloneable {
    boolean matched();

    default int compareTo( Strategy that ){
        if( this.matched() && that.matched() ) {
            return 0;
        }
        else if( this.matched() && !that.matched() ) {
            return 1;
        }
        return -1;
    }
}
