package com.walnut.redstone.ether.shuttle.route;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.shuttle.config.ShuttleConfig;
import com.walnut.redstone.ether.shuttle.config.ShuttleTargetConfig;
import com.walnut.redstone.ether.shuttle.error.ShuttleErrorCode;
import com.walnut.redstone.ether.shuttle.error.ShuttleException;

public class ShuttleTargetResolver implements Pinenut {
    protected final ShuttleConfig config;

    public ShuttleTargetResolver( ShuttleConfig config ) {
        this.config = config;
    }

    public ShuttleTargetConfig resolve( String name ) {
        String targetName = this.blank( name ) ? this.config.getDefaultTarget() : name;
        if ( this.config.getTargets() == null ) {
            throw new ShuttleException( ShuttleErrorCode.InvalidConfig, "No shuttle target is configured." );
        }
        for ( ShuttleTargetConfig target : this.config.getTargets() ) {
            if ( targetName.equals( target.getName() ) && target.isEnabled() ) {
                return target;
            }
        }
        throw new ShuttleException( ShuttleErrorCode.InvalidConfig, "Shuttle target is disabled or not found: " + targetName );
    }

    protected boolean blank( String value ) {
        return value == null || value.trim().isEmpty();
    }
}
