package com.walnut.odin.formation.recovery;

import java.time.LocalDateTime;

import com.walnut.odin.formation.FormationConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightweightFormationRunReconciler implements FormationRunReconciler {

    private static final Logger log = LoggerFactory.getLogger( LightweightFormationRunReconciler.class );

    protected FormationConfig mConfig;

    public LightweightFormationRunReconciler( FormationConfig config ) {
        this.mConfig = config;
    }

    @Override
    public void reconcileStartup() {
        if ( this.mConfig.isFormationRecoveryEnabled() && this.mConfig.isFormationStartupReconcileEnabled() ) {
            log.info( "[OdinFormation] [StartupReconcile] (Mode: `lightweight`) <Pass>" );
        }
    }

    @Override
    public void reconcilePulse( LocalDateTime pulseTime ) {
        if ( this.mConfig.isFormationRecoveryEnabled() ) {
            log.debug( "[OdinFormation] [PulseReconcile] (PulseTime: `{}`) <Pass>", pulseTime );
        }
    }
}
