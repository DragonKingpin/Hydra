package com.walnut.odin.formation.recovery;

import java.time.LocalDateTime;

import com.pinecone.framework.system.regime.Supervisor;

public interface FormationRunReconciler extends Supervisor {

    void reconcileStartup();

    void reconcilePulse( LocalDateTime pulseTime );
}
