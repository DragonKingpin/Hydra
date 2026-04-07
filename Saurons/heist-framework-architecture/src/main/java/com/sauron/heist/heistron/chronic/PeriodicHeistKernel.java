package com.sauron.heist.heistron.chronic;

import com.sauron.heist.heistron.Heistgram;
import com.sauron.heist.heistron.Heistum;
import com.sauron.system.Saunut;

import java.util.List;

public interface PeriodicHeistKernel extends Saunut {
    Heistgram getHeistgram();

    Heistum getParentHeist();

    void vitalize();

    void joinVitalize() throws InterruptedException;

    List getPreloadPrefixes() ;

    List getPreloadSuffixes() ;
}
