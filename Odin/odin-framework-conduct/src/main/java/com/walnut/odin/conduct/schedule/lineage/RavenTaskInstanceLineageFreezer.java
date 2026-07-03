package com.walnut.odin.conduct.schedule.lineage;

import java.util.Collection;

import com.walnut.odin.atlas.graph.RuntimeAtlasInstrument;
import com.walnut.odin.conduct.schedule.entity.ScheduledTaskInstanceFrame;
import com.walnut.odin.conduct.schedule.entity.ScheduledTaskInstanceLineage;

public class RavenTaskInstanceLineageFreezer implements TaskInstanceLineageFreezer {

    protected RuntimeAtlasInstrument mRuntimeAtlasInstrument;

    public RavenTaskInstanceLineageFreezer( RuntimeAtlasInstrument runtimeAtlasInstrument ) {
        this.mRuntimeAtlasInstrument = runtimeAtlasInstrument;
    }

    @Override
    public Collection<ScheduledTaskInstanceLineage> freeze( Collection<ScheduledTaskInstanceFrame> frames ) {
        return this.mRuntimeAtlasInstrument.freezeInstanceLineages( frames );
    }
}
