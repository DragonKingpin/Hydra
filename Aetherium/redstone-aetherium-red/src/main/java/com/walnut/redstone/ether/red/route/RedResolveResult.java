package com.walnut.redstone.ether.red.route;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.red.uri.RedUri;
import com.walnut.redstone.ether.resource.ResourceProjection;
import com.walnut.redstone.ether.resource.ResourceRoute;

public class RedResolveResult implements Pinenut {
    protected RedUri uri;
    protected RedRouteType routeType;
    protected String backend;
    protected String target;
    protected ResourceRoute route;
    protected ResourceProjection projection;

    public RedUri getUri() {
        return this.uri;
    }

    public void setUri( RedUri uri ) {
        this.uri = uri;
    }

    public RedRouteType getRouteType() {
        return this.routeType;
    }

    public void setRouteType( RedRouteType routeType ) {
        this.routeType = routeType;
    }

    public String getBackend() {
        return this.backend;
    }

    public void setBackend( String backend ) {
        this.backend = backend;
    }

    public String getTarget() {
        return this.target;
    }

    public void setTarget( String target ) {
        this.target = target;
    }

    public ResourceRoute getRoute() {
        return this.route;
    }

    public void setRoute( ResourceRoute route ) {
        this.route = route;
    }

    public ResourceProjection getProjection() {
        return this.projection;
    }

    public void setProjection( ResourceProjection projection ) {
        this.projection = projection;
    }
}
