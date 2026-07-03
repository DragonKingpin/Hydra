package com.walnut.redstone.ether.shuttle.config;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.shuttle.error.ShuttleErrorCode;
import com.walnut.redstone.ether.shuttle.error.ShuttleException;

public class ShuttleConfigValidator implements Pinenut {
    public void validate( ShuttleConfig config ) {
        if ( config == null ) {
            throw new ShuttleException( ShuttleErrorCode.InvalidConfig, "Shuttle config is null." );
        }
        if ( this.blank( config.getName() ) ) {
            throw new ShuttleException( ShuttleErrorCode.InvalidConfig, "Shuttle name is blank." );
        }
        if ( this.blank( config.getDefaultTarget() ) ) {
            throw new ShuttleException( ShuttleErrorCode.InvalidConfig, "Default shuttle target is blank." );
        }
        if ( config.getTargets() == null || config.getTargets().isEmpty() ) {
            throw new ShuttleException( ShuttleErrorCode.InvalidConfig, "At least one shuttle target is required." );
        }
        boolean foundDefaultTarget = false;
        for ( ShuttleTargetConfig target : config.getTargets() ) {
            this.validateTarget( target );
            if ( config.getDefaultTarget().equals( target.getName() ) && target.isEnabled() ) {
                foundDefaultTarget = true;
            }
        }
        if ( !foundDefaultTarget ) {
            throw new ShuttleException( ShuttleErrorCode.InvalidConfig, "Default shuttle target is not enabled or not found." );
        }
    }

    protected void validateTarget( ShuttleTargetConfig target ) {
        if ( target == null ) {
            throw new ShuttleException( ShuttleErrorCode.InvalidConfig, "Shuttle target is null." );
        }
        if ( this.blank( target.getName() ) ) {
            throw new ShuttleException( ShuttleErrorCode.InvalidConfig, "Shuttle target name is blank." );
        }
        if ( this.blank( target.getBaseUrl() ) ) {
            throw new ShuttleException( ShuttleErrorCode.InvalidConfig, "Shuttle target baseUrl is blank." );
        }
    }

    protected boolean blank( String value ) {
        return value == null || value.trim().isEmpty();
    }
}
