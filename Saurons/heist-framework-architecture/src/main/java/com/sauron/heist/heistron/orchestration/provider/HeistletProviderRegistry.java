package com.sauron.heist.heistron.orchestration.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pinecone.hydra.servgram.Servgram;

public class HeistletProviderRegistry {
    protected final List<HeistletProvider> providers = new ArrayList<>();

    public synchronized void addProvider( HeistletProvider provider ) {
        if ( provider == null || this.providers.contains( provider ) ) {
            return;
        }
        this.providers.add( provider );
    }

    public synchronized void removeProvider( HeistletProvider provider ) {
        this.providers.remove( provider );
    }

    public synchronized List<HeistletProvider> providers() {
        return Collections.unmodifiableList( new ArrayList<>( this.providers ) );
    }

    public List<Servgram> popping( HeistletResolveContext context ) {
        for ( HeistletProvider provider : this.providers() ) {
            List<Servgram> heistlets = provider.popping( context );
            if ( heistlets != null && !heistlets.isEmpty() ) {
                return heistlets;
            }
        }
        return Collections.emptyList();
    }
}
