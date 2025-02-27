package com.pinecone.hydra.server;

import com.pinecone.framework.util.json.JSONObject;

public abstract class ArchServer implements Server {
    protected String       name;
    protected String       nickName;
    protected boolean      enable;
    protected String       localDomain;
    protected String       wideDomain;
    protected JSONObject   extras;


    public String getName() {
        return this.name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName( String niceName ) {
        this.nickName = niceName;
    }

    public boolean isEnable() {
        return this.enable;
    }

    public void setEnable( boolean enable ) {
        this.enable = enable;
    }

    public String getLocalDomain() {
        return this.localDomain;
    }

    public void setLocalDomain( String localDomain ) {
        this.localDomain = localDomain;
    }

    public String getWideDomain() {
        return this.wideDomain;
    }

    public void setWideDomain( String wideDomain ) {
        this.wideDomain = wideDomain;
    }

    public JSONObject getExtras() {
        return this.extras;
    }

    public void setExtras( JSONObject extras ) {
        this.extras = extras;
    }

    public Object get( Object key ) {
        return this.extras.getMap().get( key );
    }

    @Override
    public String toJSONString() {
        return this.getExtras().toJSONString();
    }
}
