package com.sauron.radium.heistron;

import com.sauron.radium.system.MissionTerminateException;
import com.sauron.radium.system.RadiumSystem;
import com.sauron.radium.system.StorageSystem;
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