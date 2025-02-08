package com.sauron.shadow.chronicle;

import com.sauron.heist.heistron.Heistum;
import com.sauron.heist.heistron.chronic.ArchPeriodicHeistRehearsal;

public class ChroniclePeriodicHeistKernel extends ArchPeriodicHeistRehearsal {
    public ChroniclePeriodicHeistKernel( Heistum heistum, boolean bDaemon ) {
        super( heistum, bDaemon );
    }

    public ChroniclePeriodicHeistKernel( Heistum heistum ) {
        this( heistum, false );
    }
}
