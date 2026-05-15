package com.walnut.odin.conduct.schedule.lineage;

import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.conduct.schedule.entity.ScheduledTaskInstanceFrame;
import com.walnut.odin.conduct.schedule.entity.ScheduledTaskInstanceLineage;

public interface TaskInstanceLineageFreezer extends Pinenut {

    Collection<ScheduledTaskInstanceLineage> freeze( Collection<ScheduledTaskInstanceFrame> frames );

}
