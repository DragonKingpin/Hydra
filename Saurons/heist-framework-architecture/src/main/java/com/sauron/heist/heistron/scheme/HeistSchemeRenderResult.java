package com.sauron.heist.heistron.scheme;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.config.JSONConfig;

import java.util.ArrayList;
import java.util.List;

public class HeistSchemeRenderResult implements Pinenut {
    private String instanceName;
    private JSONConfig rawConfig;
    private JSONConfig resolvedConfig;
    private JSONConfig renderedConfig;
    private final List<HeistSchemeWarning> warnings = new ArrayList<>();

    public String getInstanceName() {
        return this.instanceName;
    }

    public HeistSchemeRenderResult setInstanceName( String instanceName ) {
        this.instanceName = instanceName;
        return this;
    }

    public JSONConfig getRawConfig() {
        return this.rawConfig;
    }

    public HeistSchemeRenderResult setRawConfig( JSONConfig rawConfig ) {
        this.rawConfig = rawConfig;
        return this;
    }

    public JSONConfig getResolvedConfig() {
        return this.resolvedConfig;
    }

    public HeistSchemeRenderResult setResolvedConfig( JSONConfig resolvedConfig ) {
        this.resolvedConfig = resolvedConfig;
        return this;
    }

    public JSONConfig getRenderedConfig() {
        return this.renderedConfig;
    }

    public HeistSchemeRenderResult setRenderedConfig( JSONConfig renderedConfig ) {
        this.renderedConfig = renderedConfig;
        return this;
    }

    public List<HeistSchemeWarning> getWarnings() {
        return this.warnings;
    }

    public HeistSchemeRenderResult addWarning( HeistSchemeWarning warning ) {
        if( warning != null ) {
            this.warnings.add( warning );
        }
        return this;
    }
}
