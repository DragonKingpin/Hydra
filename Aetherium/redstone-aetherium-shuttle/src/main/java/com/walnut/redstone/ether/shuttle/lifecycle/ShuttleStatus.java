package com.walnut.redstone.ether.shuttle.lifecycle;

import com.pinecone.framework.system.prototype.Pinenut;

public class ShuttleStatus implements Pinenut {
    protected String name;
    protected boolean enabled;
    protected boolean running;
    protected String clientType;
    protected String defaultTarget;
    protected int targetCount;

    public String getName() {
        return this.name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled( boolean enabled ) {
        this.enabled = enabled;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void setRunning( boolean running ) {
        this.running = running;
    }

    public String getClientType() {
        return this.clientType;
    }

    public void setClientType( String clientType ) {
        this.clientType = clientType;
    }

    public String getDefaultTarget() {
        return this.defaultTarget;
    }

    public void setDefaultTarget( String defaultTarget ) {
        this.defaultTarget = defaultTarget;
    }

    public int getTargetCount() {
        return this.targetCount;
    }

    public void setTargetCount( int targetCount ) {
        this.targetCount = targetCount;
    }
}
