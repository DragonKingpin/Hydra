package com.sauron.heist.heistron;

import com.pinecone.tritium.system.MissionTerminateException;
import com.pinecone.tritium.system.TritiumSystem;
import com.pinecone.tritium.system.StorageSystem;

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
    TritiumSystem getSystem();

    StorageSystem getStorageSystem();

    Logger tracer();
}