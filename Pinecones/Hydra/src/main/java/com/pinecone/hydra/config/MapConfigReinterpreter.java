package com.pinecone.hydra.config;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.unit.MultiScopeMap;

import java.util.Collection;
import java.util.Map;

public interface MapConfigReinterpreter extends Pinenut {
    MultiScopeMap<String, Object > getPrimaryScope();

    void setPrimaryScope( MultiScopeMap<String, Object > scope );

    Collection<String > getExcludeKeys();

    void addExcludeKey( String szKey );

    void addExcludeKeys( Collection<String > keys );

    void removeExcludeKey( String szKey );

    String getKeyWordsToken();

    void setKeyWordsToken( String szToken );

    void reinterpret( Map<String, Object > that );

    void reinterpret( Map<String, Object > that, MultiScopeMap<String, Object > scope );

    void reinterpretByBasicKeyWordsScope( Map<String, Object > that, MultiScopeMap<String, Object > keyWordsScope );

    void reinterpretByLineage( Map<String, Object > that, Object parent );
}
