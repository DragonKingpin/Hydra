package com.pinecone.framework.system.construction;

import com.pinecone.framework.system.prototype.Pinenut;

public interface InstancePool<T > extends Pinenut {

    T allocate() ;

    void free( T obj ) ;

    int freeSize() ;

    int pooledSize();

    boolean isEmpty() ;

    void preAllocate( int count ) ;

    void setCapacity( int capacity ) ;

    int getCapacity() ;

}
