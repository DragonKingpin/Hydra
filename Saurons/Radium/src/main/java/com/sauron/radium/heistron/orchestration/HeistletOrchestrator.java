package com.sauron.radium.heistron.orchestration;

import com.pinecone.hydra.servgram.ServgramOrchestrator;
import com.sauron.radium.heistron.Heistgram;
import com.sauron.radium.system.Saunut;

import java.util.List;

public interface HeistletOrchestrator extends Saunut, ServgramOrchestrator {
    Heistgram getHeistgram();

    List getPreloadPrefixes() ;

    List getPreloadSuffixes() ;
}
