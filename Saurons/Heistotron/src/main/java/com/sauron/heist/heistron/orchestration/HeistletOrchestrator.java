package com.sauron.heist.heistron.orchestration;

import com.pinecone.hydra.servgram.ServgramOrchestrator;
import com.sauron.heist.heistron.Heistgram;
import com.sauron.system.Saunut;

import java.util.List;

public interface HeistletOrchestrator extends Saunut, ServgramOrchestrator {
    Heistgram getHeistgram();

    List getPreloadPrefixes() ;

    List getPreloadSuffixes() ;
}
