package com.sauron.radium.heistron.chronic;

import com.sauron.radium.heistron.Heistgram;
import com.sauron.radium.heistron.Heistum;
import com.sauron.radium.system.Saunut;

import java.util.List;

public interface PeriodicHeistKernel extends Saunut {
    Heistgram getHeistgram();

    Heistum getParentHeist();

    void vitalize();

    void joinVitalize() throws InterruptedException;

    List getPreloadPrefixes() ;

    List getPreloadSuffixes() ;
}
