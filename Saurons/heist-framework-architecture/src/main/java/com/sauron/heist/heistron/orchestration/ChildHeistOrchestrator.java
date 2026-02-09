package com.sauron.heist.heistron.orchestration;

import com.pinecone.hydra.servgram.ServgramOrchestrator;
import com.sauron.heist.heistron.CascadeHeist;
import com.sauron.heist.heistron.Heistgram;
import com.sauron.heist.heistron.Heistium;
import com.sauron.system.Saunut;

public interface ChildHeistOrchestrator extends Saunut, ServgramOrchestrator {
    CascadeHeist getHeist();

    Heistium getHeistium();

    Heistgram getHeistgram();

    int nextAutoIncrementTaskId();
}
