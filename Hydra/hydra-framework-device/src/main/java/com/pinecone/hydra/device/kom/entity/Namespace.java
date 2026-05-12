package com.pinecone.hydra.device.kom.entity;

import java.util.Set;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;

public interface Namespace extends FolderElement {
    Set<String > UnbeanifiedKeys = Set.of( "distributedTreeNode", "classificationRules" );

    long getEnumId();

    void setEnumId(long id);

    GUID getGuid();

    void setGuid(GUID guid);

    GUID getMetaGuid();

    void setMetaGuid(GUID metaGuid);

    String getName();

    void setName(String name);


    GUIDImperialTrieNode getDistributedTreeNode();

    void setDistributedTreeNode(GUIDImperialTrieNode distributedTreeNode);

    @Override
    default Namespace evinceNamespace() {
        return this;
    }

    JSONObject toJSONDetails();
}
