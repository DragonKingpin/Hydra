package com.pinecone.hydra.services;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.json.JSONObject;

public interface Server extends Pinenut {
    String getName();
    void setName( String name );

    String getNickName();
    void setNickName( String niceName );

    boolean isEnable();
    void setEnable( boolean enable );

    String getLocalDomain();
    void setLocalDomain( String localDomain );

    String getWideDomain();
    void setWideDomain(String wideDomain);

    JSONObject getExtras();
    void setExtras( JSONObject extras );

    Object get( Object key );
}
