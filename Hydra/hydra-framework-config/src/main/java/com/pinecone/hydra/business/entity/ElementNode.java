package com.pinecone.hydra.business.entity;

import java.time.LocalDateTime;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public interface ElementNode extends TreeNode {

    String getType();

    void setType( String szType );

    void setGuid( GUID guid );

    void setName( String szName );

    String getCode();

    void setCode( String szCode );

    String getDescription();

    void setDescription( String szDescription );

    String getOwner();

    void setOwner( String szOwner );

    String getExtraInformation();

    void setExtraInformation( String szExtraInformation );

    String getKomPath();

    LocalDateTime getCreateTime();

    void setCreateTime( LocalDateTime createTime );

    LocalDateTime getUpdateTime();

    void setUpdateTime( LocalDateTime updateTime );

    default ElementNode evinceElementNode() {
        return this;
    }

    default ScenarioElement evinceScenarioElement() {
        return null;
    }

    default ProjectElement evinceProjectElement() {
        return null;
    }

    default IdeaElement evinceIdeaElement() {
        return null;
    }
}
