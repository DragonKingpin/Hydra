package com.pinecone.hydra.task.kom.digest;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface TaskTreeElementDigest extends Pinenut {

    long getEnumId();

    void setEnumId( long nEnumId );

    GUID getGuid();

    void setGuid( GUID guid );

    GUID getParentGuid();

    void setParentGuid( GUID parentGuid );

    String getName();

    void setName( String szName );

    String getType();

    void setType( String szType );

    String getPath();

    void setPath( String szPath );

    String getLongPath();

    void setLongPath( String szLongPath );

    int getChildrenCount();

    void setChildrenCount( int nChildrenCount );

    default boolean isHasChildren() {
        return this.getChildrenCount() > 0;
    }
}
