package com.sauron.heist.heistron;

import com.pinecone.hydra.servgram.Orchestrator;
import com.pinecone.hydra.servgram.Servgramlet;
import com.sauron.heist.heistron.orchestration.ChildHeistOrchestrator;
import com.sauron.heist.heistron.orchestration.HeistletOrchestrator;
import com.pinecone.radium.system.Saunut;
import org.slf4j.Logger;
import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.slime.chunk.RangedPage;

public interface Heistum extends Saunut, Servgramlet {
    String heistName();

    @Override
    default String gramName() {
        return this.heistName();
    }

    @Override
    default String getName() {
        return this.taskName();
    }

    default String taskName() {
        return this.getHeistium().getName();
    }

    ChildHeistOrchestrator getThisHeistletOrchestrator();

    HeistletOrchestrator getGramHeistletOrchestrator();

    @Override
    JSONConfig getConfig();

    JSONConfig getProtoConfig();

    HeistScheme getHeistScheme();

    Heistgram getHeistgram();

    RangedPage getMasterTaskPage();

    Heistium getHeistium();

    Crew newCrew( int nCrewId ) ; // For Heistium to start the crew.

    int getMaximumThread();

    Logger tracer();

    void terminate();

    @Override
    default void execute() throws Exception {
        this.toHeist();
    }

    void toRavage();

    void toStalk();

    void toEmbezzle();

    void toHeist();



    void handleAliveException( Exception e );

    void handleKillException( Exception e ) throws IllegalStateException ;


    String ConfigChildrenKey      = "Children";
    String ConfigOrchestrationKey = Orchestrator.ConfigOrchestrationKey;

    String StatusStart            = "Start";
    String StatusDone             = "Done";
}