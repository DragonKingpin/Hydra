package com.sauron.heist.heistron.orchestration.provider;

import java.util.List;

import com.pinecone.hydra.servgram.Servgram;

public interface HeistletProvider {

    /**
     * Resolves heist instances before the default local factory path is used.
     * Return an empty list when this provider does not own the requested heistlet.
     */
    List<Servgram> popping( HeistletResolveContext context );
}
