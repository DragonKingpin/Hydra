package com.sauron.radium.heistron.orchestration;

import com.pinecone.hydra.servgram.ServgramOrchestrator;
import com.sauron.radium.heistron.CascadeHeist;
import com.sauron.radium.heistron.Heistgram;
import com.sauron.radium.heistron.Heistium;
import com.sauron.radium.system.Saunut;

public interface ChildHeistOrchestrator extends Saunut, ServgramOrchestrator {
    CascadeHeist getHeist();

    Heistium getHeistium();

    Heistgram getHeistgram();

    int nextAutoIncrementTaskId();
}
