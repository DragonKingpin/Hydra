package com.sauron.heist.heistron.scheduler;

import com.sauron.system.Saunut;

public interface PageFrame64ConsumerAdapter extends Saunut {
    void consumeById( long index );
}
