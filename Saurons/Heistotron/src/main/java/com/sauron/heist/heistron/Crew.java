package com.sauron.heist.heistron;

import com.pinecone.radium.system.MissionTerminateException;
import com.pinecone.radium.system.RadiumSystem;
import com.pinecone.radium.system.StorageSystem;

import org.slf4j.Logger;

public interface Crew extends Crewnium {
    String crewName();

    Heistum parentHeist();

    void validateSpoil( String sz );

    void isTimeToFeast();

    default void terminate(){
        throw new MissionTerminateException();
    }

    void startBatchTask();

    @Override
    RadiumSystem getSystem();

    StorageSystem getStorageSystem();

    Logger tracer();
}