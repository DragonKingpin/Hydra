package com.pinecone.framework.system.executum;

import com.pinecone.framework.system.prototype.Summoner;

public interface ExecutableSummoner extends Summoner {
    void executeAfterSummonSequence() throws Exception ;
}
