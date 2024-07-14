package com.sauron.radium.heistron;

import com.sauron.radium.heistron.orchestration.Taskium;
import com.sauron.radium.heistron.scheduler.TaskProducer;

import java.util.concurrent.atomic.AtomicBoolean;

public interface Heistium extends Taskium {
    Heistum getParentHeist();

    TaskProducer getTaskProducer();

    void joinStartMultiTasks();

    void terminate();

    AtomicBoolean queryTerminationSignal();
}
