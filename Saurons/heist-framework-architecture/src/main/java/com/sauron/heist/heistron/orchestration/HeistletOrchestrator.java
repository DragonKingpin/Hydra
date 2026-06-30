package com.sauron.heist.heistron.orchestration;

import com.pinecone.hydra.servgram.ServgramOrchestrator;
import com.sauron.heist.heistron.Heistgram;
import com.sauron.heist.heistron.orchestration.provider.HeistletProvider;
import com.sauron.heist.heistron.orchestration.provider.HeistletProviderRegistry;
import com.sauron.system.Saunut;

import java.util.List;

public interface HeistletOrchestrator extends Saunut, ServgramOrchestrator {
    Heistgram getHeistgram();

    HeistletProviderRegistry providerRegistry();

    default void addProvider( HeistletProvider provider ) {
        this.providerRegistry().addProvider( provider );
    }

    default void removeProvider( HeistletProvider provider ) {
        this.providerRegistry().removeProvider( provider );
    }

    List getPreloadPrefixes() ;

    List getPreloadSuffixes() ;
}
