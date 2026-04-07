package com.sauron.heist.heistron;

import com.sauron.heist.heistron.orchestration.Taskium;
import com.sauron.heist.heistron.scheduler.TaskProducer;

import java.util.concurrent.atomic.AtomicBoolean;

public interface Heistium extends Taskium {
    Heistum getParentHeist();

    TaskProducer getTaskProducer();

    void joinStartMultiTasks();

    void terminate();

    AtomicBoolean queryTerminationSignal();
}
