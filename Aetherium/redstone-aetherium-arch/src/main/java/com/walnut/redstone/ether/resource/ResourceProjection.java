package com.walnut.redstone.ether.resource;

import com.pinecone.framework.system.prototype.Pinenut;

public class ResourceProjection implements Pinenut {
    protected String source;
    protected String target;
    protected String projectionType;
    protected ResourceRoute route;

    public String getSource() {
        return this.source;
    }

    public void setSource( String source ) {
        this.source = source;
    }

    public String getTarget() {
        return this.target;
    }

    public void setTarget( String target ) {
        this.target = target;
    }

    public String getProjectionType() {
        return this.projectionType;
    }

    public void setProjectionType( String projectionType ) {
        this.projectionType = projectionType;
    }

    public ResourceRoute getRoute() {
        return this.route;
    }

    public void setRoute( ResourceRoute route ) {
        this.route = route;
    }
}

