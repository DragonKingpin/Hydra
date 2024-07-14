package com.sauron.radium.heistron.scheduler;

import com.sauron.radium.system.Saunut;

public interface TaskConsumer extends Saunut {
    void consume();
}
