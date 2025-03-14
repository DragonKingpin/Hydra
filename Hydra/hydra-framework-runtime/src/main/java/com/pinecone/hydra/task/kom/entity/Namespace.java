package com.pinecone.hydra.task.kom.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.task.kom.GenericNamespaceRules;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import java.util.Set;

public interface Namespace extends FolderElement{
    Set<String > UnbeanifiedKeys = Set.of( "distributedTreeNode", "classificationRules" );

    long getEnumId();

    void setEnumId( long id );

    GUID getGuid();

    void setGuid( GUID guid );

    GUID getMetaGuid();

    void setMetaGuid( GUID metaGuid );

    String getName();

    void setName( String name );

    GUID getRulesGUID();

    void setRulesGUID( GUID rulesGUID );

    GenericNamespaceRules getClassificationRules();

    void setClassificationRules( GenericNamespaceRules classificationRules );

    GUIDImperialTrieNode getDistributedTreeNode();

    void setDistributedTreeNode( GUIDImperialTrieNode distributedTreeNode );

    @Override
    default Namespace evinceNamespace() {
        return this;
    }

    JSONObject toJSONDetails();
}
