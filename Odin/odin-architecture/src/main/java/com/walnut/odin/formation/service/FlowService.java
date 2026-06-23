package com.walnut.odin.formation.service;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.conduct.schedule.UniformTaskScheduler;
import com.walnut.odin.formation.entity.RunEntry;

public interface FlowService extends Pinenut {

    void flow( RunEntry run, UniformTaskScheduler taskScheduler, String claimOwner );
}
