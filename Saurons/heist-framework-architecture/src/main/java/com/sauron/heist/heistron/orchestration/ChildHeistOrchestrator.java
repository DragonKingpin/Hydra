package com.sauron.heist.heistron.orchestration;

import com.pinecone.hydra.servgram.ServgramOrchestrator;
import com.sauron.heist.heistron.CascadeHeist;
import com.sauron.heist.heistron.Heistgram;
import com.sauron.heist.heistron.Heistium;
import com.sauron.heist.heistron.orchestration.provider.HeistletProvider;
import com.sauron.heist.heistron.orchestration.provider.HeistletProviderRegistry;
import com.sauron.system.Saunut;

public interface ChildHeistOrchestrator extends Saunut, ServgramOrchestrator {
    CascadeHeist getHeist();

    Heistium getHeistium();

    Heistgram getHeistgram();

    HeistletProviderRegistry providerRegistry();

    default void addProvider( HeistletProvider provider ) {
        this.providerRegistry().addProvider( provider );
    }

    default void removeProvider( HeistletProvider provider ) {
        this.providerRegistry().removeProvider( provider );
    }

    int nextAutoIncrementTaskId();
}
