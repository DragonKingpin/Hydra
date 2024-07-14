package com.sauron.radium.heistron.scheduler;

import com.sauron.radium.system.Saunut;

public interface PageFrame64ConsumerAdapter extends Saunut {
    void consumeById( long index );
}
