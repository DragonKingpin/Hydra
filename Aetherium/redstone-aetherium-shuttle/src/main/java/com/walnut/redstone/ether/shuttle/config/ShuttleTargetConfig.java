package com.walnut.redstone.ether.shuttle.config;

import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;

public class ShuttleTargetConfig implements Pinenut {
    protected String name;
    protected String kind;
    protected String baseUrl;
    protected boolean enabled = true;
    protected int weight = 100;
    protected List<String> pathPrefixes = new ArrayList<>();

    public String getName() {
        return this.name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getKind() {
        return this.kind;
    }

    public void setKind( String kind ) {
        this.kind = kind;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public void setBaseUrl( String baseUrl ) {
        this.baseUrl = baseUrl;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled( boolean enabled ) {
        this.enabled = enabled;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight( int weight ) {
        this.weight = weight;
    }

    public List<String> getPathPrefixes() {
        return this.pathPrefixes;
    }

    public void setPathPrefixes( List<String> pathPrefixes ) {
        this.pathPrefixes = pathPrefixes == null ? new ArrayList<>() : new ArrayList<>( pathPrefixes );
    }
}
