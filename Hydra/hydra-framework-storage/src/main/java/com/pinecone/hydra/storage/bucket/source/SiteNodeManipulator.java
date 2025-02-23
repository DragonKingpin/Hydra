package com.pinecone.hydra.storage.bucket.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.bucket.entity.Site;
import com.pinecone.hydra.storage.bucket.entity.SiteNode;

import java.util.List;

public interface SiteNodeManipulator extends Pinenut {
    void insert(SiteNode siteNode );

    void remove( GUID siteNodeGuid );

    SiteNode querySiteNode( GUID siteNodeGuid );

    List<SiteNode> querySiteNodeBySiteGuid( GUID siteGuid );

    void update( SiteNode siteNode );
}
