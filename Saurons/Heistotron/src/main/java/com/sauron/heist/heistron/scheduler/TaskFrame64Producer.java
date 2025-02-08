package com.sauron.heist.heistron.scheduler;

public interface TaskFrame64Producer extends TaskProducer {
    Long require();

    void deactivate( Long that );
}
