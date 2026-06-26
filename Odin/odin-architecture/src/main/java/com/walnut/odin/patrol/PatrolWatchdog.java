package com.walnut.odin.patrol;

import com.pinecone.framework.system.prototype.Pinenut;

public interface PatrolWatchdog extends Pinenut {

    void startService();

    void terminateService();

    boolean isEnabled();

    void enable();

    void disable();

    void enableRule( String ruleCode );

    void disableRule( String ruleCode );

    PatrolWatchdogSnapshot snapshot();

    void patrolOnce();
}
