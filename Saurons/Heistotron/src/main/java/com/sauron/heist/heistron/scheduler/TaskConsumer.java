package com.sauron.heist.heistron.scheduler;

import com.sauron.system.Saunut;

public interface TaskConsumer extends Saunut {
    void consume();
}
