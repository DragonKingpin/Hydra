package com.sauron.radium.heistron.chronic;

import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.auto.PeriodicAutomatron;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface PeriodicHeistRehearsal extends PeriodicHeistKernel {
    PeriodicAutomatron getAutomatron();

    List<String > getRawChronicPeriods();

    AtomicInteger getIndexId();

    @Override
    default void vitalize() {
        this.getAutomatron().start();
    }

    @Override
    default void joinVitalize() throws InterruptedException {
        this.vitalize();
        this.getAutomatron().join();
    }

    JSONObject getRaiderMarshalingConf();

    JSONObject getRaiderConfigs();
}
