package com.pinecone.hydra.storage.bucket.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface SiteNode extends Pinenut {
    long getEnumId();
    void setEnumId( long enumId );

    String getNodeName();
    void setNodeName( String nodeName );

    GUID getNodeGuid();
    void setNodeGuid( GUID nodeGuid );

    GUID getSiteGuid();
    void setSiteGuid( GUID siteGuid );

    int getState();
    void setState( int state );

    int getIsEnabled();
    void setIsEnabled( int isEnabled );

    GUID getRelatedService();
    void setRelatedService( GUID relatedService );
}
