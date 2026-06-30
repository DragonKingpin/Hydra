package com.sauron.heist.heistron.orchestration.provider;

import com.sauron.heist.heistron.CascadeHeist;
import com.sauron.heist.heistron.Heistgram;

public class HeistletResolveContext {
    protected final Heistgram    heistgram;
    protected final CascadeHeist parentHeist;
    protected final String       heistName;
    protected final boolean      childResolve;

    public HeistletResolveContext( Heistgram heistgram, CascadeHeist parentHeist, String heistName, boolean childResolve ) {
        this.heistgram = heistgram;
        this.parentHeist = parentHeist;
        this.heistName = heistName;
        this.childResolve = childResolve;
    }

    public Heistgram getHeistgram() {
        return this.heistgram;
    }

    public CascadeHeist getParentHeist() {
        return this.parentHeist;
    }

    public String getHeistName() {
        return this.heistName;
    }

    public boolean isChildResolve() {
        return this.childResolve;
    }
}
