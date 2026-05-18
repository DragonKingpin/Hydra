package com.pinecone.hydra.business.entity;

import com.pinecone.framework.util.id.GUID;

public interface IdeaElement extends ElementNode {

    String getIdeaCode();

    void setIdeaCode( String szIdeaCode );

    String getIdeaStatus();

    void setIdeaStatus( String szIdeaStatus );

    GUID getRedMineGuid();

    void setRedMineGuid( GUID redMineGuid );

    String getValueLevel();

    void setValueLevel( String szValueLevel );

    @Override
    default IdeaElement evinceIdeaElement() {
        return this;
    }
}
